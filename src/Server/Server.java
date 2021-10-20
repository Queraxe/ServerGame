package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Server {
    private ServerSocket server;
    int port = 9933;


    public Server() {
        try {

            server = new ServerSocket(port);
            server.setSoTimeout(100000);
            run();

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        while (true){
            try {

                System.out.println("Waiting for client at " + server.getLocalPort());
                Socket client = server.accept();

                DataInputStream input = new DataInputStream(client.getInputStream());
                System.out.println(input.readUTF());

                DataOutputStream output = new DataOutputStream(client.getOutputStream());
                output.writeUTF("Hello Boss! You are connected!");

                client.close();

            } catch(Exception e){
                e.printStackTrace();
                break;
            }


        }
    }

}
