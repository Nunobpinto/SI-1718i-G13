package serie1.ex7;

import serie1.ex7.encryptionAlgorithms.asymmetric.RSAAlgorithm;
import serie1.ex7.encryptionAlgorithms.symmetric.ISymmetricAlgorithm;
import serie1.ex7.utils.ThrowableFunction;
import serie1.ex7.utils.Utils;

import javax.crypto.SecretKey;
import java.io.File;
import java.nio.file.Files;
import java.security.PublicKey;

public class Encryptor {

    public static void encrypt(File file, ThrowableFunction<byte[], ISymmetricAlgorithm> symmetricAlgorithmGenerator, PublicKey publicKey) throws Exception {
        ISymmetricAlgorithm symmetricAlgorithm = symmetricAlgorithmGenerator.apply(null);

        RSAAlgorithm rsa = new RSAAlgorithm(symmetricAlgorithm.getMode());
        byte[] plainText = Files.readAllBytes(file.toPath());
        SecretKey symmetricSecretKey = symmetricAlgorithm.generateSecretKey();

        byte[] cipherText = symmetricAlgorithm.encrypt(plainText, symmetricSecretKey);
        byte[] encSymmetricSecretKey = rsa.encrypt(symmetricSecretKey, publicKey);
        byte[] symIV = symmetricAlgorithm.getIV();

        String metadata = Utils.Base64Encode(symIV) +
                "." +
                Utils.Base64Encode(encSymmetricSecretKey) +
                "." +
                Utils.Base64Encode(file.getName().getBytes());

        Utils.createOutputFile("res\\exer7\\metadata", metadata.getBytes());
        Utils.createOutputFile("res\\exer7\\cipherFile", Utils.Base64Encode(cipherText).getBytes());
    }
}
