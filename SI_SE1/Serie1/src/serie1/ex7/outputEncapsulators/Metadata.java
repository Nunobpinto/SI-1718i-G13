package serie1.ex7.outputEncapsulators;

import serie1.ex7.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Metadata {
    private byte[] IV;
    private byte[] symmetricKey;
    private byte[] fileName;

    private Metadata(byte[] IV, byte[] symmetricKey, byte[] fileName){
        this.IV = IV;
        this.symmetricKey = symmetricKey;
        this.fileName = fileName;
    }

    public byte[] getFileName() {
        return fileName;
    }

    public byte[] getIV() {
        return IV;
    }

    public byte[] getSymmetricKey() {
        return symmetricKey;
    }

    @Override
    public String toString(){
        return "First -> IV \n Second -> Simmetric Key \n " +this.IV + " \n " + this.symmetricKey;
    }

    public static Metadata create(File metadataFile) throws Exception {
        byte[] iv;
        byte[] key;
        byte[] fileName;
        try( BufferedReader buf = new BufferedReader(new FileReader(metadataFile))) {
            String[] encodedData = buf.readLine().split("\\.");
            iv = Utils.Base64Decoder(encodedData[0]);
            key = Utils.Base64Decoder(encodedData[1]);
            fileName = Utils.Base64Decoder(encodedData[2]);

        } catch ( Exception e ) {
            throw new Exception("Error while reading metadata file" + e.getMessage());
        }
        return new Metadata(iv, key, fileName);
    }
}
