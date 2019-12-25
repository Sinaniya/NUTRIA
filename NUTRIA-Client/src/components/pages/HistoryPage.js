import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import Typography from '@material-ui/core/Typography';
// import Button from '@material-ui/core/Button';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import axios from "../../axios";
import sortArray from "../../util/sortutil";
import ProductTag from "../ProductTag";
import ChainMapViewModal from '../modals/ChainMapViewModal';


const styles = theme => ({
    root: {
        width: '100%',
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        flexBasis: '33.33%',
        flexShrink: 0,
    },
    secondaryHeading: {
        fontSize: theme.typography.pxToRem(15),
        color: theme.palette.text.secondary,
    },
    // button: {
    //     margin: theme.spacing.unit,
    //     width: '70%',
    //     marginLeft: '15%',
    //     marginRight: '15%'
    // }
});

class HistoryPage extends React.Component {
    state = {
        expanded: null,
        chainResult: []
    };
    handleChange = panel => (event, expanded) => {
        this.setState({
            expanded: expanded ? panel : false,
        });
    };

    // todo implement redux so to avoid this call
    componentDidMount() {
        this.setState({fetchingProductTag: true});
        const scannedHash = this.props.match.params.hash;

        axios.get(`/productTags/hash/${scannedHash}`)
            .then(result => {
                if (result.data.length > 0) {
                    const updatedResult = result.data.map(
                        productTag => {
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
        const {classes} = this.props;
        const {expanded, chainResult} = this.state;

        return (
            <div className={classes.root}>
                <ExpansionPanelSummary>
                    <Typography className={classes.heading}>
                        <strong>Date</strong>
                    </Typography>
                    <Typography className={classes.secondaryHeading}>
                        <strong>Producer</strong>
                    </Typography>
                </ExpansionPanelSummary>
                {chainResult.map(productTag => {
                    return (
                        <ExpansionPanel key={productTag.productTagId} expanded={expanded === productTag.productTagId}
                                        onChange={this.handleChange(productTag.productTagId)}>
                            <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>}>
                                <Typography className={classes.heading}>
                                    {productTag.date.toLocaleDateString()}
                                </Typography>
                                <Typography className={classes.secondaryHeading}>
                                    {productTag.productTagProducer.producerName}
                                </Typography>
                            </ExpansionPanelSummary>
                            <ExpansionPanelDetails>
                                <div style={{marginTop: "-50px"}}>
                                    <ProductTag productTag={productTag}/>
                                </div>
                            </ExpansionPanelDetails>
                        </ExpansionPanel>
                    );
                })}
                {/*<Button onClick={() => console.log("show map")} variant="contained" className={classes.button}>*/}
                {/*Show Map*/}
                {/*</Button>*/}
                <ChainMapViewModal productTags={chainResult}/>
            </div>
        );
    }
}

HistoryPage.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(HistoryPage);