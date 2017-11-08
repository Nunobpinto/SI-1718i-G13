package serie1.exer7.encryptionAlgorithms.symmetric;

import org.junit.jupiter.api.Test;
import serie1.ex7.encryptionAlgorithms.symmetric.BlowfishAlgorithm;

import javax.crypto.SecretKey;

import static org.junit.Assert.assertEquals;

public class BlowfishAlgorithmTest {

    @Test
    public void testEncryptionSuccessful() {
        String plainText = "Isto é uma string de teste";
        String decryptedCipherText="";
        try{
            BlowfishAlgorithm blowfishAlgorithm = new BlowfishAlgorithm();
            SecretKey key = blowfishAlgorithm.generateSecretKey();
            byte[] cipherText = blowfishAlgorithm.encrypt(plainText.getBytes(), key);
            decryptedCipherText = new String(blowfishAlgorithm.decrypt(cipherText, key));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println(" Plain text: " + plainText);
        System.out.println("Cipher text: "+ decryptedCipherText);
        assertEquals(plainText, decryptedCipherText);
    }
}
