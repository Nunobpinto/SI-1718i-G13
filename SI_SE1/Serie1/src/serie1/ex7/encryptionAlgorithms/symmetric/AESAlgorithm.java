package serie1.ex7.encryptionAlgorithms.symmetric;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

public class AESAlgorithm implements ISymmetricAlgorithm {
    private static final String ALGORITHM = "AES";
    private static final int AES_KEY_SIZE = 128;    // in bits
    private byte[] iv;
    private Cipher cipher;

    @Override
    public byte[] getIV() {
        return iv;
    }

    public AESAlgorithm(byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.iv = iv;
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    public AESAlgorithm() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    @Override
    public SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        kgen.init(AES_KEY_SIZE);
        return kgen.generateKey();
    }

    @Override
    public byte[] encrypt(byte[] value, SecretKey key) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        this.iv = cipher.getIV();
        return cipher.doFinal(value);
    }

    @Override
    public byte[] decrypt(byte[] value, SecretKey key) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(value);
    }

    @Override
    public String getMode() {
        return ALGORITHM;
    }
}
