import React from 'react';
import PTMapView from './PTMapView';

const PtViewWrapper = (props) => {
    const {productTagResult} = props;
    return (
        <div>
            <PTMapView productTagResult={productTagResult}/>
            {/*<PTTableView chainResult={chainResult}/>*/}
        </div>
    );
};

export default PtViewWrapper;