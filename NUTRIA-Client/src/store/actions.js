export const CHANGE_LANGUAGE_ACTION = 'CHANGE_LANGUAGE_ACTION';
export const SCAN_PRODUCT_TAG_ACTION = 'SCAN_PRODUCT_TAG_ACTION';



export const changeLanguage = (language) => {
    return {
        type: CHANGE_LANGUAGE_ACTION,
        payload: language
    }
};


// todo continue here -> make an api call to fetch a product tag data
export const setProductTag = () => {
}

export const fetchProductTag = (hash) => {
    return dispatch => {

    };
};
