import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import Modal from '@material-ui/core/Modal';
import Button from '@material-ui/core/Button';

import {Map, TileLayer, Marker, Popup, Polyline} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

import L from 'leaflet';
import ProductTag from "../ProductTag";

delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
    iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png')
});


const styles = theme => ({
    paper: {
        // margin: "5% 5%",
        position: 'relative',
        height: "100%",
        width: "100%",
        backgroundColor: theme.palette.background.paper,
        boxShadow: theme.shadows[5],
        // padding: theme.spacing.unit * 4,
    },
});

class ChainMapViewModal extends Component {
    state = {
        zoom: 13,
        open: false,
    };

    handleOpen = () => {
        this.setState({open: true});
    };

    handleClose = () => {
        this.setState({open: false});
    };


    closeModal = () => {
        this.state.closeModal();
    };

    render() {
        const {classes, productTags} = this.props;

        const polylines = [];

        const markers = productTags.map(productTag => {
            const markerPosition = [productTag.latitude, productTag.longitude];
            polylines.push(markerPosition);
            return <Marker
                key={productTag.productTagId}
                position={markerPosition}>
                <Popup minWidth={90}>
                    <ProductTag style={{height: '400px', width: '300px', overflowY: 'scroll'}}
                                showMap={false}
                                showHistoryButton={false}
                                productTag={productTag}/>
                </Popup>
            </Marker>
        });

        // calculate the average of the coordinates to center the map view
        let latitudeCenter = 0;
        let longitudeCenter = 0;

        const bound = markers.forEach(marker => {
            latitudeCenter += marker.props.position[0];
            longitudeCenter += marker.props.position[1];
            return [marker.props.position[0], marker.props.position[1]];
        });

        const boundCenter = [latitudeCenter / markers.length, longitudeCenter / markers.length];

        return (
            <div>
                <Button onClick={this.handleOpen}>Show Map</Button>
                <Modal
                    style={{}}
                    aria-labelledby="simple-modal-title"
                    aria-describedby="simple-modal-description"
                    open={this.state.open}
                    onClose={this.handleClose}
                >
                    <div className={classes.paper}>

                        <Map
                            bound={bound}
                            center={boundCenter}
                            zoom={7}
                            style={{height: '100%', width: '100%', margin: '0%'}}>
                            <TileLayer
                                attribution="&amp;copy <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
                                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                            />
                            {markers}
                            <Polyline positions={polylines}/>

                        </Map>

                        <Button variant="outlined" style={{
                            border: '2px solid grey',
                            backgroundColor: 'white',
                            float: 'right',
                            marginRight: '5%',
                            zIndex: '1000',
                            bottom: '70px'
                        }} onClick={this.handleClose}>Close Map</Button>
                    </div>
                </Modal>
            </div>
        );
    }
}

ChainMapViewModal.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ChainMapViewModal);