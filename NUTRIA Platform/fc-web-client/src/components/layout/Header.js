import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import {AppBar, Button, IconButton, Menu, MenuItem, Toolbar, Typography} from '@material-ui/core';
import MenuIcon from '@material-ui/icons/Menu';
import {connect} from 'react-redux';
import * as actionCreators from '../../store/actions';

const styles = (theme) => ({
    root: {
        flexGrow: 1,
        height: 70
    },
    grow: {
        flexGrow: 1,
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
    },
    button: {
        margin: theme.spacing.unit,
    }
});


class ButtonAppBar extends Component {


    state = {
        anchorEl: null,
    };

    menuItemsClickHandler = event => {
        this.setState({anchorEl: event.currentTarget});
    };

    menuItemsClickCloseHandler = () => {
        this.setState({anchorEl: null});
    };

    render() {
        const {anchorEl} = this.state;
        const {classes} = this.props;
        return (
            <div className={classes.root}>
                <AppBar position="static" style={{
                    backgroundColor: '#000000',
                    height: "100%"
                }}>
                    <Toolbar style={{
                        marginTop: '5px',
                        position: 'center'
                    }}>
                        <IconButton onClick={this.menuItemsClickHandler} className={classes.menuButton} color="inherit"
                                    aria-label="Menu">
                            <MenuIcon/>
                        </IconButton>


                        <Menu
                            // style={{backgroundColor: '#EAE9ED'}}
                            id="simple-menu"
                            anchorEl={anchorEl}
                            open={Boolean(anchorEl)}
                            onClose={this.menuItemsClickCloseHandler}
                        >
                            {/*<MenuItem>Language</MenuItem>*/}

                            <MenuItem style={{
                                display: 'block',
                                height: '100%'
                            }}>
                                {this.props.translations.language}: <br/>
                                <nav>
                                    <ul style={{paddingInlineStart: '0'}}>
                                        {this.props.languages.map(language => {
                                            return <li className="language-item"
                                                       onClick={(() => this.props.onChangeLanguageClicked(language))}
                                                       style={
                                                           language === this.props.currentLanguage ?
                                                               {
                                                                   display: 'inline-block',
                                                                   marginRight: '10px',
                                                                   color: 'blue'
                                                               } :
                                                               {
                                                                   display: 'inline-block',
                                                                   marginRight: '10px',
                                                               }

                                                       } key={language}>{language}</li>

                                        })}
                                    </ul>
                                </nav>
                            </MenuItem>
                            <MenuItem
                                onClick={this.menuItemsClickCloseHandler}>{this.props.translations.aboutUs}</MenuItem>
                            <MenuItem
                                onClick={this.menuItemsClickCloseHandler}>{this.props.translations.contactUs}</MenuItem>
                        </Menu>


                        <Typography variant="title" color="inherit" className={classes.grow}>
                            <Link to="/" style={{color: "inherit", textDecoration: "none"}}>FoodChain</Link>
                        </Typography>

                        {(this.props.location.pathname === "/" ||
                            this.props.location.pathname === "/scan") ? "" :
                            <Button style={{
                                width: '20%'
                            }} variant="flat" color="inherit">
                                <Link to="/scan" style={{all: "unset"}}>
                                    {this.props.translations.scanButton}
                                </Link>
                            </Button>}
                    </Toolbar>
                </AppBar>
            </div>
        );
    }
}

ButtonAppBar.propTypes = {
    classes: PropTypes.object.isRequired,
};

const mapStateToProps = state => {
    const {languages, defaultLanguage, currentLanguage, translations} = state.lang;
    return {
        languages: languages,
        defaultLanguage: defaultLanguage,
        currentLanguage: currentLanguage,
        translations: translations
    };
};


const mapDispatchToProps = dispatch => {
    return {
        onChangeLanguageClicked: (language) => dispatch(
            actionCreators.changeLanguage(language))
    };
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(withStyles(styles)(ButtonAppBar)));
