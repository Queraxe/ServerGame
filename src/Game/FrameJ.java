package Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Struct;
import java.util.Scanner;

public class FrameJ {

    String serverIP = "92.75.167.95";

    Scanner scanner;

    public  FrameJ(){
        try {

            Socket client = new Socket(serverIP, 9933);
            client.setSoTimeout(100000);

            DataOutputStream output = new DataOutputStream(client.getOutputStream());
            output.writeUTF(client.getLocalAddress()+ " connected!");

            DataInputStream input = new DataInputStream(client.getInputStream());
            System.out.println("got sent: " + input.readUTF());

            while (true){
                try {
                    scanner = new Scanner(System.in);
                    output.writeUTF(scanner.nextLine());
                } catch (Exception e){
                    e.printStackTrace();
                    break;
                }
            }



        } catch (IOException e){
            e.printStackTrace();
        }

    }

}
