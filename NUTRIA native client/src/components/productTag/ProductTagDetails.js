import React from 'react';
import {View, ScrollView, StyleSheet} from "react-native";
import {Card, ListItem, Text} from "react-native-elements";
import {connect} from "react-redux";

const ProductTagDetails = props => {
    const {translations} = props;
    const {
        dateTime,
        productTagHash,
        productTagActions,
        productTagProducer
    } = props.productTag;
    return (
            <View style={{flex: 1}}>
                <ScrollView contentContainerStyle={{ flexGrow: 1 }}>
                    <Text style={{fontWeight: 'bold'}} h4>
                        {translations.productTagDetails}
                    </Text>
                    <ListItem
                        title={new Date(dateTime).toLocaleDateString()}
                        titleStyle={styles.titleStyle}
                        subtitle={translations.date}
                        subtitleStyle={styles.subtitleStyle}
                        bottomDivider
                    />

                    <ListItem
                        title={productTagHash}
                        titleStyle={styles.titleStyle}
                        subtitle={translations.productTagHash}
                        subtitleStyle={styles.subtitleStyle}
                        bottomDivider
                    />

                    <Card containerStyle={{marginLeft: 0, marginRight: 0}} title={translations.productTagActions}>
                        {productTagActions.map((action, index) => (
                            <ListItem
                                key={index}
                                title={`${index + 1}. ${action.actionName}`}
                                titleStyle={styles.titleStyle}
                                style={{marginTop: -15, marginLeft: -15}}
                                // subtitle={"Producer Name"}
                                // subtitleStyle={styles.subtitleStyle}
                                // bottomDivider
                            />
                        ))}

                    </Card>

                    <Card containerStyle={{marginLeft: 0, marginRight: 0}} title={translations.productTagProducer}>
                        <ListItem
                            style={{marginTop: -15, marginLeft: -15}}
                            title={productTagProducer.producerName}
                            titleStyle={styles.titleStyle}
                            subtitle={translations.producerName}
                            subtitleStyle={styles.subtitleStyle}
                            bottomDivider
                        />
                        <ListItem
                            style={{marginLeft: -15}}
                            title={productTagProducer.licenceNumber}
                            titleStyle={styles.titleStyle}
                            subtitle={translations.licenceNumber}
                            subtitleStyle={styles.subtitleStyle}
                            bottomDivider
                        />
                        <ListItem
                            style={{marginLeft: -15, marginBottom: -15}}
                            title={productTagProducer.url}
                            titleStyle={styles.titleStyle}
                            subtitle={translations.url}
                            subtitleStyle={styles.subtitleStyle}
                        />
                    </Card>
                </ScrollView>
            </View>

    );
};

const mapStateToProps = state => {
    return {
        translations: state.languages.translations
    };
};


export default connect(mapStateToProps, null)(ProductTagDetails);

const styles = StyleSheet.create({
    titleStyle: {
        color: 'black',
        fontSize: 16,
        fontWeight: 'bold'
    },
    subtitleStyle: {
        color: 'silver',
        fontSize: 12,
    }
});
