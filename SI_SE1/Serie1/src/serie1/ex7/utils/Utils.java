package serie1.ex7.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Utils {

    public static String Base64Encode(byte[] value){
        return Base64.getUrlEncoder().encodeToString(value);
    }

    public static byte[] Base64Decoder(String value){
        return Base64.getUrlDecoder().decode(value);
    }

    public static void createOutputFile(String fileLocation, byte[] text) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileLocation);
        fos.write(text);
        fos.close();
    }
}
