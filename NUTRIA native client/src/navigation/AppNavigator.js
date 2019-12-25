import React from 'react';
import {createAppContainer, createStackNavigator, createSwitchNavigator} from 'react-navigation';
import ProducerAuthScreen from '../screens/producer/auth/ProducerAuthScreen';
import ProducerScreen from '../screens/producer/ProducerScreen';
import HomeScreen from '../screens/home/HomeScreen';
import ConsumerScreen from "../screens/consumer/ConsumerScreen";
import MapScreen from "../screens/map/MapScreen";
import SettingsScreen from "../screens/producer/SettingsScreen";
import AllProductsScreen from "../screens/producer/AllProductsScreen";

const ProducerStack = createStackNavigator({Home: ProducerScreen, ProducerSettings: SettingsScreen, AllProductsScreen: AllProductsScreen});
const ProducerAuthStack = createStackNavigator({SignIn: ProducerAuthScreen});
const HomeStack = createStackNavigator({Home: HomeScreen});
const ConsumerStack = createStackNavigator({Consumer: ConsumerScreen});
const MapStack = createStackNavigator({Map: MapScreen});

export default createAppContainer(createSwitchNavigator(
    {
        Producer: ProducerStack,
        Auth: ProducerAuthStack,
        Consumer: ConsumerStack,
        Home: HomeStack,
        Map: MapStack
    },
    {
        initialRouteName: 'Home',
    }
));

