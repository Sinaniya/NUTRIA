import {Image, SafeAreaView, ScrollView, Text, View} from "react-native";
import {images} from "../../../assets/images";
import React from "react";
import {connect} from "react-redux";
import {ButtonGroup} from "react-native-elements";
import * as actionCreators from '../../store/actions/languageActionCreators';

const HomeScreenDrawer = (props) => (
    <SafeAreaView style={{flex: 1, backgroundColor: 'white'}}>
        <View style={{
            height: 170,
            backgroundColor: 'white',
            alignItems: 'center',
            justifyContent: 'center',
            marginTop: 20,
            marginBottom: 20
        }}>
            <Image source={images.drawerIcon}
                   style={{
                       height: 205,
                       width: 250
                   }}/>
        </View>
        <ScrollView>
            <View>
                <Text
                    style={{
                        flex: 1,
                        fontSize: 26,
                        fontWeight: 'bold',
                        marginLeft: 10
                    }}
                >
                    {props.languageTitle}
                </Text>
                <ButtonGroup
                    buttons={props.languages.map(language => (language))}
                    selectedIndex={props.currentLanguageIndex}
                    onPress={selectedIndex => props.onChangeLanguageClicked(selectedIndex)}
                    containerStyle={{marginBottom: 20}}
                />
            </View>
        </ScrollView>
    </SafeAreaView>
);

const mapStateToProps = state => {
    return {
        languages: state.languages.languages,
        currentLanguageIndex: state.languages.translations.languageIndex,
        languageTitle: state.languages.translations.language
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onChangeLanguageClicked: (index) => dispatch(
            actionCreators.changeLanguage(index))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(HomeScreenDrawer);