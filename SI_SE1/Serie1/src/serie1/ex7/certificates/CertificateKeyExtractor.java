package serie1.ex7.certificates;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.*;
import java.util.*;

public class CertificateKeyExtractor {
    private static final String VALIDATION_ALGORITHM = "PKIX";
    private char[] keyStorePassword;
    private FileInputStream keyStoreFile;
    private Set<X509Certificate> intermediateCerts;

    public CertificateKeyExtractor(String keyStoreFile, String keyStorePassword) throws Exception {
        fillCertificateList();
        this.keyStorePassword = keyStorePassword.toCharArray();
        this.keyStoreFile = new FileInputStream(keyStoreFile);
    }

    public CertificateKeyExtractor(String keyStorePassword) throws Exception {
        fillCertificateList();
        this.keyStorePassword = keyStorePassword.toCharArray();
    }

    public PublicKey getPublicKeyFromCertificate(FileInputStream fin) throws Exception {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)certFactory.generateCertificate(fin);
        if (!checkIfCertificateIsValid(certificate))
            throw new CertPathValidatorException("Certificate validation failed!!");
        return certificate.getPublicKey();
    }

    public PrivateKey getPrivateKeyFromKeyStore(FileInputStream fin) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(fin, keyStorePassword);
        Enumeration<String> aliases = keyStore.aliases();
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keyStorePassword);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)getPrivateKeyEntry(keyStore, aliases, protParam);
        return privateKeyEntry.getPrivateKey();
    }

    private void fillCertificateList() throws Exception {
        String[] intCerts = {
                "res\\exer7\\certificates\\CA1-int.cer",
                "res\\exer7\\certificates\\CA2-int.cer"
        };
        intermediateCerts = new HashSet<>();
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        FileInputStream fin;
        for (String aIntCert : intCerts) {
            fin = new FileInputStream(aIntCert);
            X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(fin);
            intermediateCerts.add(certificate);
            fin.close();
        }
    }

    private boolean checkIfCertificateIsValid(X509Certificate cert) throws Exception {
        return checkIfCertificateIsSelfValidated(cert) || verifyCertificate(cert);
    }

    private boolean verifyCertificate(X509Certificate cert) throws Exception {
        try {
            PKIXBuilderParameters pkixBuilderParameters = instantiatePKIXParams(cert);

            CertPathBuilder certPathBuilder = CertPathBuilder.getInstance(VALIDATION_ALGORITHM);
            CertPath certPath = certPathBuilder.build(pkixBuilderParameters).getCertPath();

            CertPathValidator certPathValidator = CertPathValidator.getInstance(VALIDATION_ALGORITHM);
            certPathValidator.validate(certPath, pkixBuilderParameters);
        }catch (CertPathValidatorException | CertPathBuilderException e) {
            return false;
        }
        return true;
    }

    private PKIXBuilderParameters instantiatePKIXParams(X509Certificate cert) throws Exception {
        X509CertSelector certToValidateSelector = new X509CertSelector();
        certToValidateSelector.setCertificate(cert);

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(keyStoreFile, keyStorePassword);

        CertStore intermediateCertStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(intermediateCerts));

        PKIXBuilderParameters res = new PKIXBuilderParameters(keyStore, certToValidateSelector);
        res.setRevocationEnabled(false);
        res.addCertStore(intermediateCertStore);
        return res;
    }

    private boolean checkIfCertificateIsSelfValidated(X509Certificate cert) throws Exception {
        String subjectdn = cert.getSubjectDN().getName();
        String issuerdn = cert.getIssuerDN().getName();
        if (subjectdn.equals(issuerdn)){
            cert.verify(cert.getPublicKey()) ;
            return true;
        }
        return false;
    }

    private Object getPrivateKeyEntry(KeyStore ks, Enumeration<String> aliases, KeyStore.ProtectionParameter protParam) throws Exception {
        Object aux = null;
        String alias;
        while(aliases.hasMoreElements()) {
            alias = aliases.nextElement();
            aux = ks.getEntry(alias, protParam);
            if (aux instanceof KeyStore.PrivateKeyEntry)
                break;
        }
        return aux;
    }
}
