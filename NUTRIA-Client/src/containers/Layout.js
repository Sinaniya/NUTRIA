import React from 'react';
import Aux from '../components/hoc/Aux';
import Header from '../components/Header';
import Footer from '../components/Footer';


const Layout = (props) => {
    return (
        <Aux>
            <Header/>
            {props.children}
            <Footer/>
        </Aux>
    );
};

export default Layout;
