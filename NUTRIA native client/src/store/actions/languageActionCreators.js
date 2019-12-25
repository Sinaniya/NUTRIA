import {CHANGE_LANGUAGE_ACTION} from "./actionTypes";


export const changeLanguage = (index) => {
    return {
        type: CHANGE_LANGUAGE_ACTION,
        payload: index
    }
};