package serie1.ex7;

import serie1.ex7.certificates.*;
import serie1.ex7.encryptionAlgorithms.symmetric.*;
import serie1.ex7.utils.ThrowableFunction;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class App {
    private Map<String, ThrowableFunction<byte[], ISymmetricAlgorithm>> symmetricAlgorithms;
    private ThrowableFunction<byte[], ISymmetricAlgorithm> symmetricAlgorithmGenerator;
    private CertificateKeyExtractor certificateKeyExtractor;
    private FileInputStream certificate;
    private File file, metadata;
    private String operation;

    public App(String operation, String mode, String fileName, String cert, String keyStore) throws Exception {
        fillSymmetricEncryptorsList();
        this.symmetricAlgorithmGenerator = symmetricAlgorithms.get(mode);
        this.certificateKeyExtractor = new CertificateKeyExtractor(keyStore, "changeit");
        this.certificate = new FileInputStream(cert);
        this.file = new File(fileName);
        this.operation = operation;
    }

    public App(String operation, String mode, String fileName, String metadata, String pfx, String password) throws Exception {
        fillSymmetricEncryptorsList();
        this.symmetricAlgorithmGenerator = symmetricAlgorithms.get(mode);
        this.certificateKeyExtractor = new CertificateKeyExtractor(password);
        this.certificate = new FileInputStream(pfx);
        this.file = new File(fileName);
        this.metadata = new File(metadata);
        this.operation = operation;
    }

    private void fillSymmetricEncryptorsList() throws Exception {
        symmetricAlgorithms = new HashMap<>();
        symmetricAlgorithms.put("AES", (iv) -> iv == null ? new AESAlgorithm() : new AESAlgorithm(iv));
        symmetricAlgorithms.put("Blowfish", (iv) -> iv == null ? new BlowfishAlgorithm() : new BlowfishAlgorithm(iv));
    }

    public void run() throws Exception {
        if (operation.equals("encrypt")) {
            Encryptor.encrypt(file, symmetricAlgorithmGenerator, certificateKeyExtractor.getPublicKeyFromCertificate(certificate));
        } else {
            Decryptor.decrypt(file, metadata, symmetricAlgorithmGenerator, certificateKeyExtractor.getPrivateKeyFromKeyStore(certificate));
        }
    }

    private static App create(String[] args) throws Exception {
        if( args.length != 5 && args.length != 6 )
            throw new Exception("Bad arguments!");
        if(args.length == 5 )
            return new App(args[0], args[1], args[2], args[3], args[4]);
        return new App(args[0], args[1], args[2], args[3],args[4], args[5]);
    }

    public static void main(String[] args) {
        try {
            App app = App.create(args);
            app.run();
        }catch ( Exception e ) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}