import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ProductTagDetails from './PTDetails';
import Aux from '../hoc/Aux';


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
    }
});

class PTTableView extends React.Component {
    state = {
        expanded: null,
        chainResult: []
    };
    handleChange = panel => (event, expanded) => {
        this.setState({
            expanded: expanded ? panel : false,
        });
    };

    render() {
        const {classes, chainResult} = this.props;
        const {expanded} = this.state;

        return (
            <Aux>
                <p style={{margin: '20px 0px 0px 10px', fontSize: '1.3rem'}}><strong>Tabular View</strong></p>
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
                                    {new Date(productTag.dateTime).toDateString()}
                                </Typography>
                                <Typography className={classes.secondaryHeading}>
                                    {productTag.productTagProducer.producerName}
                                </Typography>
                            </ExpansionPanelSummary>
                            <ExpansionPanelDetails>
                                <div style={{marginTop: "-50px"}}>
                                    <ProductTagDetails productTag={productTag}/>
                                </div>
                            </ExpansionPanelDetails>
                        </ExpansionPanel>
                    );
                })}
            </Aux>
        );
    }
}

PTTableView.propTypes = {
    classes: PropTypes.object.isRequired,
    chainResult: PropTypes.array.isRequired,
};

export default withStyles(styles)(PTTableView);