package ch.uzh.csg.foodchain.Models;

/**
 * The type All certificate model.
 */
public class AllCertificateModel {

    /**
     * The Certificate id.
     */
    String certificateId;
    /**
     * The Certificate name.
     */
    String certificateName;

    /**
     * Is checked boolean.
     *
     * @return the boolean
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Sets checked.
     *
     * @param checked the checked
     */
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /**
     * The Is checked.
     */
    boolean isChecked;

    /**
     * Gets certificate id.
     *
     * @return the certificate id
     */
    public String getCertificateId() {
        return certificateId;
    }

    /**
     * Sets certificate id.
     *
     * @param certificateId the certificate id
     */
    public void setCertificateId(String certificateId) {
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

