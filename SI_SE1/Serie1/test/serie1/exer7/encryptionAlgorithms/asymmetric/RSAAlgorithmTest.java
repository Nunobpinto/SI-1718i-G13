package serie1.exer7.encryptionAlgorithms.asymmetric;

import org.junit.jupiter.api.Test;
import serie1.ex7.encryptionAlgorithms.asymmetric.RSAAlgorithm;
import serie1.ex7.encryptionAlgorithms.symmetric.AESAlgorithm;

import javax.crypto.SecretKey;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class RSAAlgorithmTest {

    @Test
    public void testKeyEncryptionSuccessful(){
        SecretKey AESkeyToEncrypt = null;
        SecretKey AESkeyDecrypted = null;
        KeyPair RSAkey;
        try {
            AESAlgorithm aesAlgorithm = new AESAlgorithm();
            RSAAlgorithm rsaEncryptor = new RSAAlgorithm("AES");
            AESkeyToEncrypt = aesAlgorithm.generateSecretKey();
            RSAkey = rsaEncryptor.generateKeyPair();
            byte[] AESKeyAux = rsaEncryptor.encrypt(AESkeyToEncrypt, RSAkey.getPublic());
            AESkeyDecrypted = rsaEncryptor.decrypt(AESKeyAux, RSAkey.getPrivate());
        } catch(Exception ex){
            ex.printStackTrace();
        }
        assertArrayEquals(AESkeyToEncrypt.getEncoded(), AESkeyDecrypted.getEncoded());
    }
}
