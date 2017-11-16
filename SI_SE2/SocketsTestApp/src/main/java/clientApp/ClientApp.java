package clientApp;

import javax.net.ssl.*;
import java.io.*;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;

public class ClientApp {

    public static void main(String[] args) throws Exception {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        SSLSocketFactory sf = loadSSLContext(args[2]).getSocketFactory();
        try (
                SSLSocket echoSocket = (SSLSocket) sf.createSocket(hostName, portNumber);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader( new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            echoSocket.startHandshake();
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    private static SSLContext loadSSLContext(String clientKeyStore) throws Exception {
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("res\\CA1.jks"), "changeit".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(trustStore);

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(clientKeyStore), "changeit".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, "changeit".toCharArray());

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        return sc;
    }
}
