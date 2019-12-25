import * as actionTypes from '../actions';

const enTranslation = {
    language: 'Language',
    aboutUs: 'About us',
    contactUs: 'Contact us',
    scanButton: 'Scan Product',
    university: 'Department of Informatics - Communication Systems Group - UZH',
    mapOpenFullScreen: 'Full Screen',
    mapCloseFullScreen: 'Close Full Screen',
    productTagDetails: 'Product Tag Details',
    productTagActions: 'Product Tag Actions',
    date: 'Date',
    ptProducer: 'Product Tag Producer',
    geoData: 'Geodata',
    longitude: 'Longitude',
    latitude: 'Latitude',
    producerLicenceNumber: 'Producer\'s Licence Number',
    producerUrl: 'Producer\'s URL',
    producerName: 'Producer\'s Name'
};

const deTranslation = {
    language: 'Sprache',
    aboutUs: 'Über uns',
    contactUs: 'Wenden Sie sich an uns.',
    scanButton: 'Scan Produkt',
    university: 'Department of Informatics - Communication Systems Group - UZH',
    mapOpenFullScreen: 'Vollbild',
    mapCloseFullScreen: 'Vollbild schliessen',
    productTagDetails: 'Product Beschreibung',
    productTagActions: 'Produkt Aktionen',
    date: 'Datum',
    ptProducer: 'Hersteller des Produktes',
    geoData: 'Geodaten',
    longitude: 'Längengrad',
    latitude: 'Breitengrad',
    producerLicenceNumber: 'Lizenznummer des Herstellers',
    producerUrl: 'Webadresse des Herstellers',
    producerName: 'Name des Herstellers'
};

const frTranslation = {
    language: 'La langue',
    aboutUs: 'À propos de nous',
    contactUs: 'Contactez nous',
    scanButton: 'Numériser',
    university: 'Department of Informatics - Communication Systems Group - UZH',
    mapOpenFullScreen: 'Plein écran',
    mapCloseFullScreen: 'Close Full Screen',
    productTagDetails: 'Fermer plein écran',
    productTagActions: 'Le Nom de l\'Action',
    date: 'Rendez-vous amoureux',
    ptProducer: 'Producteur du produit',
    geoData: 'Géodonnées',
    longitude: 'Longitude',
    latitude: 'Latitude',
    producerLicenceNumber: 'Le Nom du Certificat',
    producerUrl: 'URL du producteur',
    producerName: 'Nom du producteur'
};

const itTranslation = {
    language: 'Linguaggio',
    aboutUs: 'Riguardo a noi',
    contactUs: 'Contattaci',
    scanButton: 'Scansione del prodotto',
    university: 'Department of Informatics - Communication Systems Group - UZH',
    mapOpenFullScreen: 'A schermo intero',
    mapCloseFullScreen: 'Chiudi schermo intero',
    productTagDetails: 'Dettagli tag prodotto',
    productTagActions: 'Azioni tag prodotto',
    date: 'Data',
    ptProducer: 'Produttore tag prodotto',
    geoData: 'Geodata',
    longitude: 'Longitudine',
    latitude: 'Latitudine',
    producerLicenceNumber: 'Numero di licenza del produttore',
    producerUrl: 'URL del produttore',
    producerName: 'Nome del produttore'
};

const ptTranslation = {
    language: 'Língua',
    aboutUs: 'Sobre nós',
    contactUs: 'Contate-Nos',
    scanButton: 'Produto de digitalização',
    university: 'Department of Informatics - Communication Systems Group - UZH',
    mapOpenFullScreen: 'Tela cheia',
    mapCloseFullScreen: 'Fechar tela cheia',
    productTagDetails: 'Detalhes do tag de produto',
    productTagActions: 'Ações de tag de produto',
    date: 'Encontro',
    ptProducer: 'Produtor de etiquetas de produto',
    geoData: 'Geodata',
    longitude: 'Longitude',
    latitude: 'Latitude',
    producerLicenceNumber: 'Número de Licença do Produtor',
    producerUrl: 'URL do produtor',
    producerName: 'Nome do Produtor'
};


const initialState = {
    languages: ['EN', 'DE', 'FR', 'IT', 'PT'],
    defaultLanguage: 'EN',
    currentLanguage: 'EN',
    translations: enTranslation
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.CHANGE_LANGUAGE_ACTION:
            let translation = enTranslation;
            switch (action.payload) {
                case 'EN':
                    translation = enTranslation;
                    break;
                case 'DE' :
                    translation = deTranslation;
                    break;
                case 'FR' :
                    translation = frTranslation;
                    break;
                case 'IT' :
                    translation = itTranslation;
                    break;
                case 'PT' :
                    translation = ptTranslation;
                    break;
            }
            return {
                ...state,
                currentLanguage: action.payload,
                translations: translation
            };
        default:
            return state;
    }
};

export default reducer;

