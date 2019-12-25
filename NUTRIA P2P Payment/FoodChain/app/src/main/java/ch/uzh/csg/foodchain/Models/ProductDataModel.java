package ch.uzh.csg.foodchain.Models;


/**
 * The type Product data model.
 */
public class ProductDataModel {
    /**
     * The Product tag id.
     */
    String productTagId;
    /**
     * The Date.
     */
    String date;
    /**
     * The Product tag hash.
     */
    String productTagHash;
    /**
     * The Date time.
     */
    String DateTime;

    /**
     * Gets product tag id.
     *
     * @return the product tag id
     */
    public String getProductTagId() {
        return productTagId;
    }

    /**
     * Sets product tag id.
     *
     * @param productTagId the product tag id
     */
    public void setProductTagId(String productTagId) {
        this.productTagId = productTagId;
    }

    /**
     * Gets product tag hash.
     *
     * @return the product tag hash
     */
    public String getProductTagHash() {
        return productTagHash;
    }

    /**
     * Sets product tag hash.
     *
     * @param productTagHash the product tag hash
     */
    public void setProductTagHash(String productTagHash) {
        this.productTagHash = productTagHash;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

}
