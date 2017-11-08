package serie1.ex7.encryptionAlgorithms.asymmetric;

import javax.crypto.*;
import java.security.*;

public class RSAAlgorithm {
    private static final String ALGORITHM = "RSA";
    private static final int RSA_KEY_SIZE = 1024;    // in bits
    private String mode;
    private Cipher cipher;

    public RSAAlgorithm(String mode) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.mode = mode;
        cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
    }

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(RSA_KEY_SIZE);
        return keyPairGenerator.genKeyPair();
    }

    public byte[] encrypt(SecretKey keyToEncrypt, PublicKey publicKey) throws InvalidKeyException, IllegalBlockSizeException {
        cipher.init(Cipher.WRAP_MODE, publicKey);
        return cipher.wrap(keyToEncrypt);
    }

    public SecretKey decrypt(byte[] keyToDecrypt, PrivateKey privateKey) throws InvalidKeyException, NoSuchAlgorithmException {
        cipher.init(Cipher.UNWRAP_MODE, privateKey);
        return (SecretKey)cipher.unwrap(keyToDecrypt, mode, Cipher.SECRET_KEY);
    }

}
