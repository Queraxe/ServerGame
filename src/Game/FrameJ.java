package Game;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FrameJ {

    String serverIP = "92.75.167.95";

    Socket socket;
    InputStreamReader inputStreamReader;
    OutputStreamWriter outputStreamWriter;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    Scanner scanner;

    public  FrameJ(){
        try {

            socket = new Socket(serverIP, 9933);

            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            scanner = new Scanner(System.in);


            while (true){

                    String msgToSend = scanner.nextLine();
                    bufferedWriter.write(msgToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    System.out.println("Server" + bufferedReader.readLine());

                    if (msgToSend.equalsIgnoreCase("disconnect")){
                        break;
                    }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // do when while loop breaks
        finally {
            // close everything if not null
            try {
                if (socket != null) {
                    socket.close();
                }
                if (inputStreamReader != null){
                    inputStreamReader.close();
                }
                if (outputStreamWriter != null){
                    outputStreamWriter.close();
                }
                if (bufferedReader != null){
                    bufferedReader.close();
                }
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
