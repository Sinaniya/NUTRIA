const checkIfAlreadyScanned = (scannedProductTags, productTag) => {
    return scannedProductTags.some(pt => (
        pt.hash === productTag.hash
    ))
};

export default checkIfAlreadyScanned;