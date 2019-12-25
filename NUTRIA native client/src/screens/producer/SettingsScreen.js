import React from 'react';
import {Button, Card, Input, Text} from "react-native-elements";
import {ImageBackground, KeyboardAvoidingView, ScrollView, StyleSheet, View} from "react-native";
import Icon from 'react-native-vector-icons/FontAwesome';
import SimpleIcon from 'react-native-vector-icons/SimpleLineIcons';
import ProducerCertificateList from "../../components/ProducerCertificateList";
import ProducerActionList from "../../components/ProducerActionList";
import {connect} from "react-redux";
import {openQrScannerModal} from "../../store/actions/uiActionCreators";
import {images} from "../../../assets/images";
import Layout from "../../constants/Layout";
import {fetchSignUpFormData, updateProducer} from "../../store/actions/producerActionCreators";

class SettingsScreen extends React.Component {
    // static navigationOptions = ({navigation}) => {
    //     return {
    //         // headerLeft: (
    //         //     <View style={{flex: 1, flexDirection: 'row'}}>
    //         //         <Icon name="ios-arrow-back" onPress={() => navigation.navigate('Producer')}/>
    //         //         <Text>Back</Text>
    //         //     </View>
    //         // ),
    //         title: 'Producer Settings',
    //         // left: 'Back',
    //         // right: <Button title='Back' onPress={() => navigation.navigate('Producer')} />
    //     }
    // };

    state = {
        producerToUpdate: {
            username: '',
            password: '',
            passwordConfirmation: '',
            isPasswordValid: true,
            isConfirmationValid: true,
            producerName: '',
            ethereumAccount: '',
            isEthereumAccountValid: true,
            licenceNumber: '',
            website: '',
            certificates: [],
            actions: [],
            newCertificate: '',
            newAction: ''
        },
    };

    componentDidMount() {
        const {activeProducer, fetchSignUpFormInitData} = this.props;
        fetchSignUpFormInitData();
        this.setState({
            ...this.state,
            producerToUpdate: {
                ...this.state.producerToUpdate,
                username: activeProducer.username,
                isPasswordValid: true,
                isConfirmationValid: true,
                producerName: activeProducer.producerName,
                ethereumAccount: activeProducer.ethereumAccount,
                isEthereumAccountValid: true,
                licenceNumber: activeProducer.licenceNumber,
                website: activeProducer.url,
                certificates: activeProducer.producerCertificates,
                actions: activeProducer.producerActions,
                newCertificate: '',
                newAction: ''
            }
        })
    }

    handleActionToggleChange = (value, action) => {
        const producerToUpdate = value ? {
            ...this.state.producerToUpdate,
            actions: [...this.state.producerToUpdate.actions, action]
        } : {
            ...this.state.producerToUpdate,
            actions: [...this.state.producerToUpdate.actions.filter(act => act.actionName !== action.actionName)]
        };
        this.setState({producerToUpdate});
    };

    handleNewActionChangeText = newAction => {
        const producerToUpdate = {
            ...this.state.producerToUpdate,
            newAction
        };
        this.setState({producerToUpdate})
    };

    handleAddNewAction = () => {
        this.props.signUpFormInitData.actions.push({
            actionName: this.state.producerToUpdate.newAction
        });
        const producerToUpdate = {
            ...this.state.producerToUpdate,
            actions: [...this.state.producerToUpdate.actions, {
                actionName: this.state.producerToUpdate.newAction
            }],
            newAction: ''
        };
        this.setState({producerToUpdate});
        // this.shakeInput2 && this.shakeInput2.shake()
    };

    handleCertificateToggleChange = (value, certificate) => {
        const producerToUpdate = value ? {
            ...this.state.producerToUpdate,
            certificates: [...this.state.producerToUpdate.certificates, certificate]
        } : {
            ...this.state.producerToUpdate,
            certificates: [...this.state.producerToUpdate.certificates.filter(cert => cert.certificateName !== certificate.certificateName)]
        };
        this.setState({producerToUpdate});
    };

    handleNewCertificateChangeText = newCertificate => {
        const producerToUpdate = {
            ...this.state.producerToUpdate,
            newCertificate
        };
        this.setState({producerToUpdate})
    };

