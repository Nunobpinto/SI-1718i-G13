package serverApp;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;

public class ServerApp {
    private static final int HTTP_PORT = 5050;

    public static void main(String[] args) throws Exception {
        ServerApp server = new ServerApp();
        server.startServer();
    }

    public void startServer() throws Exception {
        SSLServerSocketFactory ssf = loadSSLContext().getServerSocketFactory();
        try (SSLServerSocket sslServerSocket = (SSLServerSocket) ssf.createServerSocket(HTTP_PORT)) {
            sslServerSocket.setNeedClientAuth(false);
            while(true) {
                Socket clientSocket = sslServerSocket.accept();
                processRequest(clientSocket);
            }
        } catch (Exception e) {
            System.out.println("Exception caught when trying to listen on port " + HTTP_PORT + " or listening for a connection");
            e.printStackTrace();
        }
    }

    private void processRequest(Socket clientSocket) throws IOException {
        try(
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())
        ) {
            char []buf = new char[1024];
            int size =  in.read(buf);
            char [] newBuffer = Arrays.copyOf(buf,size);
            String request = new String(newBuffer);
            System.out.println("Received from Client: " + request);
            out.writeBytes("HTTP/1.1 200 OK\r\n");
            out.writeBytes("Content-Type: text/plain\r\n");
            out.writeBytes("Content-Length: " + request.getBytes().length + "\r\n");
            out.writeBytes("\r\n");
            out.writeBytes(request + "\r\n");
            out.flush();
        }
    }

    private static SSLContext loadSSLContext() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream("res\\localhost.pfx"), "changeit".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
        kmf.init(keyStore, "changeit".toCharArray());

        /*KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("res\\CA1.jks"), "changeit".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(trustStore);*/

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, new SecureRandom());
        return sc;
    }
}
