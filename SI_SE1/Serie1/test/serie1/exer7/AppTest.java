package serie1.exer7;

import org.junit.Test;
import serie1.ex7.App;

public class AppTest {

    @Test
    public void testPdfEncryptionAndDecryptionTimeAES() throws Exception {
        App appEnc = new App("encrypt", "AES", "res\\exer7\\SI_1718i_LI51N_SE1.pdf", "res\\exer7\\certificates\\Alice_1.cer", "res\\exer7\\certificates\\CA1.jks");

        long start = System.nanoTime();
        appEnc.run();
        double elapsedTime = (System.nanoTime() - start) / 1000000;

        System.out.println(String.format("It took about %.2f milliseconds to encrypt with AES the file SI_1718i_LI51N_SE1.pdf",
                elapsedTime));

        App appDec = new App("decrypt", "AES", "res\\exer7\\cipherFile", "res\\exer7\\metadata", "res\\exer7\\certificates\\Alice_1.pfx", "changeit");

        start = System.nanoTime();
        appDec.run();
        elapsedTime = (System.nanoTime() - start) / 1000000;

        System.out.println(String.format("It took about %.2f milliseconds to decrypt with AES the file SI_1718i_LI51N_SE1.pdf",
                elapsedTime));
    }
}
