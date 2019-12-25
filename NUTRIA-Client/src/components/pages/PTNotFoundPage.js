import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import {Link} from "react-router-dom";

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
    }
});


const pTNotFoundPage = (props) => {

    const {classes} = props;

    return (
        <div>
            <h1>There is no product tag for the given QR code.</h1>
            <Button variant="contained" className={classes.button}>
                <Link to="/" style={{all: "unset"}}>
                    Homepage
                </Link>
            </Button>
            <Button variant="contained" className={classes.button}>
                <Link to="/scan" style={{all: "unset"}}>
                    Scan Again
                </Link>
            </Button>
        </div>
    );
};


pTNotFoundPage.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(pTNotFoundPage);

