import * as actionTypes from '../actions/actionTypes';

const enTranslation = {
    languageIndex: 0,
    producerTitle: 'Producer',
    consumerTitle: 'Consumer',
    language: 'Language',
    aboutUs: 'About us',
    contactUs: 'Contact us',
    scanButton: 'Scan Product',
    university: 'University of Zurich',
    mapOpenFullScreen: 'Full Screen',
    mapCloseFullScreen: 'Close Full Screen',
    productTagDetails: 'Product Tag Details',
    productTagActions: 'Product Tag Actions',
    productTagHash: 'Product Tag Hash',
    productTagProducer: 'Product Tag Producer',
    logIn: 'Login',
    signUp: 'Sign up',
    signOut: 'Sign Out',
    username: 'Username',
    password: 'Password',
    atLeast8Characters: 'Please enter at least 8 characters',
    producerName: 'Producer Name',
    licenceNumber: 'Licence Number',
    confirmPassword: 'Confirm Password',
    samePasswordError: 'Please enter the same password',
    scannedProducts: 'Scanned Products',
    ethereumAccount: 'Ethereum Account',
    websiteUrl: 'Your Website URL',
    certificates: 'Certificates',
    valueNotValid: 'Value not valid',
    enterNewCertificate: 'Enter new Certificate',
    error: 'Error',
    success: 'Success',
    certificateCreated: 'Certificate successfully added. Value:',
    defaultActions: 'Default Actions',
    enterNewAction: 'Enter new Action',
    actionCreated: 'Action successfully added. Value:',
    add: 'Add',
    loginUppercase: 'LOGIN',
    signUpUppercase: 'SIGN UP',
    producerMode: 'Producer Mode',
    settings: 'Settings',
    allMyProducts: 'All My Products',
    generatePTButton: 'Generate Product Tag',
    details: 'Details',
    date: 'Date',
    url: 'URL',
    ptScanSuccess: 'Product successfully scanned.',
    ptNotValid: 'Scanned product tag is not valid.',
    ptAlreadyScanned: 'This product is already scanned.',
    scanAgain: 'Scan again',
    close: 'Close',
    showDetails: 'Show Details',
    modalClosed: 'Modal has been closed.',
    closeQRScanner: 'Close QR Scanner',
    printQR: 'Print',
    warning: 'Warning',
    ptNotScanned: 'You have not scanned any product tag, are you sure it is the first product tag in the chain?',
    yesCreatePt: 'Yes, create Product Tag',
    noActionsSelected: 'You have not selected any action for this product tag!',
    errorWhileGeneratingPt: 'An error occurred while generating the product tag!',
    tryAgain: 'Try again',
    ptGenerated: 'Product Tag successfully generated!',
    markerMetadataStatus: 'Status: ',
    markerTitle: 'Marker Title',
    closeMap: 'Close Map',
    producerDetails: 'Producer Details',
    updateProducerButton: 'Update Producer',
    fcTrackingSystemTitle: 'Food Chain Tracking System',
    selectRoleTitle: 'Select your role'
};

const deTranslation = {
    languageIndex: 1,
    producerTitle: 'Produzent',
    consumerTitle: 'Konsument',
    language: 'Sprache',
    aboutUs: 'Über uns',
    contactUs: 'Wenden Sie sich an uns.',
    scanButton: 'Scan Produkt',
    university: 'Universität Zürich',
    mapOpenFullScreen: 'Vollbild',
    mapCloseFullScreen: 'Vollbild schliessen',
    productTagDetails: 'Product Beschreibung',
    productTagActions: 'Produkt Aktionen',
    productTagHash: 'Produkt-Tag-Hash',
    productTagProducer: 'Hersteller',
    logIn: 'Einloggen',
    signUp: 'Registrieren',
    signOut: 'Ausloggen',
    username: 'Nutzername',
    password: 'Kennwort',
    atLeast8Characters: 'Bitte geben Sie mindestens 8 Zeichen ein',
    producerName: 'Name des Herstellers',
    licenceNumber: 'Lizenznummer',
    confirmPassword: 'Passwort bestätigen',
    samePasswordError: 'Bitte geben Sie das gleiche Passwort ein',
    scannedProducts: 'Gescannte Produkte',
    ethereumAccount: 'Ethereum-Konto',
    websiteUrl: 'Ihre Website URL',
    certificates: 'Zertifikate',
    valueNotValid: 'Wert nicht gültig',
    enterNewCertificate: 'Neues Zertifikat eingeben',
    error: 'Fehler',
    success: 'Erfolg',
    certificateCreated: 'Zertifikat erfolgreich hinzugefügt: ',
    defaultActions: 'Standardaktionen',
    enterNewAction: 'Neue Aktion eingeben',
    actionCreated: 'Aktion erfolgreich hinzugefügt: ',
    add: 'Hinzufügen',
    loginUppercase: 'EINLOGGEN',
    signUpUppercase: 'REGISTRIEREN',
    producerMode: 'Producer-Modus',
    settings: 'Einstellungen',
    allMyProducts: 'Alle meine Produkte',
    generatePTButton: 'Produkt-Tag generieren',
    details: 'Einzelheiten',
    date: 'Datum',
    url: 'URL',
    ptScanSuccess: 'Produkt erfolgreich gescannt.',
    ptNotValid: 'Das gescannte Produktetikett ist ungültig.',
    ptAlreadyScanned: 'Dieses Produkt wurde bereits gescannt.',
    scanAgain: 'Erneut scannen',
    close: 'Schließen',
    showDetails: 'Zeige Details',
    modalClosed: 'Modal wurde geschlossen.',
    closeQRScanner: 'Schließen Sie den QR-Scanner',
    printQR: 'Drucken',
    warning: 'Warnung',
    ptNotScanned: 'Sie haben kein Produkt-Tag gescannt. Sind Sie sicher, dass es das erste Produkt-Tag in der Kette ist?',
    yesCreatePt: 'Ja, Produkt-Tag erstellen',
    noActionsSelected: 'Sie haben keine Aktion für dieses Produkt-Tag ausgewählt!',
    errorWhileGeneratingPt: 'Beim Generieren des Produkt-Tags ist ein Fehler aufgetreten!',
    tryAgain: 'Versuchen Sie es nochmal',
    ptGenerated: 'Produkt-Tag erfolgreich generiert!',
    markerMetadataStatus: 'Status: ',
    markerTitle: 'Markertitel',
    closeMap: 'Karte schließen',
    producerDetails: 'Angaben zum Hersteller',
    updateProducerButton: 'Produzent aktualisieren',
    fcTrackingSystemTitle: 'System zur Verfolgung der Nahrungskette',
    selectRoleTitle: 'Wählen Sie Ihre Rolle aus'
};

const initialState = {
    languages: ['EN', 'DE', 'FR', 'IT'],
    defaultLanguage: 'EN',
    currentLanguage: 'EN',
    translations: enTranslation
};


// todo implement FR and IT translations
const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.CHANGE_LANGUAGE_ACTION:
            let translation = state.translations;
            switch (action.payload) {
                case 0:
                    translation = enTranslation;
                    break;
                case 1 :
                    translation = deTranslation;
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
