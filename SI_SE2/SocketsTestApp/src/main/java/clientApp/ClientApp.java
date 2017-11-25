package clientApp;

import javax.net.ssl.*;
import java.io.*;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;

public class ClientApp {
    private static final String PASSWORD = "changeit";
    private static final String TRUSTSTORE_PATH = "res\\CA1.jks";
    private String hostName, certPath;
    private int port;

    public ClientApp(String hostName, int port, String certPath) {
        this.certPath = certPath;
        this.hostName = hostName;
        this.port = port;
    }

    public void startClient() throws Exception {
        SSLSocketFactory sf = loadSSLContext(certPath).getSocketFactory();
        while (true){
            try {
                SSLSocket echoSocket = (SSLSocket) sf.createSocket(hostName, port);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Hello user!! Write Something....");
                String userInput = stdIn.readLine();
                out.println(userInput);
                String res = readResponse(in) ;
                echoSocket.close();
                System.out.println(res);
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + hostName);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " +
                        hostName);
                System.exit(1);
            }
        }
    }

    private static String readResponse(BufferedReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            sb.append(line).append("\n");
        return sb.toString();
    }

    private static SSLContext loadSSLContext(String clientKeyStore) throws Exception {
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(TRUSTSTORE_PATH), PASSWORD.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(trustStore);

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(clientKeyStore), PASSWORD.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, "changeit".toCharArray());

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        return sc;
    }

    public static void main(String[] args) throws Exception {
        ClientApp clientApp = new ClientApp(args[0], Integer.parseInt(args[1]), args[2]);
        clientApp.startClient();
    }
}
