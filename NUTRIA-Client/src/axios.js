import axios from 'axios';

const instance = axios.create({
    baseURL: 'https://foodchain.ddns.net:8443'
});

export default instance;