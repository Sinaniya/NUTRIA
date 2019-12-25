import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import Modal from '@material-ui/core/Modal';
import Button from '@material-ui/core/Button';

import {Map, Marker, Polyline, Popup, TileLayer} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

import L from 'leaflet';
import ProductTag from "./PTDetails";
import {connect} from "react-redux";

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

class PTMapView extends Component {
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


    createMarkersAndPolylines = (productTagResult) => {
        return this.traversePTgs(productTagResult, {
            markers: [],
            polylines: []
        }, []);
    };


    // getChainResult = (foundProductTag, chainResult) => {
    //     if (foundProductTag.productTagHash === "0") {
    //         return;
    //     }
    //     if (chainResult.filter(pt => pt.productTagId === foundProductTag.productTagId).length === 0) {
    //         chainResult.push(foundProductTag);
    //         foundProductTag.previousProductTags.forEach(productTag => {
    //             this.getChainResult(productTag, chainResult);
    //         });
    //     }
    //     return chainResult;
    // };



    traversePTgs = (productTagResult, markersPolylines, chainResult) => {
        // console.warn("TRAVERSING: pt:", productTagResult, "markers:", markersPolylines, "chain: ", chainResult);
        if (productTagResult.productTagHash === "0" || productTagResult.productTagHash === null) {
            return;
        }
        if (chainResult.filter(pt => pt.productTagId === productTagResult.productTagId).length === 0) {
            chainResult.push(productTagResult);
            const markerPolyline = this.createMarkerAndPolyline(productTagResult);
            markersPolylines.markers.push(markerPolyline.marker);
            markersPolylines.polylines.push(markerPolyline.polyline);
            productTagResult.previousProductTags.forEach(productTag => {
                this.traversePTgs(productTag, markersPolylines, chainResult);
            });
        }
        return markersPolylines;
    };


    createMarkerAndPolyline = (productTag) => {
        const markerPosition = [productTag.latitude, productTag.longitude];
        const markerPolyline = {
            polyline: markerPosition,
            marker: <Marker
                key={productTag.productTagId}
                position={markerPosition}>
                <Popup minWidth={90}>
                    <ProductTag style={{height: '400px', width: '300px', overflowY: 'scroll'}}
                                showMap={false}
                                showHistoryButton={false}
                                productTag={productTag}/>
                </Popup>
            </Marker>
        };
        return markerPolyline;
    };




    render() {
        const {classes, productTagResult} = this.props;

        const markersPolylines = this.createMarkersAndPolylines(productTagResult);

        // console.log("MARKER POLYLINES: ", markersPolylines);

        const polylines = markersPolylines ? markersPolylines.polylines : [];
        const markers = markersPolylines ? markersPolylines.markers : [];


        // calculate the average of the coordinates to center the map view
        let latitudeCenter = 0;
        let longitudeCenter = 0;

        const bound = markers.forEach(marker => {
            latitudeCenter += marker.props.position[0];
            longitudeCenter += marker.props.position[1];
            return [marker.props.position[0], marker.props.position[1]];
        });

        const boundCenter = [latitudeCenter / markers.length, longitudeCenter / markers.length];

        const mapHeight = window.outerHeight - (70 + 70);

        const mapStyle = this.state.open ? {
            height: '100%', width: '100%', margin: '0%'
        } : {
            height: `${mapHeight}px`, margin: '0px'
        };

        const map =
            (<Map
                bound={bound}
                center={boundCenter}
                zoom={7}
                style={mapStyle}>
                <TileLayer
                    attribution="&amp;copy <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                {markers}
                <Polyline positions={polylines}/>

            </Map>);

        const modal = (<Modal
            style={{}}
            aria-labelledby="simple-modal-title"
            aria-describedby="simple-modal-description"
            open={this.state.open}
            onClose={this.handleClose}
        >
            <div className={classes.paper}>

                {map}

                <Button variant="outlined" style={{
                    border: '2px solid grey',
                    backgroundColor: 'white',
                    // float: 'right',
                    marginLeft: '5%',
                    zIndex: '1000',
                    bottom: '70px'
                }} onClick={this.handleClose}>{this.props.translations.mapCloseFullScreen}</Button>
            </div>
        </Modal>);

        const show = this.state.open ? modal : map;

        return (
            <div>
                {show}
                <Button variant="outlined" style={{
                    border: '2px solid grey',
                    backgroundColor: 'white',
                    // float: 'right',
                    marginLeft: '5%',
                    zIndex: '1000',
                    bottom: '70px'
                }} onClick={this.handleOpen}>{this.props.translations.mapOpenFullScreen}</Button>
            </div>
        );
    }
}

PTMapView.propTypes = {
    classes: PropTypes.object.isRequired,
};


const mapStateToProps = state => {
    const {translations} = state.lang;
    return {
        translations: translations
    };
};


export default connect(mapStateToProps)(withStyles(styles)(PTMapView));