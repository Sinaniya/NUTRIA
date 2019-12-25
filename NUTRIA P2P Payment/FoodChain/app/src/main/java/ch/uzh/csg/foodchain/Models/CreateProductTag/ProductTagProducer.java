package ch.uzh.csg.foodchain.Models.CreateProductTag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Product tag producer.
 */
public class ProductTagProducer {

    @SerializedName("producerId")
    @Expose
    private Integer producerId;
    @SerializedName("producerName")
    @Expose
    private String producerName;
    @SerializedName("licenceNumber")
    @Expose
    private String licenceNumber;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * Gets producer id.
     *
     * @return the producer id
     */
    public Integer getProducerId() {
        return producerId;
    }

    /**
     * Sets producer id.
     *
     * @param producerId the producer id
     */
    public void setProducerId(Integer producerId) {
        this.producerId = producerId;
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
     * Sets producer name.
     *
     * @param producerName the producer name
     */
    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    /**
     * Gets licence number.
     *
     * @return the licence number
     */
    public String getLicenceNumber() {
        return licenceNumber;
    }

    /**
     * Sets licence number.
     *
     * @param licenceNumber the licence number
     */
    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}