import React, {Component} from 'react';
import {Map, TileLayer, Marker, Popup} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

import L from 'leaflet';

delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
    iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png')
});

class MapView extends Component {
    state = {
        zoom: 13
    };

    render() {
        const position = [this.props.productTagResult.latitude, this.props.productTagResult.longitude];
        const markerPosition = [this.props.productTagResult.latitude, this.props.productTagResult.longitude];


        return (
            <Map
                center={position}
                zoom={this.state.zoom}
                style={{height: '98%', width: '98%', margin: '1% 1%'}}>
                <TileLayer
                    attribution="&amp;copy <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <Marker
                    position={markerPosition}>
                    <Popup minWidth={90}>
                        should be the producer's name, date
                    </Popup>
                </Marker>
            </Map>
        )
    }
}


export default MapView;