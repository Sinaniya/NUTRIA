import React, {Component} from 'react';
import axios from '../../axios';
import LinearProgress from "@material-ui/core/LinearProgress/LinearProgress";
import sortArray from '../../util/sortutil';
import PTNotFoundPage from './PTNotFoundPage';
import * as actionCreators from "../../store/actions";
import connect from "react-redux/es/connect/connect";

import PTViewWrapper from '../fragments/PTViewWrapper';


class PTResultPage extends Component {
    state = {
        fetchingProductTag: false,
        // chainResult: [],
        productTagResult: null,
        error: null
    };

    componentDidMount() {
        this.setState({fetchingProductTag: true});
        const scannedHash = this.props.match.params.hash;


        axios.get(`/productTags/hash/${scannedHash}`)
            .then(result => {
                if (result.data) {
                    // const updatedResult = result.data.map(
                    //     productTag => {
                    //         if (productTag.productTagHash === scannedHash) {
                    //             this.setState({productTagResult: productTag});
                    //         }
                    //         return {
                    //             ...productTag,
                    //             date: new Date(productTag.date)
                    //         }
                    //     });
                    //
                    // this.setState({chainResult: updatedResult.sort(sortArray('date'))});

                    // const chainResult = this.getChainResult(result.data, []);
                    //
                    // const updatedChainResult = chainResult.map(productTag => {
                    //         if (productTag.productTagHash === scannedHash) {
                    //             this.setState({productTagResult: productTag});
                    //         }
                    //         return {
                    //             ...productTag,
                    //             date: new Date(productTag.date)
                    //         }
                    //     });
                    //
                    // this.setState({chainResult: updatedChainResult.sort(sortArray('date'))});

                    this.setState({productTagResult: result.data});
                    this.setState({fetchingProductTag: false});

                    // console.log(result.data);
                    // console.log("chain result", this.getChainResult(result.data, []));
                    // this.setState({chainResult: this.getChainResult(result.data, [])})
                } else {
                    this.setState({fetchingProductTag: false});
                }

            })
            .catch(error => {
                console.error('error while fetching the product tag.');
                this.setState({error: error.message, fetchingProductTag: false});
            });
    }

    // // todo improve this
    // getChainResult = (foundProductTag, chainResult) => {
    //     console.log(foundProductTag);
    //     if (!foundProductTag) return;
    //     if (foundProductTag.productTagHash === "0") return;
    //     if (chainResult.filter(pt => pt.productTagId === foundProductTag.productTagId).length === 0) {
    //         chainResult.push(foundProductTag);
    //         foundProductTag.previousProductTags.forEach(productTag => {
    //             this.getChainResult(productTag, chainResult);
    //         });
    //     }
    //     return chainResult;
    // };

    render() {

        const {error, productTagResult, fetchingProductTag} = this.state;

        let result = <LinearProgress color="secondary"/>;

        if (!fetchingProductTag) {
            if (productTagResult && productTagResult.productTagHash) {
                result =
                    (<div>
                        <PTViewWrapper productTagResult={productTagResult}/>
                    </div>);
            } else {
                result = <PTNotFoundPage/>
            }
        }

        return (
            <div>
                {!error ? result : error}
            </div>
        );
    }
}

const mapDispatchToProps = dispatch => {
    return {
        onChangeLanguageClicked: (hash) => dispatch(
            actionCreators.fetchProductTag(hash))
    };
};

export default connect(null, mapDispatchToProps)(PTResultPage);
