import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import Layout from './containers/Layout';
import Homepage from "./components/pages/Homepage";
import ScanPage from './components/pages/ScanPage';
import ResultPage from './components/pages/PTResultPage';
import PTViewWrapper from './components/fragments/PTViewWrapper';
import 'leaflet/dist/leaflet.css';


class App extends Component {
  render() {
    return (
        <Router>
            <Layout>
                <Switch>
                    <Route exact path="/" component={Homepage}/>
                    <Route exact path="/scan" component={ScanPage}/>
                    <Route exact path="/product-tags/:hash" component={ResultPage}/>
                    <Route exact path="/product-tags/history/:hash" component={PTViewWrapper}/>
                </Switch>
            </Layout>
        </Router>
    );
  }
}

export default App;
