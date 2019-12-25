import React from 'react';
import {withStyles} from '@material-ui/core/styles';

import Typography from '@material-ui/core/Typography';

const styles = theme => ({
    footer: {
        backgroundColor: theme.palette.background.dark,
        marginTop: theme.spacing.unit,
        padding: `${theme.spacing.unit * 1}px 0`,
    },
});


const footer = (props) => {
    const {classes} = props;
    return (
        <footer className={classes.footer}>
            <Typography variant="title" align="center" gutterBottom>
                Footer
            </Typography>
            <Typography variant="subheading" align="center" color="textSecondary" component="p">
                Footer content
            </Typography>
        </footer>
    );
};

export default withStyles(styles)(footer);
