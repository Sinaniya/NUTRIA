package ch.uzh.csg.foodchain.interfaces;

import java.util.List;


/**
 * The interface Certificate addition callback.
 */
public interface CertificateAdditionCallback {
    /**
     * Gets user defined certificate.
     *
     * @param userCertificates the user certificates
     */
    public void getUserDefinedCertificate(List<String> userCertificates);


}
