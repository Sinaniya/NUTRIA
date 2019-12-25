package ch.uzh.csg.foodchain.Models;

/**
 * The type Map data model.
 */
public class MapDataModel {
    /**
     * The Product id.
     */
    int productId;
    /**
     * The Lat.
     */
    Double lat;
    /**
     * The Lng.
     */
    Double lng;
    /**
     * The Date time.
     */
    String dateTime;
    /**
     * The Curr hash.
     */
    String currHash;
    /**
     * The Pre hash.
     */
    String preHash;
    /**
     * The Actions.
     */
    String actions;
    /**
     * The Certificates.
     */
    String certificates;
    /**
     * The Product tag actions.
     */
    String productTagActions;
    /**
     * The Producer name.
     */
    String producerName;

    //Modification example
    //    String actiondate;
    //
    //    public void setActiondate(String actiondate) {
    //        this.actiondate = actiondate;
    //    }
    //
    //    public String getActiondate() {
    //        return actiondate;
    //    }

    /**
     * Sets curr hash.
     *
     * @param currHash the curr hash
     */
    public void setCurrHash(String currHash) {
        this.currHash = currHash;
    }

    /**
     * Sets date time.
     *
     * @param dateTime the date time
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Sets lat.
     *
     * @param lat the lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * Sets lng.
     *
     * @param lng the lng
     */
    public void setLng(Double lng) {
        this.lng = lng;
    }

    /**
     * Sets pre hash.
     *
     * @param preHash the pre hash
     */
    public void setPreHash(String preHash) {
        this.preHash = preHash;
    }

    /**
     * Sets product id.
     *
     * @param productId the product id
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Sets actions.
     *
     * @param actions the actions
     */
    public void setActions(String actions) {
        this.actions = actions;
    }

    /**
     * Sets certificates.
     *
     * @param certificates the certificates
     */
    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    /**
     * Sets producer name.
     *
     * @param producerName the producer name
     */
    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    /**
     * Sets product tag actions.
     *
     * @param productTagActions the product tag actions
     */
    public void setProductTagActions(String productTagActions) {
        this.productTagActions = productTagActions;
    }

    /**
     * Gets lat.
     *
     * @return the lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     * Gets lng.
     *
     * @return the lng
     */
    public Double getLng() {
        return lng;
    }

    /**
     * Gets product id.
     *
     * @return the product id
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Gets curr hash.
     *
     * @return the curr hash
     */
    public String getCurrHash() {
        return currHash;
    }

    /**
     * Gets date time.
     *
     * @return the date time
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Gets pre hash.
     *
     * @return the pre hash
     */
    public String getPreHash() {
        return preHash;
    }

    /**
     * Gets actions.
     *
     * @return the actions
     */
    public String getActions() {
        return actions;
    }

    /**
     * Gets certificates.
     *
     * @return the certificates
     */
    public String getCertificates() {
        return certificates;
    }

    /**
     * Gets producer name.
     *
     * @return the producer name
     */
    public String getProducerName() {
        return producerName;
    }

    /**
     * Gets product tag actions.
     *
     * @return the product tag actions
     */
    public String getProductTagActions() {
        return productTagActions;
    }
}
