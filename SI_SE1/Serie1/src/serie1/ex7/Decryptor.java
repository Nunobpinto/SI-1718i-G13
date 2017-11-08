package serie1.ex7;

import serie1.ex7.encryptionAlgorithms.asymmetric.RSAAlgorithm;
import serie1.ex7.encryptionAlgorithms.symmetric.ISymmetricAlgorithm;
import serie1.ex7.outputEncapsulators.Metadata;
import serie1.ex7.utils.ThrowableFunction;
import serie1.ex7.utils.Utils;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.util.Base64;

public class Decryptor {

    public static void decrypt(File file, File metadataFile, ThrowableFunction<byte[], ISymmetricAlgorithm> symmetricEncryptorGenerator, PrivateKey privateKey) throws Exception {
        Metadata metadata = Metadata.create(metadataFile);
        ISymmetricAlgorithm symmetricAlgorithm = symmetricEncryptorGenerator.apply(metadata.getIV());
        RSAAlgorithm rsa = new RSAAlgorithm(symmetricAlgorithm.getMode());

        SecretKey symmetricSecretKey = rsa.decrypt(metadata.getSymmetricKey(), privateKey);

        byte[] cipherText = Base64.getUrlDecoder().decode(Files.readAllBytes(file.toPath()));

        byte[] plainText = symmetricAlgorithm.decrypt(cipherText, symmetricSecretKey);
        Utils.createOutputFile("res\\exer7\\Decoded" + new String(metadata.getFileName()), plainText);
    }
}
