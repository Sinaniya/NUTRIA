package ch.uzh.csg.foodchain.Models.CreateProductTag;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Product tag.
 */
public class ProductTag {

    @SerializedName("productTagId")
    @Expose
    private Integer productTagId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("productTagHash")
    @Expose
    private String productTagHash;
    @SerializedName("previousProductTagHash")
    @Expose
    private String previousProductTagHash = null;
    @SerializedName("productTagProducer")
    @Expose
    private ProductTagProducer productTagProducer;
    @SerializedName("productTagActions")
    @Expose
    private List<ProductTagAction> productTagActions = null;

    /**
     * Gets product tag id.
     *
     * @return the product tag id
     */
    public Integer getProductTagId() {
        return productTagId;
    }

    /**
     * Sets product tag id.
     *
     * @param productTagId the product tag id
     */
    public void setProductTagId(Integer productTagId) {
        this.productTagId = productTagId;
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

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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
     * Gets previous product tag hash.
     *
     * @return the previous product tag hash
     */
    public String getPreviousProductTagHash() {
        return previousProductTagHash;
    }

    /**
     * Sets previous product tag hash.
     *
     * @param previousProductTagHash the previous product tag hash
     */
    public void setPreviousProductTagHash(String previousProductTagHash) {
        this.previousProductTagHash = previousProductTagHash;
    }

    /**
     * Gets product tag producer.
     *
     * @return the product tag producer
     */
    public ProductTagProducer getProductTagProducer() {
        return productTagProducer;
    }

    /**
     * Sets product tag producer.
     *
     * @param productTagProducer the product tag producer
     */
    public void setProductTagProducer(ProductTagProducer productTagProducer) {
        this.productTagProducer = productTagProducer;
    }

    /**
     * Gets product tag actions.
     *
     * @return the product tag actions
     */
    public List<ProductTagAction> getProductTagActions() {
        return productTagActions;
    }

    /**
     * Sets product tag actions.
     *
     * @param productTagActions the product tag actions
     */
    public void setProductTagActions(List<ProductTagAction> productTagActions) {
        this.productTagActions = productTagActions;
    }

}
