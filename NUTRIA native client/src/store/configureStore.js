import thunk from "redux-thunk";
import {applyMiddleware, combineReducers, createStore, compose} from "redux";

import languageReducer from './reducers/languageReducer';
import consumerReducer from './reducers/consumerReducer';
import producerReducer from './reducers/producerReducer';
import productTagReducer from './reducers/productTagReducer';
import uiReducer from './reducers/uiReducer';
import mapReducer from "./reducers/mapReducer";



const rootReducer = combineReducers({
    languages: languageReducer,
    consumer: consumerReducer,
    producer: producerReducer,
    productTag: productTagReducer,
    ui: uiReducer,
    map: mapReducer
});

let composeEnhancers = compose;

if (__DEV__) {
    composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
}

const addLoggingToDispatch = store => {
    const rawDispatch = store.dispatch;

    if (!console.group) {
        return rawDispatch;
    }

    return action => {
        console.group("Return action from configure store: ", action.type);
        // console.log("%c prev state", "color: gray", store.getState());
        // console.log("%c action", "color: blue", action);
        const returnValue = rawDispatch(action);
        // console.log("%c next state", "color: green", store.getState());
        console.groupEnd(action.type);

        return returnValue;
    };
};

export default function configureStore() {
    const store = createStore(rootReducer, composeEnhancers(applyMiddleware(thunk)));
    store.dispatch = addLoggingToDispatch(store);
    return store;
}