import * as actionTypes from '../actions';

const initialState = {
    scannedProductTag: null,
    productTagChain: []
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SCAN_PRODUCT_TAG_ACTION:
            return {};
        default:
            return state;
    }
};

export default reducer;