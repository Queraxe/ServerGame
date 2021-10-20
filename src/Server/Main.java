package Server;

import java.io.*;
import java.net.*;

public class Main {

    Socket socket;
    InputStreamReader inputStreamReader;
    OutputStreamWriter outputStreamWriter;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    ServerSocket serverSocket;
    int port = 9933;

    public Main() throws IOException {

        serverSocket = new ServerSocket(port);
        System.out.println("ServerSocket: " + serverSocket);

        while (true){

            try {

                socket = serverSocket.accept();
                System.out.println("New client online: " + socket);

                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                while (true) {

                    String msgFromClient = bufferedReader.readLine();
                    System.out.println("Client: " + msgFromClient);

                    bufferedWriter.write("MSG received");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    if (msgFromClient.equalsIgnoreCase("disconnect")) {
                        break;
                    }
                }

                socket.close();
                inputStreamReader.close();
                outputStreamWriter.close();
                bufferedWriter.close();
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        System.out.println("Server");
        new Main();
    }

}
