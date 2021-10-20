package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Main {

    private ServerSocket server;
    int port = 9933;

    public Main() {
        try {

            server = new ServerSocket(port);
            server.setSoTimeout(100000);
            run();

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    void run(){
        while (true){
            try {

                System.out.println("Waiting for client at " + server.getLocalPort());
                Socket client = server.accept();
                client.setSoTimeout(10000);

                DataInputStream input = new DataInputStream(client.getInputStream());
                System.out.println(input.readUTF());

                DataOutputStream output = new DataOutputStream(client.getOutputStream());
                output.writeUTF("You are connected!");
                while (true) {
                    try {

                        System.out.println(input.readUTF());

                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }

            } catch(Exception e){
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Server");
        new Main();
    }

}
