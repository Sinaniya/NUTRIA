import {
    AUTH_SET_JWT_TOKEN_ACTION,
    LOG_IN_ACTION,
    SIGN_OUT_ACTION,
    ON_CREATE_PRODUCT_TAG_ERROR_ACTION,
    ON_CREATE_PRODUCT_TAG_SUCCESS_ACTION,
    SET_PRODUCER_DATA_ACTION,
    SET_PRODUCERS_PRODUCT_TAGS_ACTION,
    SET_SIGN_UP_FORM_INIT_DATA_ACTION,
    SIGN_UP_ACTION,
    REMOVE_SCANNED_PT_ACTION
} from "./actionTypes";

import Common from '../../constants/Common';

import {uiStartLoading, uiStopLoading} from "./uiActionCreators";

export const fetchSignUpFormData = () => {
    return dispatch => {
        fetch(`${Common.BACKEND_BASE_URL}/api/v2/producers/registerData`)
            .catch(error => {
                alert("Error while fetching form data from the server.");
                console.error(error);
            })
            .then(response => response.json())
            .then(jsonResult => {


                const actions = jsonResult.actions;
                const certificates = jsonResult.certificates;

                const signUpFormInitData = {
                    actions,
                    certificates
                };
                dispatch(setInitSignUpFormData(signUpFormInitData));
            })
    }
};

export const tryAuth = (authData, authMode, thisRef) => {
    return dispatch => {
        switch (authMode) {
            case LOG_IN_ACTION :
                dispatch(authLogin(authData, thisRef));
                break;
            case SIGN_UP_ACTION :
                dispatch(authSignUp(authData, thisRef));
                break;
        }
    };

};

export const authLogin = (authData, thisRef) => {
    const {username, password} = authData;
    const formData = new FormData();
    formData.append("username", username);
    formData.append("password", password);
    return dispatch => {
        dispatch(uiStartLoading());
        fetch(`${Common.BACKEND_BASE_URL}/api/v1/producers/login`, {
            method: 'POST',
            body: formData,
            headers: {
                "Content-Type": "multipart/form-data"
            }
        })
            .catch(error => {
                console.error("Error: ", error);
                alert("Layout.window.heightcation failed, please try again!");
                dispatch(uiStopLoading())
            })
            .then(result => result.json())
            .then(jsonResult => {
                dispatch(uiStopLoading());
                if (!jsonResult.token || !jsonResult.resourceId) {
                    alert("Authentication failed. Please try again.");
                } else {
                    dispatch(authSetJwtToken(jsonResult.token, jsonResult.resourceId));
                    thisRef.props.navigation.navigate('Producer')
                }
            });
    };
};

export const authSignUp = (authData, thisRef) => {

    return dispatch => {
        dispatch(uiStartLoading());
        fetch(`${Common.BACKEND_BASE_URL}/api/v1/producers/register`, {
            method: 'POST',
            body: JSON.stringify(authData),
            headers: {
                "Content-Type": "application/json"
            }
        })
            .catch(error => {
                console.error("Error: ", error);
                alert("Authentication failed, please try again!");
                dispatch(uiStopLoading())
            })
            .then(result => result.json())
            .then(jsonResult => {
                dispatch(uiStopLoading());
                if (!jsonResult.token || !jsonResult.resourceId) {
                    alert("Authentication failed. Please try again.");
                } else {
                    dispatch(authSetJwtToken(jsonResult.token, jsonResult.resourceId));
                    thisRef.props.navigation.navigate('Producer')
                }
            });
    };
};

export const fetchProducerData = (token, producerId) => {
    return dispatch => {
        fetch(`${Common.BACKEND_BASE_URL}/api/v1/producers/${producerId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .catch(error => {
                alert("Error while fetching producer's data from the server.");
                console.error(error);
            })
            .then(response => {
                return response.json()
            })
            .then(jsonResult => {
                dispatch(setProducerData(jsonResult));
            })
    }
};

// todo Continue here, now we have a token. the next step is to create a pt
export const authGetJwtToken = () => {
    return (dispatch, getState) => {
        const promise = new Promise((resolve, reject) => {
            const token = getState().producer.jwtToken;
            if (!token) {
                reject();
            } else {
                resolve(token);
            }
        });
    }
};


export const setInitSignUpFormData = signUpFormInitData => {
    return {
        type: SET_SIGN_UP_FORM_INIT_DATA_ACTION,
        signUpFormInitData
    }
};

export const setProducerData = producerData => {
    return {
        type: SET_PRODUCER_DATA_ACTION,
        producerData
    }
};

export const authSetJwtToken = (token, producerId) => {
    return {
        type: AUTH_SET_JWT_TOKEN_ACTION,
        token,
        producerId
    }
};


export const generateNewProductTag = (token, newProductTagData) => {
    return dispatch => {
        dispatch(uiStartLoading());
        fetch(`${Common.BACKEND_BASE_URL}/api/v2/productTags`, {
            method: 'POST',
            body: JSON.stringify(newProductTagData),
            headers: {
                "Content-Type": "application/json",
                'Authorization': `Bearer ${token}`
            }
        })
            .catch(error => {
                console.error("Error: ", error);
                Alert.alert("Error occurred while creating a new Product Tag, please try again!");
                dispatch(uiStopLoading());
            })
            .then(result => result.json())
            .then(jsonResult => {
                if (jsonResult.error || jsonResult.errors) {
                    dispatch(onCreateProductTagError(jsonResult));
                } else {
                    dispatch(uiStopLoading());
                    dispatch(onSuccessPTGeneration(jsonResult));
                }
            });
    };
};

export const onCreateProductTagError = response => {
    return {
        type: ON_CREATE_PRODUCT_TAG_ERROR_ACTION,
        response
    }
};


export const onSuccessPTGeneration = productTag => {
    return {
        type: ON_CREATE_PRODUCT_TAG_SUCCESS_ACTION,
        productTag
    }
};


export const onProducersAllPTScreenOpen = (token, producerId) => {
    return dispatch => {
        fetch(`${Common.BACKEND_BASE_URL}/api/v2/productTags/producer/${producerId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .catch(error => {
                alert("Error while fetching producer's product tags.");
                console.error(error);
            })
            .then(response => {
                return response.json()
            })
            .then(jsonResult => {
                dispatch(setProducerProductTags(jsonResult));
            })
    }
};


export const setProducerProductTags = productTags => {
    return {
        type: SET_PRODUCERS_PRODUCT_TAGS_ACTION,
        productTags
    }
};

export const producerSignOut = () => {
    return {
        type: SIGN_OUT_ACTION
    }
};


export const updateProducer = (token, producerId, producerData) => {
    return dispatch => {
        dispatch(uiStartLoading());
        fetch(`${Common.BACKEND_BASE_URL}/api/v1/producers/${producerId}`, {
            method: 'PUT',
            body: JSON.stringify(producerData),
            headers: {
                "Content-Type": "application/json",
                'Authorization': `Bearer ${token}`
            }
        })
            .catch(error => {
                console.error("Error: ", error);
                alert("Error while updating producer, please try again!");
                dispatch(uiStopLoading())
            })
            .then(result => result.json())
            .then(jsonResult => {
                console.log('after update result: ', jsonResult);
                dispatch(uiStopLoading());
                if (jsonResult.httpStatus !== 200) {
                    alert("Producer update failed, please try again!");
                } else {
                    console.log('update sucessfull, result: ', jsonResult);
                    dispatch(fetchProducerData(token, producerId))
                }
            });
    };
};

export const removeScannedPt = hash => {
    return {
        type: REMOVE_SCANNED_PT_ACTION,
        hash
    }
};