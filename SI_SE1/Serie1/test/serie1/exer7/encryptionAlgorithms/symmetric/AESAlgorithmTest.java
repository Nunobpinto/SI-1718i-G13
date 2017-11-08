package serie1.exer7.encryptionAlgorithms.symmetric;

import org.junit.jupiter.api.Test;
import serie1.ex7.encryptionAlgorithms.symmetric.AESAlgorithm;

import javax.crypto.SecretKey;

import static org.junit.Assert.assertEquals;

public class AESAlgorithmTest {

    @Test
    public void testEncryptionSuccessful() {
        String plainText = "Isto é uma string de teste";
        String decryptedCipherText="";
        try{
            AESAlgorithm aesEncryptor = new AESAlgorithm();
            SecretKey key = aesEncryptor.generateSecretKey();
            byte[] cipherText = aesEncryptor.encrypt(plainText.getBytes(), key);
            decryptedCipherText = new String(aesEncryptor.decrypt(cipherText, key));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println(" Plain text: " + plainText);
        System.out.println("Cipher text: "+ decryptedCipherText);
        assertEquals(plainText, decryptedCipherText);
    }
}
