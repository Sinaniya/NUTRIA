import {Print} from "expo";
import QRCode from "qrcode";


const printQrCode = async hash => {

    const qrcode = await generateQr(hash);
    // console.log(qrcode);
    // Print.printAsync({
    //     html: `<style>html, body { width: 5cm; height: 5cm; }</style><div>${qrcode}</div>`,
    // });

    await Print.printAsync({
        html: `<div>${qrcode}</div>`
    });

};

const generateQr = async hash => {
    try {
        return await QRCode.toString(hash);
    } catch (e) {
        console.log(e);
    }
};

export default printQrCode;