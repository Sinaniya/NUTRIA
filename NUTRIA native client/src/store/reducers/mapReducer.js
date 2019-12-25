import {SET_PT_FOR_MAP_VIEW_ACTION} from "../actions/actionTypes";
import sortProductTags from "../../util/ptChainSortUtil";

const initialState = {
    hash: null,
    ptDetails: null,
    ptChain: []
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_PT_FOR_MAP_VIEW_ACTION:
            return {
                ...state,
                hash: action.productTag.hash,
                ptDetails: action.productTag.ptDetails,
                ptChain: sortProductTags(action.productTag.ptChain)
            };
        default:
            return state;
    }
};

export default reducer;