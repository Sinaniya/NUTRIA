import React from 'react';
import {withRouter} from "react-router-dom";

import Aux from '../components/hoc/Aux';
import Header from '../components/layout/Header';
import Footer from '../components/layout/Footer';


const Layout = (props) => {

    let style = {minHeight: 'calc(100vh - 110px)'};
    switch (props.location.pathname) {
        case '/':
            style = {minHeight: 'calc(100vh - 95px)'};
            break
    }

    return (
        <Aux>
            <div style={style}>
                <Header/>
                {props.children}
            </div>
            <Footer/>
        </Aux>
    );
};

export default withRouter(Layout);
