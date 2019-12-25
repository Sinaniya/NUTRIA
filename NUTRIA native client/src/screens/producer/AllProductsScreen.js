import React from 'react';
import {Card, ListItem} from "react-native-elements";

import {ImageBackground, ScrollView, StyleSheet, View, Alert} from "react-native";
import Layout from "../../constants/Layout";
import {images} from "../../../assets/images";
import {
    onProducersAllPTScreenOpen
} from "../../store/actions/producerActionCreators";
import {openMapViewModal} from "../../store/actions/uiActionCreators";
import {setPTForMapView} from "../../store/actions/mapActionCreators";
import {connect} from "react-redux";

import printQrCode from '../../util/qrCodePrintUtil'

class AllProductsScreen extends React.Component {

    componentDidMount() {
        const {jwtToken, activeProducerId, onProducersAllPTScreenOpen} = this.props;
        onProducersAllPTScreenOpen(jwtToken, activeProducerId)
    }

    onPTButtonGroupPressed = async (index, pt, hash) => {
        switch (index) {
            case 0: {
                this.showPTDetails(pt);
                return ;
            }
            case 1: {
                await this.handlePrintQrCode(hash);
            }
        }
    };

    showPTDetails = productTag => {
        this.props.setPTForMapView(productTag);
        this.props.onMapViewModalOpen();
    };

    handlePrintQrCode = async hash => {
        try {
            await printQrCode(hash);
        } catch (e) {
            console.log('Printing not completed.');
        }
    };

    render() {
        const {translations} = this.props;
        const {width, height} = Layout.window;
        return (
            <ImageBackground source={images.background} style={styles.bgImage}>
                <Card style={{
                    width: width,
                    height: height * 0.8
                }} title={translations.allMyProducts}>
                    <View style={{
                        height: height * 0.7,
                        width: width * 0.8
                    }}>
                        <ScrollView>
                            {this.props.productTags.map(pt => {
                                const hash = pt.hash;
                                return <ListItem key={hash}
                                                 title={new Date(pt.ptDetails.dateTime).toDateString()}
                                                 buttonGroup={{
                                                     buttons: [translations.details, translations.printQR],
                                                     onPress: (index) => this.onPTButtonGroupPressed(index, pt, hash)
                                                 }}
                                                 bottomDivider
                                />
                            })}
                        </ScrollView>
                    </View>
                </Card>
            </ImageBackground>
        );
    }
}


const mapStateToProps = state => {
    return {
        producerData: state.producer.activeProducer,
        activeProducerId: state.producer.activeProducerId,
        jwtToken: state.producer.jwtToken,
        isMapViewModalOpen: state.ui.isMapViewModalOpen,
        isLoading: state.ui.isLoading,
        productTags: state.producer.allProducersProductTags,
        translations: state.languages.translations
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onMapViewModalOpen: () => dispatch(openMapViewModal()),
        setPTForMapView: productTag => dispatch(setPTForMapView(productTag)),
        onProducersAllPTScreenOpen: (token, producerId) => dispatch(onProducersAllPTScreenOpen(token, producerId))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(AllProductsScreen);


const styles = StyleSheet.create({
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