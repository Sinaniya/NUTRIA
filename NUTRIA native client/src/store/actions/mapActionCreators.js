import {SET_PT_FOR_MAP_VIEW_ACTION} from "./actionTypes";

export const setPTForMapView = productTag => {
    return {
        type: SET_PT_FOR_MAP_VIEW_ACTION,
        productTag
    }
};