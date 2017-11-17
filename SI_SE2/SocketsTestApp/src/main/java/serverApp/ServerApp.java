package serverApp;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;

public class ServerApp {
    private static final int HTTP_PORT = 5050;
    private static final String PASSWORD = "changeit";

    public static void main(String[] args) throws Exception {
        ServerApp server = new ServerApp();
        server.startServer();
    }

    public void startServer() throws Exception {
        SSLServerSocketFactory ssf = loadSSLContext().getServerSocketFactory();
        try (SSLServerSocket sslServerSocket = (SSLServerSocket) ssf.createServerSocket(HTTP_PORT)) {
            sslServerSocket.setNeedClientAuth(true);
            while(true) {
                Socket clientSocket = sslServerSocket.accept();
                processRequest(clientSocket);
            }
        }
    }

    private void processRequest(Socket clientSocket) {
        try(
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())
        ) {
            char[] buf = new char[1024];
            int size =  in.read(buf);
            char[] newBuffer = Arrays.copyOf(buf,size);
            String request = new String(newBuffer);
            System.out.println("Received from Client: " + request);

            out.writeBytes("HTTP/1.1 200 OK\r\n");
            out.writeBytes("Content-Type: text/plain\r\n");
            out.writeBytes("Content-Length: " + request.getBytes().length + "\r\n");
            out.writeBytes("\r\n");
            out.writeBytes(request + "\r\n");
            out.flush();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + HTTP_PORT + " or listening for a connection");
            e.printStackTrace();
        }
    }

    private static SSLContext loadSSLContext() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream("res\\localhost.pfx"), PASSWORD.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
        kmf.init(keyStore, PASSWORD.toCharArray());

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("res\\CA1.jks"), PASSWORD.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(trustStore);

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        return sc;
    }
}
