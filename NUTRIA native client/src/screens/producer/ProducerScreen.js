import React, {Component} from 'react';
import {
    Alert,
    ImageBackground,
    KeyboardAvoidingView,
    Platform,
    ScrollView,
    StyleSheet,
    Text,
    TouchableOpacity,
    View
} from "react-native";
import {Constants, Location, Permissions} from 'expo';
import {Icon} from "native-base";
import {connect} from "react-redux";
import {Button, Card, ListItem} from "react-native-elements";
import Layout from "../../constants/Layout";
import Common from "../../constants/Common";
import {
    fetchProducerData,
    fetchSignUpFormData,
    generateNewProductTag,
    producerSignOut,
    removeScannedPt
} from "../../store/actions/producerActionCreators";
import {setPTForMapView} from "../../store/actions/mapActionCreators";
import QrScannerModal from "../qrScanner/QrScannerModal";
import {closeQrScannerModal, openMapViewModal, openQrScannerModal} from "../../store/actions/uiActionCreators";
import MapViewModal from "../map/MapViewModal";
import ProducerActionList from "../../components/ProducerActionList";
import ptUpdateUtil from '../../util/ptUpdateUtil';
import {images} from "../../../assets/images";
import printQrCode from '../../util/qrCodePrintUtil'

class ProducerScreen extends Component {
    state = {
        componentInitMount: true,
        locationErrorMessage: '',
        newActionValue: '',
        newProductTag: {
            longitude: null,
            latitude: null,
            previousProductTagHashes: [],
            productTagActions: [],
            productTagProducer: {
                producerId: null
            }
        }
    };

    static navigationOptions = ({navigation}) => {
        return {
            headerLeft: (
                <TouchableOpacity style={{flex: 1, flexDirection: 'row', alignItems: 'center', marginLeft: 5}}
                                  onPress={() => {
                                      navigation.getParam('signOut')();
                                  }}>
                    <Icon style={{color: 'red'}} name="ios-log-out"/>
                    <Text style={{color: 'red', marginLeft: 5}}>{navigation.getParam('signOutText')}</Text>
                </TouchableOpacity>
            ),
            title: navigation.getParam('mode'),
            headerTitleStyle: {
                marginLeft: Platform.OS === 'android' ? 50 : 0
            },
            headerRight: (
                <TouchableOpacity style={{flex: 1, flexDirection: 'row', alignItems: 'center', marginRight: 5}}
                                  onPress={() => {
                                      navigation.getParam('openSettingsScreen')();
                                  }}>
                    <Text style={{color: 'blue', marginRight: 5}}>{navigation.getParam('settingsText')}</Text>
                    <Icon style={{color: 'blue'}} name="ios-settings"/>
                </TouchableOpacity>
            )
        }
    };

