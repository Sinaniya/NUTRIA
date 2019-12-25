import React, {Component} from 'react';
import QrReader from 'react-qr-reader';


class ScanPage extends Component {
    state = {
        qrCode: null,
        delay: 500
    };

    handleScan = (data) => {
        if (data) {
            this.setState({qrCode: data});
            this.props.history.push(`/product-tags/${data}`);
        }
    };

    handleError = () => {
        console.log('error while scanning');
    };

    render() {
        return (
            <div className="scan-comp"
                 style={{
                     width: '90%',
                     margin: '5% 5%',
                     backgroundColor: 'white'
                 }}>
                <QrReader
                    onError={this.handleError}
                    onScan={this.handleScan}
                    delay={this.state.delay}
                    style={{width: '100%'}}
                />
            </div>
        );
    }
}

export default ScanPage;