
package ch.uzh.csg.foodchain.Models.ScannedQRModel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Product tag producer.
 */
public class ProductTagProducer implements Serializable {

    @SerializedName("producerId")
    @Expose
    private Integer producerId;
    @SerializedName("producerName")
    @Expose
    private String producerName;
    @SerializedName("licenceNumber")
    @Expose
    private String licenceNumber;
    @SerializedName("ethereumAccount")
    @Expose
    private String ethereumAccount;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("producerCertificates")
    @Expose
    private List<ProducerCertificate> producerCertificates = null;
    @SerializedName("producerActions")
    @Expose
    private List<ProducerAction> producerActions = null;

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
     * Gets ethereum account.
     *
     * @return the ethereum account
     */
    public String getEthereumAccount() {
        return ethereumAccount;
    }

    /**
     * Sets ethereum account.
     *
     * @param ethereumAccount the ethereum account
     */
    public void setEthereumAccount(String ethereumAccount) {
        this.ethereumAccount = ethereumAccount;
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

    /**
     * Gets producer certificates.
     *
     * @return the producer certificates
     */
    public List<ProducerCertificate> getProducerCertificates() {
        return producerCertificates;
    }

    /**
     * Sets producer certificates.
     *
     * @param producerCertificates the producer certificates
     */
    public void setProducerCertificates(List<ProducerCertificate> producerCertificates) {
        this.producerCertificates = producerCertificates;
    }

    /**
     * Gets producer actions.
     *
     * @return the producer actions
     */
    public List<ProducerAction> getProducerActions() {
        return producerActions;
    }

    /**
     * Sets producer actions.
     *
     * @param producerActions the producer actions
     */
    public void setProducerActions(List<ProducerAction> producerActions) {
        this.producerActions = producerActions;
    }

}
