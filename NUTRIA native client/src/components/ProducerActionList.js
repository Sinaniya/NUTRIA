import React from 'react';
import {ScrollView, StyleSheet, View, Alert} from "react-native";
import {Button, Input, ListItem} from "react-native-elements";

import Layout from '../constants/Layout'
import {connect} from "react-redux";

const ProducerActionList = props => {
    const {translations} = props;
    return (
        <View>
            <ScrollView
                style={{height: Layout.window.height * props.heightPercent}} >
                {props.actions.map(action => (
                    <ListItem title={action.actionName}
                              key={Math.random()} switch={{
                        value: props.selectedActions.some(act => act.actionName === action.actionName),
                        onValueChange: value => props.onActionToggleChange(value, action)
                    }} bottomDivider/>
                ))}
            </ScrollView>

            {props.showNewActionInput ? <Input
                style={{width: '100%'}}
                containerStyle={[styles.inputContainerStyle]}
                placeholder={translations.enterNewAction}
                value={props.newActionValue}
                onChangeText={newAction => props.onNewActionChangeText(newAction)}
                // ref={ref => (this.shakeInput2 = ref)}
                rightIcon={
                    <Button
                        title={translations.add}
                        onPress={() => {
                            if (props.newActionValue.length < 3) {
                                Alert.alert(
                                    translations.error,
                                    translations.valueNotValid
                                )
                            } else {
                                props.onAddNewAction();
                                Alert.alert(
                                    translations.success,
                                    `${translations.actionCreated} ${props.newActionValue}`
                                )
                            }
                        }}
                    />
                }
                // errorMessage="Shake me on error !"
            />: null}
        </View>
    );
};

const mapStateToProps = state => {
    return {
        translations: state.languages.translations
    }
};

export default connect(mapStateToProps, null)(ProducerActionList);

const styles = StyleSheet.create({
    inputContainerStyle: {
        marginTop: 16,
        width: '100%',
    }
});