package serie1.ex7.encryptionAlgorithms.symmetric;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public interface ISymmetricAlgorithm {

    SecretKey generateSecretKey() throws NoSuchAlgorithmException;

    byte[] encrypt(byte [] value, SecretKey key) throws Exception;

    byte[] decrypt(byte[] value, SecretKey key) throws Exception;

    String getMode();

    byte[] getIV();
}