    handleAddNewCertificate = () => {
        this.props.signUpFormInitData.certificates.push({
            certificateName: this.state.producerToUpdate.newCertificate
        });
        const producerToUpdate = {
            ...this.state.producerToUpdate,
            certificates: [...this.state.producerToUpdate.certificates, {
                certificateName: this.state.producerToUpdate.newCertificate
            }],
            newCertificate: ''
        };
        this.setState({producerToUpdate});
        // this.shakeInput2 && this.shakeInput2.shake()
    };

    handleUpdateProducerPressed = () => {
        const {producerToUpdate} = this.state;
        const producerUpdateData = {
            producerName: producerToUpdate.producerName,
            url: producerToUpdate.website,
            licenceNumber: producerToUpdate.licenceNumber,
            ethereumAccount: producerToUpdate.ethereumAccount,
            producerActions: producerToUpdate.actions,
            producerCertificates: producerToUpdate.certificates
        };
        this.props.updateProducer(this.props.jwtToken, this.props.activeProducerId, producerUpdateData);
    };

    render() {
        const {translations} = this.props;
        return (
            <ImageBackground source={images.background}
                             style={{...styles.bgImage}}
            >

                <KeyboardAvoidingView
                    behavior="padding"
                    style={{
                        marginTop: 15,
                        flex: 1,
                        width: Layout.window.width * 0.9
                    }}
                    keyboardVerticalOffset={100}
                >
                    <ScrollView>
                        <View style={{backgroundColor: 'white'}}>


                            <Text style={{
                                fontWeight: 'bold',
                                fontSize: 22,
                                alignSelf: 'center',
                                marginTop: 15,
                                marginBottom: 10
                            }}>{translations.producerDetails}</Text>

                            <Input
                                leftIcon={
                                    <Icon
                                        name="user-o"
                                        color="rgba(0, 0, 0, 0.38)"
                                        size={25}
                                        style={{backgroundColor: 'transparent'}}
                                    />
                                }
                                value={this.state.producerToUpdate.producerName}
                                keyboardAppearance="light"
                                autoFocus={false}
                                autoCapitalize="none"
                                autoCorrect={false}
                                keyboardType="default"
                                returnKeyType="next"
                                inputStyle={{marginLeft: 10}}
                                placeholder={translations.producerName}
                                containerStyle={{
                                    marginBottom: 16,
                                    borderBottomColor: 'rgba(0, 0, 0, 0.38)',
                                }}
                                ref={input => (this.userInput = input)}
                                onSubmitEditing={() => this.userInput.focus()}
                                onChangeText={producerName => {
                                    const producerToUpdate = {
                                        ...this.state.producerToUpdate,
                                        producerName
                                    };
                                    this.setState({producerToUpdate})
                                }}
                                // errorMessage={
                                //     isEmailValid ? null : 'Please enter a username'
                                // }
                            />

                            <Input
                                leftIcon={
                                    <Icon
                                        name="drivers-license-o"
                                        color="rgba(0, 0, 0, 0.38)"
                                        size={25}
                                        style={{backgroundColor: 'transparent'}}
                                    />
                                }
                                value={this.state.producerToUpdate.licenceNumber}
                                keyboardAppearance="light"
                                autoFocus={false}
                                autoCapitalize="none"
                                autoCorrect={false}
                                keyboardType="default"
                                returnKeyType="next"
                                inputStyle={{marginLeft: 10}}
                                placeholder={translations.licenceNumber}
                                containerStyle={{
                                    marginBottom: 16,
                                    borderBottomColor: 'rgba(0, 0, 0, 0.38)',
                                }}
                                ref={input => (this.userInput = input)}
                                onSubmitEditing={() => this.userInput.focus()}
                                onChangeText={licenceNumber => {
                                    const producerToUpdate = {
                                        ...this.state.producerToUpdate,
                                        licenceNumber
                                    };
                                    this.setState({producerToUpdate})
                                }}
                                // errorMessage={
                                //     isEmailValid ? null : 'Please enter a username'
                                // }
                            />

                            <Input
                                leftIcon={
                                    <Icon
                                        name="user"
                                        color="rgba(0, 0, 0, 0.38)"
                                        size={25}
                                        style={{backgroundColor: 'transparent'}}
                                    />
                                }
                                value={this.state.producerToUpdate.username}
                                keyboardAppearance="light"
                                autoFocus={false}
                                autoCapitalize="none"
                                autoCorrect={false}
                                keyboardType="default"
                                returnKeyType="next"
                                inputStyle={{marginLeft: 10}}
                                placeholder={translations.username}
                                containerStyle={{
                                    marginBottom: 16,
                                    borderBottomColor: 'rgba(0, 0, 0, 0.38)',
                                }}
                                // ref={input => (this.userInput = input)}
                                // onSubmitEditing={() => this.userInput.focus()}
                                // onChangeText={username => {
                                //     const producerToUpdate = {
                                //         ...this.state.producerToUpdate,
                                //         username
                                //     };
                                //     this.setState({producerToUpdate})
                                // }}
                                // errorMessage={
                                //     isEmailValid ? null : 'Please enter a username'
                                // }
                            />

                            <Input
                                leftIcon={
                                    <SimpleIcon
                                        name="lock"
                                        color="rgba(0, 0, 0, 0.38)"
                                        size={25}
                                        style={{backgroundColor: 'transparent'}}
                                    />
                                }
                                value={this.state.producerToUpdate.password}
                                keyboardAppearance="light"
                                autoCapitalize="none"
                                autoCorrect={false}
                                secureTextEntry={true}
                                // returnKeyType={isSignUpPage ? 'next' : 'done'}
                                blurOnSubmit={true}
                                containerStyle={{
                                    marginBottom: 16,
                                    borderBottomColor: 'rgba(0, 0, 0, 0.38)',
                                }}
                                inputStyle={{marginLeft: 10}}
                                placeholder={translations.password}
                                ref={input => (this.passwordInput = input)}
                                // onSubmitEditing={() =>
                                //     isSignUpPage
                                //         ? this.confirmationInput.focus()
                                //         : this.login()
                                // }
                                onChangeText={password => {
                                    const producerToUpdate = {
                                        ...this.state.producerToUpdate,
                                        password
                                    };
                                    this.setState({producerToUpdate})
                                }}
                                errorMessage={
                                    this.state.producerToUpdate.isPasswordValid
                                        ? null
                                        : translations.atLeast8Characters
                                }
                            />

                            {/*<Input*/}
                            {/*    icon={*/}
                            {/*        <SimpleIcon*/}
                            {/*            name="lock"*/}
                            {/*            color="rgba(0, 0, 0, 0.38)"*/}
                            {/*            size={25}*/}
                            {/*            style={{backgroundColor: 'transparent'}}*/}
                            {/*        />*/}
                            {/*    }*/}
                            {/*    value={this.state.producerToUpdate.passwordConfirmation}*/}
                            {/*    secureTextEntry={true}*/}
                            {/*    keyboardAppearance="light"*/}
                            {/*    autoCapitalize="none"*/}
                            {/*    autoCorrect={false}*/}
                            {/*    keyboardType="default"*/}
                            {/*    returnKeyType={'done'}*/}
                            {/*    blurOnSubmit={true}*/}
                            {/*    containerStyle={{*/}
                            {/*        marginBottom: 16,*/}
                            {/*        borderBottomColor: 'rgba(0, 0, 0, 0.38)',*/}
                            {/*    }}*/}
                            {/*    inputStyle={{marginLeft: 10}}*/}
                            {/*    placeholder={'Confirm password'}*/}
                            {/*    ref={input => (this.confirmationInput = input)}*/}
                            {/*    onSubmitEditing={this.signUp}*/}
                            {/*    onChangeText={passwordConfirmation => {*/}
                            {/*        const producerToUpdate = {*/}
                            {/*            ...this.state.producerToUpdate,*/}
                            {/*            passwordConfirmation*/}
                            {/*        };*/}
                            {/*        this.setState({producerToUpdate})*/}
                            {/*    }}*/}
                            {/*    errorMessage={*/}
                            {/*        this.state.producerToUpdate.isConfirmationValid*/}
                            {/*            ? null*/}
                            {/*            : 'Please enter the same password'*/}
                            {/*    }*/}
                            {/*/>*/}


                            <Input
                                leftIcon={
                                    <Icon
                                        name="connectdevelop"
                                        color="rgba(0, 0, 0, 0.38)"
                                        size={25}
                                        style={{backgroundColor: 'transparent'}}
                                    />
                                }
                                value={this.state.producerToUpdate.ethereumAccount}
                                keyboardAppearance="light"
                                autoFocus={false}
                                autoCapitalize="none"
                                autoCorrect={false}
                                keyboardType="default"
                                returnKeyType="next"
                                inputStyle={{marginLeft: 10}}
                                placeholder={translations.ethereumAccount}
                                containerStyle={{
                                    marginBottom: 16,
                                    borderBottomColor: 'rgba(0, 0, 0, 0.38)',
                                }}
                                ref={input => (this.userInput = input)}
                                onSubmitEditing={() => this.userInput.focus()}
                                onChangeText={ethereumAccount => {
                                    const producerToUpdate = {
                                        ...this.state.producerToUpdate,
                                        ethereumAccount
                                    };
                                    this.setState({producerToUpdate})
                                }}
                                // errorMessage={
                                //     isEmailValid ? null : 'Please enter a username'
                                // }
                            />

                            <Input
                                leftIcon={
                                    <Icon
                                        name="chrome"
                                        color="rgba(0, 0, 0, 0.38)"
                                        size={25}
                                        style={{backgroundColor: 'transparent'}}
                                    />
                                }
                                value={this.state.producerToUpdate.website}
                                keyboardAppearance="light"
                                autoFocus={false}
                                autoCapitalize="none"
                                autoCorrect={false}
                                keyboardType="default"
                                returnKeyType="next"
                                inputStyle={{marginLeft: 10}}
                                placeholder={translations.websiteUrl}
                                containerStyle={{
                                    marginBottom: 16,
                                    borderBottomColor: 'rgba(0, 0, 0, 0.38)',
                                }}
                                ref={input => (this.userInput = input)}
                                onSubmitEditing={() => this.userInput.focus()}
                                onChangeText={website => {
                                    const producerToUpdate = {
                                        ...this.state.producerToUpdate,
                                        website
                                    };
                                    this.setState({producerToUpdate})
                                }}
                                // errorMessage={
                                //     isEmailValid ? null : 'Please enter a username'
                                // }
                            />

                            <Card title={translations.certificates} style={{
                                width: '100%'
                            }}>
                                <ProducerCertificateList
                                    heightPercent={0.3}
                                    certificates={this.props.signUpFormInitData.certificates}
                                    selectedCertificates={this.state.producerToUpdate.certificates}
                                    onCertificateToggleChange={(value, certificate) => this.handleCertificateToggleChange(value, certificate)}
                                    newCertificateValue={this.state.producerToUpdate.newCertificate}
                                    onNewCertificateChangeText={newCertificate => this.handleNewCertificateChangeText(newCertificate)}
                                    onAddNewCertificate={() => this.handleAddNewCertificate()}
                                />

                            </Card>

                            <Card title={translations.defaultActions} style={{
                                width: '100%',
                                marginBottom: 10
                            }}>
                                <ProducerActionList
                                    heightPercent={0.3}
                                    actions={this.props.signUpFormInitData.actions}
                                    selectedActions={this.state.producerToUpdate.actions}
                                    onActionToggleChange={(value, action) => this.handleActionToggleChange(value, action)}
                                    showNewActionInput={true}
                                    newActionValue={this.state.producerToUpdate.newAction}
                                    onNewActionChangeText={newAction => this.handleNewActionChangeText(newAction)}
                                    onAddNewAction={() => this.handleAddNewAction()}
                                />
                            </Card>
                        </View>
                        <Button activeOpacity={0.7} style={{
                            ...styles.buttonStyle,
                            width: Layout.window.width * 0.8,
                            marginTop: 15,
                            marginBottom: 15,
                            alignSelf: 'center'
                        }}
                                title={translations.updateProducerButton}
                                onPress={() => this.handleUpdateProducerPressed()}
                                loading={this.props.isLoading}
                                // disabled={this.props.isLoading}
                                disabled={true}
                        />
                    </ScrollView>
                </KeyboardAvoidingView>
            </ImageBackground>
        );
    }
}


const mapStateToProps = state => {
    return {
        activeProducer: state.producer.activeProducer,
        activeProducerId: state.producer.activeProducerId,
        jwtToken: state.producer.jwtToken,
        signUpFormInitData: state.producer.signUpFormInitData,
        isLoading: state.ui.isLoading,
        translations: state.languages.translations
    };
};

const mapDispatchToProps = dispatch => {
    return {
        fetchSignUpFormInitData: () => dispatch(fetchSignUpFormData()),
        onQrScannerModalOpen: () => dispatch(openQrScannerModal()),
        updateProducer: (token, producerId, producerData) => dispatch(updateProducer(token, producerId, producerData))
    }
};


export default connect(mapStateToProps, mapDispatchToProps)(SettingsScreen);

const styles = StyleSheet.create({
    container: {
        flex: 1
    },
    loginContainer: {
        alignItems: 'center',
        justifyContent: 'center'
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