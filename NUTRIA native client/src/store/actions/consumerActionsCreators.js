// import {SET_PRODUCT_TAG_ACTION} from "./actionTypes";
//
//
// import {Alert} from "react-native";
//
// export const getProductTagData = (productTagHash) => {
//     return dispatch => {
//         fetch(`https://foodchain.ddns.net/api/v1/productTags/hash/${productTagHash}`, {
//             method: 'GET'
//         }).catch(error => {
//             Alert.alert(
//                 'Something went wrong.',
//                 productTagHash
//             );
//         })
//             .then(result => result.json())
//             .then(parsedResult => {
//                 dispatch(setProductTag(parsedResult))
//             });
//     };
// };
//
//
// export const setProductTag = productTag => {
//     return {
//         type: SET_PRODUCT_TAG_ACTION,
//         productTag
//     }
// };
