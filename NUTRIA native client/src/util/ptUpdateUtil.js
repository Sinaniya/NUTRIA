const getUpdatedPTToAdd = scannedProductTag => {
    const {hash, ptChain} = scannedProductTag;
    const ptDetails = ptChain.filter(pt => pt.productTagHash === hash)[0];
    const scannedPT = {
        hash,
        ptDetails,
        ptChain
    };
    return scannedPT;
};

export default getUpdatedPTToAdd;