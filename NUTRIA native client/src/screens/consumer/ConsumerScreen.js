import React from 'react';
import {ImageBackground, ScrollView, StyleSheet, View} from 'react-native';
import {connect} from "react-redux";
import {images} from "../../../assets/images";
import {Button as BaseButton, Text as NativeText} from "native-base";
import {openMapViewModal, openQrScannerModal} from "../../store/actions/uiActionCreators";
import QrScannerModal from "../qrScanner/QrScannerModal";
import Common from "../../constants/Common";
import MapViewModal from "../map/MapViewModal";
import {Button, Card, ListItem} from "react-native-elements";
import {setPTForMapView} from "../../store/actions/mapActionCreators";
import Layout from "../../constants/Layout";

const SCREEN_WIDTH = Layout.window.width;
const SCREEN_HEIGHT = Layout.window.height;

class ConsumerScreen extends React.Component {
    static navigationOptions = ({navigation}) => {
        return {
            title: navigation.getParam('otherParam', 'Consumer Mode'),
            headerLeft: <BaseButton hasText transparent onPress={() => navigation.navigate('Home')}>
                <NativeText>Home</NativeText>
            </BaseButton>
        };
    };

    onScannedProductButtonGroupPressed = (index, pt) => {
        switch (index) {
            case 0: {
                this.showPTDetails(pt);
            }
            // case 1: {
            //     this.removeSelectedPT(pt);
            // }
        }
    };

    showPTDetails = productTag => {
        this.props.setPTForMapView(productTag);
        this.props.onMapViewModalOpen();
    };

    render() {
        const {width, height} = Layout.window;
        const {translations} = this.props;
        return (
            <ImageBackground source={images.background} style={styles.bgImage}>
                <Card
                      title={translations.scannedProducts}>
                    <View style={{
                        height: height * 0.6,
                        width: width * 0.8
                    }}>
                        <ScrollView>
                            {this.props.scannedProductTags.map(pt => {
                                const hash = pt.hash;
                                return <ListItem key={hash}
                                                 title={new Date(pt.ptDetails.dateTime).toLocaleDateString()}
                                                 buttonGroup={{
                                                     buttons: [translations.details],
                                                     onPress: (index) => this.onScannedProductButtonGroupPressed(index, pt, hash)
                                                 }}
                                                 bottomDivider
                                />
                            })}
                        </ScrollView>
                    </View>
                </Card>

                {this.props.isQrScannerModalOpen ?
                    <QrScannerModal mode={Common.mode.CONSUMER}/> : null}
                {this.props.isMapViewModalOpen ?
                    <MapViewModal mode={Common.mode.CONSUMER}/> : null}
                <Button activeOpacity={0.7} style={{
                    ...styles.buttonStyle,
                    width: width * 0.8,
                    marginTop: 15,
                    marginBottom: 15,
                    alignSelf: 'center'
                }}
                        title={translations.scanButton}
                    onPress={() =>  this.props.onQrScannerModalOpen()}/>
            </ImageBackground>
        );
    }
}

const mapStateToProps = state => {
    return {
        isQrScannerModalOpen: state.ui.isQrScannerModalOpen,
        isMapViewModalOpen: state.ui.isMapViewModalOpen,
        scannedProductTags: state.consumer.scannedProductTags,
        translations: state.languages.translations
    };
};


const mapDispatchToProps = dispatch => {
    return {
        // onConsumerScreenOpen: () => dispatch(openQrScanner()),
        onQrScannerModalOpen: () => dispatch(openQrScannerModal()),
        setPTForMapView: productTag => dispatch(setPTForMapView(productTag)),
        onMapViewModalOpen: () => dispatch(openMapViewModal()),
    }
};


export default connect(mapStateToProps, mapDispatchToProps)(ConsumerScreen);


const styles = StyleSheet.create({
    bgImage: {
        flex: 1,
        top: 0,
        left: 0,
        width: SCREEN_WIDTH,
        height: SCREEN_HEIGHT,
        justifyContent: 'center',
        alignItems: 'center',
    },
    buttonStyle: {
        textAlign: 'center',
        color: 'white',
        fontSize: 24,
        fontWeight: 'bold',
        backgroundColor: 'transparent',
    },
});

