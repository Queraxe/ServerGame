package Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FrameJ {

    String serverIP = "92.75.167.95";

    public  FrameJ(){
        try {

            Socket client = new Socket(serverIP, 9933);

            DataOutputStream output = new DataOutputStream(client.getOutputStream());
            output.writeUTF("Hi, I am " + client.getLocalAddress());

            DataInputStream input = new DataInputStream(client.getInputStream());
            System.out.println("input: " + input.readUTF());
            client.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }

}
