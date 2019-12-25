import React from 'react';
import {withStyles} from '@material-ui/core/styles';
import {withRouter} from "react-router-dom";

import Typography from '@material-ui/core/Typography';
import {connect} from "react-redux";

const styles = theme => ({
    footer: {

        backgroundColor: "black",
        // marginTop: theme.spacing.unit,
        padding: `${theme.spacing.unit * 1}px 0`,
    },
});


const footer = (props) => {
    const {classes} = props;

    // don't do this Danijel...
    const footerStyle = props.location.pathname.includes('product-tags') ? {marginTop: '-40px'} : {};

    return (
        <footer className={classes.footer} style={footerStyle}>
            <Typography style={{fontSize: '1.2rem', color: "white"}} variant="title" align="center" gutterBottom>
                {props.translations.university}
            </Typography>
            <Typography style={{color: "white"}} variant="subheading" align="center" color="textSecondary"
                        component="p">
                {props.translations.contactUs}
            </Typography>
        </footer>
    );
};


const mapStateToProps = state => {
    const {translations} = state.lang;
    return {
        translations: translations
    };
};

export default withRouter(connect(mapStateToProps)(withStyles(styles)(footer)));
