package serie1.hashCalculator;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class HashCalculatorApp {
    private static final String FILE1_PATH = "res\\exer6\\BadApp.java";
    private static final String FILE2_PATH = "res\\exer6\\GoodApp.java";
    private static final int TIMES_EXECUTED = 5;
    private Random random;
    private MessageDigest sha;
    private int numOfBytes;
    private int inputtedBits;
    private File file1;
    private File file2;

    public HashCalculatorApp(int bits) throws NoSuchAlgorithmException {
        sha = MessageDigest.getInstance("SHA-1");
        this.random = new Random();
        file1 = new File(FILE1_PATH);
        file2 = new File(FILE2_PATH);
        numOfBytes = (int) Math.round(bits / 8.0);
        inputtedBits = bits;
    }

    public void determineHash() throws NoSuchAlgorithmException, IOException, DigestException {
        System.out.print("First " + numOfBytes + " bytes of SHA1(" + file1.getName() + ") = ");
        printHash(applyHash(file1));
        System.out.println();

        System.out.print("First " + numOfBytes + " bytes of SHA1(" + file2.getName() + ") = ");
        printHash(applyHash(file2));
        System.out.println();
    }

    public void findCollision() throws NoSuchAlgorithmException, IOException, DigestException {
        byte[] file1Hash = applyHash(file1);
        int[] executions = new int[TIMES_EXECUTED];
        for (int i = 0; i < TIMES_EXECUTED; i++) {
            File file2Copy = createCopyOf(file2, i);
            byte[] fileAuxHash;
            do {
                ++executions[i];
                Files.write(Paths.get(file2Copy.getPath()), generateRandomString().getBytes(), StandardOpenOption.APPEND);
                fileAuxHash = applyHash(file2Copy);
            }while(!Arrays.equals(file1Hash, fileAuxHash));
        }
        System.out.print(String.format("It takes on average %.2f iterations to find a matching %d byte hash code originated from another file equal to 0x",
                Arrays.stream(executions).average().getAsDouble(),
                numOfBytes));
        printHash(file1Hash);
    }

    public void findCollisionPair() throws IOException, DigestException, NoSuchAlgorithmException {
        List<byte[]> file1HashResults = new ArrayList<>();
        List<byte[]> file2HashResults = new ArrayList<>();
        int[] executions = new int[TIMES_EXECUTED];
        for (int i = 0; i < TIMES_EXECUTED; i++) {
            File file1Copy = createCopyOf(file1, i);
            File file2Copy = createCopyOf(file2, i);
            do {
                ++executions[i];
                byte[] rd = generateRandomString().getBytes();
                Files.write(Paths.get(file1Copy.getPath()), rd, StandardOpenOption.APPEND);
                Files.write(Paths.get(file2Copy.getPath()), rd, StandardOpenOption.APPEND);
                file1HashResults.add(applyHash(file1Copy));
                file2HashResults.add(applyHash(file2Copy));
            }while (file1HashResults.stream().noneMatch( bytes -> Arrays.equals(bytes, file2HashResults.get(file2HashResults.size()-1)) ) &&
                    file2HashResults.stream().noneMatch( bytes -> Arrays.equals(bytes, file1HashResults.get(file1HashResults.size()-1)) ) );
            file1HashResults.clear();
            file2HashResults.clear();
        }
        System.out.println(String.format("It takes on average %.2f iterations to find a matching %d byte hash code originated from two different files",
                Arrays.stream(executions).average().getAsDouble(),
                numOfBytes));
    }

    private String generateRandomString() {
        byte[] randomBytes = new byte[7];
        random.nextBytes(randomBytes);
        return "//" + new String(randomBytes, Charset.forName("UTF-8"));
    }

    private void printHash(byte[] hash) {
        for(int i = 0; i< hash.length; ++i) {
            System.out.print(String.format("%1$02x", hash[i]));
        }
    }

    private File createCopyOf(File file, int i) throws IOException {
        String filePath = "res\\exer6\\" + i + file.getName();
        Files.copy(file.toPath(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        return new File(filePath);
    }

    private byte[] applyHash(File file) throws IOException, NoSuchAlgorithmException, DigestException {
        FileInputStream fin = new FileInputStream(file);
        int nread = 0;
        byte buf[] = new byte[1024];
        while ((nread = fin.read(buf)) != -1) {
            sha.update(buf, 0, nread);
        }
        byte[] original = Arrays.copyOf(sha.digest(), numOfBytes);
        BigInteger toShift = new BigInteger(original);
        return numOfBytes * 8 - inputtedBits == 0 ? original : toShift.shiftRight(numOfBytes*8 - inputtedBits).toByteArray();
    }

    public static void main(String[] args) {
        try {
            HashCalculatorApp hashCalculatorApp = new HashCalculatorApp(Integer.parseInt(args[1]));
            switch (args[0]) {
                case "determineHash": hashCalculatorApp.determineHash(); break;
                case "findCollision": hashCalculatorApp.findCollision(); break;
                case "findCollisionPair": hashCalculatorApp.findCollisionPair(); break;
            }
        } catch (Exception e) {
            System.out.println("An error occurred!!");
            e.printStackTrace();
        }
    }
}
