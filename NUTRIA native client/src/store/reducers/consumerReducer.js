import {PT_ALREADY_SCANNED_ACTION, SET_CONSUMER_SCANNED_PT_ACTION} from "../actions/actionTypes";

import getUpdatedPTToAdd from "../../util/ptUpdateUtil";
import checkIfAlreadyScanned from "../../util/ptCheckIfScannedUtil";

const initialState = {
    scannedProductTags: [],
    scannedProductTagAlreadyScanned: false,
    currentScannedConsumerProductTag: null
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_CONSUMER_SCANNED_PT_ACTION:
            const updatedProductTag = getUpdatedPTToAdd(action.scannedProductTag);
            const alreadyScanned = checkIfAlreadyScanned(state.scannedProductTags, updatedProductTag);
            return alreadyScanned ? {
                ...state,
                scannedProductTagAlreadyScanned: true
            } : {
                ...state,
                scannedProductTags: [...state.scannedProductTags, updatedProductTag],
                scannedProductTagAlreadyScanned: false,
                currentScannedConsumerProductTag: updatedProductTag
            };
        case PT_ALREADY_SCANNED_ACTION:
            return {
                ...state,
                scannedProductTagAlreadyScanned: action.scanned
            };
        default:
            return state;
    }
};

export default reducer;