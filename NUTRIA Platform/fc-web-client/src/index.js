import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import {applyMiddleware, combineReducers, compose, createStore} from 'redux';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';

import languageReducer from './store/reducers/languageReducer';
import productTagReducer from './store/reducers/productTagReducer';

const rootReducer = combineReducers({
    lang: languageReducer,
    pt: productTagReducer
});

const logger = store => {
    return next => {
        return action => {
            // console.log('[Middleware] Dispatching: ', action);
            const result = next(action);
            // console.log('[Middleware] next state: ', store.getState());
            return result;
        }
    }
};

window.__MUI_USE_NEXT_TYPOGRAPHY_VARIANTS__ = true;

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(rootReducer, composeEnhancers(applyMiddleware(logger, thunk)));


ReactDOM.render(<Provider store={store}><App/></Provider>, document.getElementById('root'));
registerServiceWorker();
