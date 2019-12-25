import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import {connect} from "react-redux";

const styles = theme => ({
    root: {
        width: '100%',
        maxWidth: 360,
        backgroundColor: theme.palette.background.paper,
        position: 'relative',
        overflow: 'auto',
        maxHeight: 300,
    },
    listSection: {
        backgroundColor: 'inherit',
    },
    ul: {
        backgroundColor: 'inherit',
        padding: 0,
    },
});

function actionList(props) {
    const {classes, actions} = props;

    return (
        <List className={classes.root} subheader={<li/>} style={{backgroundColor: "inherit"}}>
            <li key={`section`} className={classes.listSection}>
                <ul className={classes.ul}>
                    <ListSubheader style={{
                        fontSize: '1rem',
                        color: 'black'
                    }}>{props.translations.productTagActions}</ListSubheader>
                    {actions.map((action, key) => (
                        <ListItem key={`item-${action.actionId}`}>
                            <ListItemText primary={`  ${key + 1}. ${action.actionName}`}/>
                        </ListItem>
                    ))}
                </ul>
            </li>
        </List>
    );
}

actionList.propTypes = {
    classes: PropTypes.object.isRequired,
};

const mapStateToProps = state => {
    const {translations} = state.lang;
    return {
        translations: translations
    };
};

export default connect(mapStateToProps)(withStyles(styles)(actionList));
