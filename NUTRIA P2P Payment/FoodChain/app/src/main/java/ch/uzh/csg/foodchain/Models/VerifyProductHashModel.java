package ch.uzh.csg.foodchain.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * The type Verify product hash model.
 */
public class VerifyProductHashModel {

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

    /**
     * Gets product tag id.
     *
     * @return the product tag id
     */
    @SerializedName("productTagActions")


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


}