    componentDidMount() {
        const {navigation, translations, onProducerScreenMounted} = this.props;
        onProducerScreenMounted(this.props.jwtToken, this.props.activeProducerId);
        navigation.setParams({
            openSettingsScreen: this.openSettingsScreen,
            signOut: this.signOut,
            signOutText: translations.signOut,
            mode: translations.producerMode,
            settingsText: translations.settings
        });
        if (this.props.allActions.length === 0) {
            this.props.onSignUpDataNotLoaded();
        }
    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.producerData && this.state.componentInitMount) {
            this.setState({
                ...this.state,
                componentInitMount: false,
                newProductTag: {
                    ...this.state.newProductTag,
                    productTagActions: nextProps.producerData.producerActions,
                    productTagProducer: {
                        producerId: nextProps.producerData.producerId
                    }
                }
            })
        }
        if (this.props.numberOfGeneratedProductTags < nextProps.numberOfGeneratedProductTags) {
            if (nextProps.productTagSuccessfullyGenerated) {
                this.showAlertOnSuccessGeneratedProductTag(nextProps);
            } else {
                this.showAlertOnErrorGeneratedProductTag(nextProps);
            }
        }
    }

    openSettingsScreen = () => {
        this.props.navigation.navigate('ProducerSettings');
    };

    signOut = async () => {
        // await AsyncStorage.clear();
        this.props.navigation.navigate('Auth');
        this.props.handleSignOutPressed();
    };


    onScannedProductButtonGroupPressed = (index, pt) => {
        switch (index) {
            case 0: {
                this.showPTDetails(pt);
                break;
            }
            case 1: {
                this.removeSelectedPT(pt);
                break;
            }
        }
    };

    showPTDetails = productTag => {
        this.props.setPTForMapView(productTag);
        this.props.onMapViewModalOpen();
    };

    removeSelectedPT = productTag => {
        this.props.onRemoveScannedPTPressed(productTag.hash);
    };

    handleActionToggleChange = (value, action) => {
        const newProductTag = value ? {
            ...this.state.newProductTag,
            productTagActions: [...this.state.newProductTag.productTagActions, action]
        } : {
            ...this.state.newProductTag,
            productTagActions: [...this.state.newProductTag.productTagActions.filter(act => act.actionName !== action.actionName)]
        };
        this.setState({newProductTag});
    };

    _getLocationAsync = async () => {
        if (Platform.OS === 'android' && !Constants.isDevice) {
            this.setState({
                errorMessage: 'Oops, this will not work on Sketch in an Android emulator. Try it on your device!',
            });
        } else {
            let {status} = await Permissions.askAsync(Permissions.LOCATION);
            if (status !== 'granted') {
                this.setState({
                    locationErrorMessage: 'Permission to access location was denied!'
                });
            }

            const location = await Location.getCurrentPositionAsync({});
            this.setState({
                ...this.state,
                newProductTag: {
                    ...this.state.newProductTag,
                    longitude: location.coords.longitude,
                    latitude: location.coords.latitude
                }
            });
        }

    };

    showNoPTAlert = () => {
        const {translations} = this.props;
        Alert.alert(
            translations.warning,
            translations.ptNotScanned,
            [
                {
                    text: translations.yesCreatePt, onPress: () => {
                        const productTag = {
                            ...this.state.newProductTag,
                            previousProductTagHashes: [Common.GENESIS_PRODUCT_TAG_HASH_VALUE]
                        };
                        this.props.generateNewProductTag(this.props.jwtToken, productTag);
                    }
                },
                {text: translations.scanButton, onPress: () => this.props.onQrScannerModalOpen()},
                {text: translations.close, onPress: () => console.log('Close Pressed')},
            ],
            {cancelable: false}
        )
    };

    showNoActionsAlert = () => {
        const {translations} = this.props;
        Alert.alert(
            translations.warning,
            translations.noActionsSelected,
            [
                {text: translations.close, onPress: () => console.log('Close Pressed')},
            ],
            {cancelable: false}
        )
    };

    onGenerateNewProductTagPressed = async () => {
        await this._getLocationAsync();
        if (this.props.scannedProductTags.length === 0) {
            this.showNoPTAlert();
        } else if (this.state.newProductTag.productTagActions.length === 0) {
            this.showNoActionsAlert();
        } else {
            const previousProductTagHashes = this.props.scannedProductTags.map(productTag => productTag.hash);
            await this.setState({newProductTag: {...this.state.newProductTag, previousProductTagHashes}});
            this.props.generateNewProductTag(this.props.jwtToken, this.state.newProductTag);
        }
    };

    showAlertOnErrorGeneratedProductTag = nextProps => {
        const {translations} = this.props;
        Alert.alert(
            translations.error,
            translations.errorWhileGeneratingPt,
            [
                {text: translations.tryAgain, onPress: () => this.onGenerateNewProductTagPressed()},
                {text: translations.close, onPress: () => console.log('Close Pressed')},
            ],
            {cancelable: false}
        )
    };

    showAlertOnSuccessGeneratedProductTag = nextProps => {
        const {translations} = this.props;

        Alert.alert(
            translations.success,
            translations.ptGenerated,
            [
                {
                    text: translations.printQR,
                    onPress: () => this.handlePrintQrPressed(nextProps.generatedProductTag.hash)
                },
                {
                    text: translations.showDetails, onPress: () => {
                        this.props.setPTForMapView(ptUpdateUtil(nextProps.generatedProductTag));
                        this.props.onMapViewModalOpen();
                    }
                },
                {text: translations.close, onPress: () => console.log('Close Pressed')},
            ],
            {cancelable: false}
        )
    };

    handlePrintQrPressed = async hash => {
        try {
            await printQrCode(hash);
        } catch (e) {
            console.error('error while printing');
        }
    };

    render() {
        const {translations} = this.props;
        const {width, height} = Layout.window;
        return (
            <ImageBackground source={images.background} style={styles.bgImage}>
                <KeyboardAvoidingView
                    behavior="position"
                    style={{
                        width: width
                        // alignItems: 'center',
                        // margin: -20
                    }}
                >
                    <ScrollView>
                        {/*<Button title="Show me more of the app" onPress={this.openSettingsScreen}/>*/}
                        {/*<Button title="Actually, sign me out :)" onPress={this.signOut}/>*/}


                        <Button activeOpacity={0.7} style={{
                            ...styles.buttonStyle,
                            width: width * 0.8,
                            marginTop: 15,
                            alignSelf: 'center'
                        }} title={translations.allMyProducts} onPress={() => this.props.navigation.navigate('AllProductsScreen')}/>


                        <Card style={{width: width}} title={translations.scannedProducts}>
                            <View>
                                <ScrollView style={{
                                    height: height * 0.3,
                                    // width: '100%'
                                }}>
                                    {this.props.scannedProductTags.map(pt => {
                                        const hash = pt.hash;
                                        return <ListItem key={hash}
                                                         title={new Date(pt.ptDetails.dateTime).toLocaleDateString()}
                                                         buttonGroup={{
                                                             buttons: ['Details', 'Remove'],
                                                             onPress: (index) => this.onScannedProductButtonGroupPressed(index, pt, hash)
                                                         }}
                                        />
                                    })}
                                </ScrollView>
                            </View>
                        </Card>

                        <Button activeOpacity={0.7} style={{
                            ...styles.buttonStyle,
                            width: width * 0.8,
                            marginTop: 15,
                            alignSelf: 'center'
                        }} title={translations.scanButton} onPress={() => this.props.onQrScannerModalOpen()}/>

                        <Card title={translations.productTagActions} style={{
                            width: '100%'
                        }}>
                            <ProducerActionList
                                heightPercent={0.3}
                                actions={this.props.allActions}
                                selectedActions={this.state.newProductTag.productTagActions}
                                onActionToggleChange={(value, action) => this.handleActionToggleChange(value, action)}
                                showNewActionInput={false}
                            />
                        </Card>

                        {this.props.isQrScannerModalOpen ? <QrScannerModal mode={Common.mode.PRODUCER}/> : null}
                        {this.props.isMapViewModalOpen ? <MapViewModal mode={Common.mode.PRODUCER}/> : null}

                        <Button activeOpacity={0.7} style={{
                            ...styles.buttonStyle,
                            width: width * 0.8,
                            marginTop: 15,
                            marginBottom: 15,
                            alignSelf: 'center'
                        }}
                                title={translations.generatePTButton}
                                onPress={() => this.onGenerateNewProductTagPressed()}
                                loading={this.props.isLoading}
                                disabled={this.props.isLoading}
                        />
                    </ScrollView>
                </KeyboardAvoidingView>
            </ImageBackground>
        );
    }

}

