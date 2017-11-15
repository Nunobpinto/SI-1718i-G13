package serverApp;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class ServerApp {

    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        SSLServerSocketFactory ssf = ssf = loadSSLContext(args[1]).getServerSocketFactory();
        try (
                ServerSocket serverSocket = ssf.createServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    private static SSLContext loadSSLContext(String keystoreFileName) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keystoreFileName), "changeit".toCharArray());

        setTrustedCert(ks);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(ks, "changeit".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(ks);

        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);
        return sc;
    }

    private static void setTrustedCert(KeyStore keystore) throws CertificateException, KeyStoreException, FileNotFoundException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        KeyStore.Entry newEntry = new KeyStore.TrustedCertificateEntry(certFactory.generateCertificate(new FileInputStream("res\\CA1.cer")));
        keystore.setEntry("CA1", newEntry, null);
    }
}
