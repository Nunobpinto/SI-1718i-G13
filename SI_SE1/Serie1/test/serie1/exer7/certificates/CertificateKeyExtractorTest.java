package serie1.exer7.certificates;

import org.junit.Assert;
import org.junit.Test;
import serie1.ex7.certificates.CertificateKeyExtractor;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertPathValidatorException;

public class CertificateKeyExtractorTest {
    private static final String PASSWORD = "changeit";
    private static final String CA1_KEYSTORE = "res\\exer7\\certificates\\CA1.jks";
    private static final String CA2_KEYSTORE = "res\\exer7\\certificates\\CA2.jks";
    private static final String ALICE_CERT1 = "res\\exer7\\certificates\\Alice_1.cer";
    private static final String ALICE_CERT2 = "res\\exer7\\certificates\\Alice_2.cer";
    private static final String ALICE_PFX1 = "res\\exer7\\certificates\\Alice_1.pfx";
    private static final String ALICE_PFX2 = "res\\exer7\\certificates\\Alice_2.pfx";

    @Test
    public void testPublicKeyExtractionSuccessful() throws Exception {
        CertificateKeyExtractor certificateKeyExtractor = new CertificateKeyExtractor(CA1_KEYSTORE, PASSWORD);
        FileInputStream certificateFile = new FileInputStream(ALICE_CERT1);

        PublicKey publicKey = certificateKeyExtractor.getPublicKeyFromCertificate(certificateFile);

        Assert.assertNotNull(publicKey);
    }

    @Test(expected = CertPathValidatorException.class)
    public void testPublicKeyExtractionFailCertValidation() throws Exception {
        CertificateKeyExtractor certificateKeyExtractor = new CertificateKeyExtractor(CA2_KEYSTORE, PASSWORD);
        FileInputStream certificateFile = new FileInputStream(ALICE_CERT1);

        PublicKey publicKey = certificateKeyExtractor.getPublicKeyFromCertificate(certificateFile);

        Assert.assertNotNull(publicKey);
    }

    @Test
    public void testPrivateKeyExtractionSuccessful() throws Exception {
        CertificateKeyExtractor certificateKeyExtractor = new CertificateKeyExtractor(PASSWORD);
        FileInputStream pfxFile = new FileInputStream(ALICE_PFX1);

        PrivateKey privateKey = certificateKeyExtractor.getPrivateKeyFromKeyStore(pfxFile);

        Assert.assertNotNull(privateKey);
    }
}
