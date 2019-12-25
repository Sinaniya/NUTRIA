import React, {Component} from 'react';
import axios from '../../axios';
import LinearProgress from "@material-ui/core/LinearProgress/LinearProgress";
import sortArray from '../../util/sortutil';
import ProductTag from '../../components/ProductTag';
import PTNotFoundPage from './PTNotFoundPage';
import * as actionCreators from "../../store/actions";
import connect from "react-redux/es/connect/connect";


class ResultPage extends Component {
    state = {
        fetchingProductTag: false,
        chainResult: [],
        productTagResult: null,
        error: null
    };

    componentDidMount() {
        this.setState({fetchingProductTag: true});
        const scannedHash = this.props.match.params.hash;


        // todo should continue here -> dispatch action
        axios.get(`/productTags/hash/${scannedHash}`)
            .then(result => {
                if (result.data.length > 0) {
                    const updatedResult = result.data.map(
                        productTag => {
                            if (productTag.productTagHash === scannedHash) {
                                this.setState({productTagResult: productTag});
                            }
                            return {
                                ...productTag,
                                date: new Date(productTag.date)
                            }
                        });

                    this.setState({chainResult: updatedResult.sort(sortArray('date'))});
                }
                // console.log(this.state.chainResult);
                this.setState({fetchingProductTag: false});
            })
            .catch(error => {
                console.error('error while fetching the product tag.');
                this.setState({error: error.message, fetchingProductTag: false});
            });
    }

    render() {

        const {error, productTagResult, fetchingProductTag} = this.state;

        let productTag = <LinearProgress color="secondary"/>;

        if (!fetchingProductTag) {
            if (productTagResult) {
                productTag = <ProductTag showMap={true}
                                         showHistoryButton={true}
                                         productTag={productTagResult}/>
            } else {
                productTag = <PTNotFoundPage/>
            }
        }

        return (
            <div>
                {!error ? productTag : error}
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

export default connect(null, mapDispatchToProps)(ResultPage);