const mapStateToProps = state => {
    return {
        producerData: state.producer.activeProducer,
        activeProducerId: state.producer.activeProducerId,
        jwtToken: state.producer.jwtToken,
        scannedProductTags: state.producer.scannedProductTags,
        isAlertOnScanOpen: state.producer.isAlertOnScanOpen,
        isQrScannerModalOpen: state.ui.isQrScannerModalOpen,
        isMapViewModalOpen: state.ui.isMapViewModalOpen,
        isLoading: state.ui.isLoading,
        allActions: state.producer.signUpFormInitData.actions,
        numberOfGeneratedProductTags: state.producer.numberOfGeneratedProductTags,
        productTagSuccessfullyGenerated: state.producer.productTagSuccessfullyGenerated,
        generatedProductTag: state.producer.generatedProductTag,
        translations: state.languages.translations
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onSignUpDataNotLoaded: () => dispatch(fetchSignUpFormData()),
        onProducerScreenMounted: (token, producerId) => dispatch(fetchProducerData(token, producerId)),
        onQrScannerModalOpen: () => dispatch(openQrScannerModal()),
        onQrScannerModalClose: () => dispatch(closeQrScannerModal()),
        onMapViewModalOpen: () => dispatch(openMapViewModal()),
        setPTForMapView: productTag => dispatch(setPTForMapView(productTag)),
        generateNewProductTag: (jwtToken, productTagData) => dispatch(generateNewProductTag(jwtToken, productTagData)),
        handleSignOutPressed: () => dispatch(producerSignOut()),
        onRemoveScannedPTPressed: hash => dispatch(removeScannedPt(hash))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ProducerScreen);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    settingsIcon: {
        marginRight: 10
    },
    buttonStyle: {
        textAlign: 'center',
        color: 'white',
        fontSize: 24,
        fontFamily: 'light',
        fontWeight: 'bold',
        backgroundColor: 'transparent',
    },
    bgImage: {
        flex: 1,
        top: 0,
        left: 0,
        width: Layout.window.width,
        height: Layout.window.height,
        justifyContent: 'center',
        alignItems: 'center',
    }
});