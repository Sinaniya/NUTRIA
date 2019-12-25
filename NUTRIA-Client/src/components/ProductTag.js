import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import {withStyles} from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import Divider from '@material-ui/core/Divider';
import ActionList from "./ActionList";
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';

import MapView from "../components/maps/MapView";

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

class ProductTag extends Component {

    // todo attach reverse geo data to the prductTag
    // componentDidMount() {
    //     const {longitude, latitude} = this.props.productTag;
    //     axios.get(`https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${latitude}&lon=${longitude}`)
    //         .then(result => {
    //             console.log('result location', result.data);
    //         });
    // }

    render() {
        const {classes, showMap, showHistoryButton} = this.props;
        const {productTagHash, date, productTagProducer, longitude, latitude, productTagActions} = this.props.productTag;

        const convertedDate = new Date(date);

        const style = this.props.style;

        return (
            <div className="productTagDetails" style={style ? style : null}>
                {/*<form className={classes.container} noValidate autoComplete="off">*/}

                <Grid style={{
                    width: "90%",
                    marginLeft: "1px",
                    marginTop: "10px"
                }} container spacing={24} className="heading">
                    <Grid item xs={showHistoryButton ? 8 : 12}>
                            <p style={{
                                position: 'center',
                                fontSize: '1.2rem'
                            }}>
                                <strong>Product Tag Details</strong>
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
                        label="Date"
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
                            marginLeft: "8px"
                        }}>Product Tag Producer</p>

                        <TextField
                            variant={"filled"}
                            id="producerName"
                            label="Producer's Name"
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
                            label="Producer's URL"
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
                            label="Producer's Licence Number"
                            defaultValue={productTagProducer.licenceNumber}
                            className={classes.textField}
                            margin="normal"
                            InputProps={{
                                readOnly: true,
                            }}
                        />
                    </div>

                    <div><p style={{
                        width: "90%",
                        marginLeft: "8px"
                    }}>Geo Data</p>
                        <TextField
                            variant={"filled"}
                            id="longitude"
                            label="Longitude"
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
                            label="Latitude"
                            defaultValue={`${latitude}° E`}
                            className={classes.textField}
                            margin="dense"
                            InputProps={{
                                readOnly: true,
                            }}
                        />
                        {showMap ? (
                            <div style={{width: '100%', height: '400px'}}>
                                <MapView
                                    productTagResult={this.props.productTag}/>
                            </div>) : null}
                    </div>
            </div>
        );
    }
}

export default withStyles(styles)(ProductTag);
