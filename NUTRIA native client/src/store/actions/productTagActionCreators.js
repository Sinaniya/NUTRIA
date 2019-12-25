import {
    SET_FETCHED_PT_ACTION,
    PREPARE_STATE_FOR_QR_SCANNER_SCREEN_ACTION,
    PT_DOES_NOT_EXIST_ACTION,
    SET_PRODUCER_SCANNED_PT_ACTION,
    SCANNED_PT_VALID_ACTION,
    SET_CONSUMER_SCANNED_PT_ACTION,
    PT_ALREADY_SCANNED_ACTION
} from "./actionTypes";

import Common from '../../constants/Common'

// import {closePtMapViewModal, openQrScanner} from "./uiActionCreators";


export const fetchPTByHash = (hash, mode) => {
    return dispatch => {
        fetch(`${Common.BACKEND_BASE_URL}/api/v2/productTags/hash/${hash}`)
            .catch(error => {
                alert("Error while fetching product tag data from the server.");
                console.error(error);
            })
            .then(response => response.json())
            .then(ptChain => {
                if (ptChain.length === 0) {
                    dispatch(scannedProductTagValid(false));
                    dispatch(productTagAlreadyScanned(false));
                } else {
                    dispatch(scannedProductTagValid(true));
                    dispatch(setScannedPT(hash, ptChain, mode))
                }
            })
    }
};

export const setScannedPT = (hash, ptChain, mode) => {
    const action = (mode === Common.mode.CONSUMER) ?
        SET_CONSUMER_SCANNED_PT_ACTION :
        SET_PRODUCER_SCANNED_PT_ACTION;
    return {
        type: action,
        scannedProductTag: {
            hash,
            ptChain
        }
    }
};

export const scannedProductTagValid = valid => {
    return {
        type: SCANNED_PT_VALID_ACTION,
        valid
    }
};


/*export const onScanAgainPressed = () => {
    return dispatch => {
        dispatch(prepareStateForQrCodeScannerScreen());
        dispatch(openQrScanner())
    }
};*/

export const setFetchedPTChain = (scannedHash, ptChain) => {
    return {
        type: SET_FETCHED_PT_ACTION,
        ptChain,
        scannedHash
    }
};

export const setPTDoesNotExist = () => {
    return {
        type: PT_DOES_NOT_EXIST_ACTION
    }
};


// export const prepareStateForQrCodeScannerScreen = () => {
//     return dispatch => {
//         dispatch(closePtMapViewModal());
//         dispatch(initializeScannedPtState());
//     };
// };

export const initializeScannedPtState = () => {
    return {
        type: PREPARE_STATE_FOR_QR_SCANNER_SCREEN_ACTION
    }
};

export const productTagAlreadyScanned = scanned => {
    return {
        type: PT_ALREADY_SCANNED_ACTION,
        scanned
    }
};