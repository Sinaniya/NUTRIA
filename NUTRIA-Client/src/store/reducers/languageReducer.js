import * as actionTypes from '../actions';

const initialState = {
    languages: ['EN', 'DE', 'FR', 'IT'],
    defaultLanguage: 'EN',
    currentLanguage: 'EN'
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.CHANGE_LANGUAGE_ACTION:
            return {
                ...state,
                currentLanguage: action.payload
            };
        default:
            return state;
    }
};

export default reducer;