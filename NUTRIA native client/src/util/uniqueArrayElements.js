const getUnique = (array, fieldName) => {
    const set = new Set();
    return array.map((v, index) => {
        if(set.has(v[fieldName])) {
            return false
        } else {
            set.add(v[fieldName]);
            return index;
        }
    }).filter(e=>e).map(e=>array[e]);
};

export default getUnique;