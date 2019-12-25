import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import {withStyles} from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import Divider from '@material-ui/core/Divider';
import ActionList from "./ActionList";
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import {connect} from "react-redux";

// import MapView from "../components/maps/MapView";

const styles = theme => ({
    container: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    textField: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,
        width: "90%"
    },
    dense: {
        marginTop: 19,
    },
});

class PTDetails extends Component {

    render() {
        const {classes, showHistoryButton} = this.props;
        const {productTagHash, dateTime, productTagProducer, longitude, latitude, productTagActions} = this.props.productTag;

        const convertedDate = new Date(dateTime);

        const style = this.props.style;

        return (
            <div className="productTagDetails" style={style ? style : null}>

                <Grid style={{
                    width: "90%",
                    marginLeft: "1px",
                    marginTop: "10px"
                }} container spacing={24} className="heading">
                    <Grid item xs={showHistoryButton ? 8 : 12}>
                            <p style={{
                                position: 'center',
                                fontSize: '1.3rem',
                                marginLeft: '5px'
                            }}>
                                <strong>{this.props.translations.productTagDetails}</strong>
                            </p>
                        </Grid>
                        {showHistoryButton ? (
                            <Grid item xs={4}>
                                <Link to={`/product-tags/history/${productTagHash}`}
                                      style={{color: "inherit", textDecoration: "none"}}>
                                    <Button style={{marginRight: '5px'}} variant="contained" size="large"
                                            color="inherit"
                                            className={classes.button}>
                                        Show Product History
                                    </Button>
                                </Link>
                            </Grid>
                        ) : null}
                    </Grid>

                    <TextField
                        variant={"filled"}
                        id="productTagHash"
                        label="Hash"
                        defaultValue={productTagHash}
                        className={classes.textField}
                        margin="normal"
                        InputProps={{
                            readOnly: true,
                        }}
                    />

                    <TextField
                        variant={"filled"}
                        id="date"
                        label={this.props.translations.date}
                        defaultValue={convertedDate.toLocaleDateString() + " " + convertedDate.toLocaleTimeString()}
                        className={classes.textField}
                        margin="normal"
                        InputProps={{
                            readOnly: true,
                        }}
                    />

                <ActionList actions={productTagActions}/>

                <Divider/>

                <div>
                        <p style={{
                            width: "90%",
                            marginLeft: "15px",
                            fontSize: '1rem',
                            color: 'black',
                            fontWeight: 'bold',
                            marginBottom: 0
                        }}>{this.props.translations.ptProducer}</p>

                        <TextField
                            variant={"filled"}
                            id="producerName"
                            label={this.props.translations.producerName}
                            defaultValue={productTagProducer.producerName}
                            className={classes.textField}
                            margin="normal"
                            InputProps={{
                                readOnly: true,
                            }}
                        />

                        <TextField
                            variant={"filled"}
                            id="producerUrl"
                            label={this.props.translations.producerUrl}
                            defaultValue={productTagProducer.url}
                            className={classes.textField}
                            margin="normal"
                            InputProps={{
                                readOnly: true,
                            }}
                        />

                        <TextField
                            variant={"filled"}
                            id="producerLicenceNumber"
                            label={this.props.translations.producerLicenceNumber}
                            defaultValue={productTagProducer.licenceNumber}
                            className={classes.textField}
                            margin="normal"
                            InputProps={{
                                readOnly: true,
                            }}
                        />
                    </div>

                <div>
                    <p style={{
                        width: "90%",
                        marginLeft: "15px",
                        fontSize: '1rem',
                        color: 'black',
                        fontWeight: 'bold',
                        marginBottom: 0
                    }}>{this.props.translations.geoData}</p>
                        <TextField
                            variant={"filled"}
                            id="longitude"
                            label={this.props.translations.longitude}
                            defaultValue={`${longitude}° N`}
                            className={classes.textField}
                            margin="dense"
                            InputProps={{
                                readOnly: true,
                            }}
                        />

                        <TextField
                            variant={"filled"}
                            id="latitude"
                            label={this.props.translations.latitude}
                            defaultValue={`${latitude}° E`}
                            className={classes.textField}
                            margin="dense"
                            InputProps={{
                                readOnly: true,
                            }}
                        />
                    </div>
            </div>
        );
    }
}


const mapStateToProps = state => {
    const {translations} = state.lang;
    return {
        translations: translations
    };
};


export default connect(mapStateToProps)(withStyles(styles)(PTDetails));
