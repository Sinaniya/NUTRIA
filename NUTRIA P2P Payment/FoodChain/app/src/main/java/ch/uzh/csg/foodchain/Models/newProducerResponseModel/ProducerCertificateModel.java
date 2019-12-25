package ch.uzh.csg.foodchain.Models.newProducerResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Producer certificate model.
 */
public class ProducerCertificateModel {
    @SerializedName("certificateId")
    @Expose
    private Integer certificateId;
    @SerializedName("certificateName")
    @Expose
    private String certificateName;

    /**
     * Gets certificate id.
     *
     * @return the certificate id
     */
    public Integer getCertificateId() {
        return certificateId;
    }

    /**
     * Sets certificate id.
     *
     * @param certificateId the certificate id
     */
    public void setCertificateId(Integer certificateId) {
        this.certificateId = certificateId;
    }

    /**
     * Gets certificate name.
     *
     * @return the certificate name
     */
    public String getCertificateName() {
        return certificateName;
    }

    /**
     * Sets certificate name.
     *
     * @param certificateName the certificate name
     */
    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

}
