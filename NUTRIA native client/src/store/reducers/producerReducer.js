import {
    SET_SIGN_UP_FORM_INIT_DATA_ACTION,
    AUTH_SET_JWT_TOKEN_ACTION,
    SET_PRODUCER_DATA_ACTION,
    OPEN_ALERT_ON_SCAN_ACTION,
    CLOSE_ALERT_ON_SCAN_ACTION,
    SET_PRODUCER_SCANNED_PT_ACTION,
    ON_CREATE_PRODUCT_TAG_SUCCESS_ACTION,
    ON_CREATE_PRODUCT_TAG_ERROR_ACTION,
    SET_PRODUCERS_PRODUCT_TAGS_ACTION,
    SIGN_OUT_ACTION,
    REMOVE_SCANNED_PT_ACTION
} from "../actions/actionTypes";

import getUpdatedPTToAdd from "../../util/ptUpdateUtil";
import checkIfAlreadyScanned from "../../util/ptCheckIfScannedUtil";


const initialState = {
    generatedProductTag: null,
    numberOfGeneratedProductTags: 0,
    productTagSuccessfullyGenerated: false,
    errorResponseOnGenerateProductTag: null,
    scannedProductTags: [],
    scannedProductTagAlreadyScanned: false,
    isAlertOnScanOpen: false,
    actions: [],
    activeProducer: null,
    activeProducerId: null,
    jwtToken: null,
    signUpFormInitData: {
        actions: [],
        certificates: []
    },
    allProducersProductTags: []
};


const reducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_SIGN_UP_FORM_INIT_DATA_ACTION :
            return {
                ...state,
                signUpFormInitData: {
                    actions: action.signUpFormInitData.actions,
                    certificates: action.signUpFormInitData.certificates
                }
            };
        case AUTH_SET_JWT_TOKEN_ACTION :
            return {
                ...state,
                jwtToken: action.token,
                activeProducerId: action.producerId
            };
        case SET_PRODUCER_DATA_ACTION: {
            return {
                ...state,
                activeProducer: action.producerData,
                // newProductTag: {
                //     ...state.newProductTag,
                //     productTagActions: action.producerData.producerActions
                // }
            }
        }
        case OPEN_ALERT_ON_SCAN_ACTION: {
            return {
                ...state,
                isAlertOnScanOpen: true
            }
        }
        case CLOSE_ALERT_ON_SCAN_ACTION: {
            return {
                ...state,
                isAlertOnScanOpen: false
            }
        }
        case SET_PRODUCER_SCANNED_PT_ACTION: {
            const updatedProductTag = getUpdatedPTToAdd(action.scannedProductTag);
            const alreadyScanned = checkIfAlreadyScanned(state.scannedProductTags, updatedProductTag);
            return alreadyScanned ? {
                ...state,
                scannedProductTagAlreadyScanned: true
            } : {
                ...state,
                scannedProductTags: [...state.scannedProductTags, updatedProductTag],
                scannedProductTagAlreadyScanned: false
            }
        }
        case ON_CREATE_PRODUCT_TAG_SUCCESS_ACTION: {
            console.log('success: ', action.productTag);
            return {
                ...state,
                generatedProductTag: action.productTag,
                productTagSuccessfullyGenerated: true,
                numberOfGeneratedProductTags: state.numberOfGeneratedProductTags + 1,
                scannedProductTags: []
            }
        }
        case ON_CREATE_PRODUCT_TAG_ERROR_ACTION: {
            console.log("error: ", action.response);
            return {
                ...state,
                errorResponseOnGenerateProductTag: action.response,
                productTagSuccessfullyGenerated: false,
                numberOfGeneratedProductTags: state.numberOfGeneratedProductTags + 1
            }
        }
        case SET_PRODUCERS_PRODUCT_TAGS_ACTION: {
            return {
                ...state,
                allProducersProductTags: action.productTags.map(productTag => getUpdatedPTToAdd(productTag))
            }
        }
        case SIGN_OUT_ACTION: {
            return {
                ...state,
                generatedProductTag: null,
                numberOfGeneratedProductTags: 0,
                productTagSuccessfullyGenerated: false,
                errorResponseOnGenerateProductTag: null,
                scannedProductTags: [],
                scannedProductTagAlreadyScanned: false,
                isAlertOnScanOpen: false,
                actions: [],
                activeProducer: null,
                activeProducerId: null,
                jwtToken: null,
                allProducersProductTags: []
            }
        }
        case REMOVE_SCANNED_PT_ACTION: {
            return {
                ...state,
                scannedProductTags: state.scannedProductTags.filter(pt => pt.hash !== action.hash)
            }
        }
        default:
            return state;
    }
};

export default reducer;