import React from 'react';
import InitApp from './InitApp';
import {Provider} from 'react-redux';
import configureStore from './src/store/configureStore';
import { ScreenOrientation } from 'expo';

const store = configureStore();

ScreenOrientation.allowAsync(ScreenOrientation.Orientation.PORTRAIT);

const App = () => (
    <Provider store={store}>
        <InitApp/>
    </Provider>
);

export default App;