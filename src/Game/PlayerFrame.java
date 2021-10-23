package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class PlayerFrame {

    JFrame frame;
    String serverIP = "92.75.167.95";
    int port = 9933;
    private Socket socket;
    int playerID;
    int width = 600;
    int height = 400;
    private PlayerSprite me, enemy;
    private DrawingComponent dc;
    private Timer animationTimer;
    private boolean up, down, left, right;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;

    public PlayerFrame(){
        System.out.println("=== PLAYER ===");
        up = false;
        down = false;
        left = false;
        right = false;
    }

    public void GUI(){
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("Player #" + playerID);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        dc = new DrawingComponent();
        frame.add(dc);

        frame.setVisible(true);

        setUpAnimationTimer();
        setUpKeyListener();
    }

    public void createPlayers(){
        if (playerID == 1){
            me = new PlayerSprite(100,200, 50, Color.blue);
            enemy = new PlayerSprite(200,200,50, Color.red);
        } else {
            enemy = new PlayerSprite(100,200, 50, Color.blue);
            me = new PlayerSprite(200,200,50, Color.red);
        }
    }

    private void setUpAnimationTimer(){
        int interval = 10;
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double speed = 5;

                if (up) {
                    me.moveV(-speed);
                } if (down){
                    me.moveV(speed);
                } if (left){
                    me.moveH(-speed);
                } if (right){
                    me.moveH(speed);
                }
                dc.repaint();
            }
        };
        animationTimer = new Timer(interval, al);
        animationTimer.start();
    }

    private void setUpKeyListener(){
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_UP) {
                    up = true;
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    down = true;
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    left = true;
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    right = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_UP) {
                    up = false;
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    down = false;
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    left = false;
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    right = false;
                }
            }
        };
        frame.addKeyListener(kl);
        frame.setFocusable(true);
    }


    public void connect(){
        try {
            socket = new Socket(serverIP, port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            playerID = in.readInt();
            System.out.println("You are Player #" + playerID);

            if (playerID == 1){
                System.out.println("Waiting for Player #2");
            }
            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);
            rfsRunnable.waitForStartMsg();

        } catch (IOException ex){
            System.out.println("IOException from connect()");
        }
    }

    private class DrawingComponent extends JComponent{
        protected void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D) g;
            me.drawSprite(g2d);
            enemy.drawSprite(g2d);
        }
    }


    private class ReadFromServer implements Runnable{
        private DataInputStream dataIn;

        public ReadFromServer(DataInputStream in){
            dataIn = in;
            System.out.println("RFS Runnable created");
        }

        @Override
        public void run() {
            System.out.println("run");
            try {
                while (true) {
                    if (enemy != null) {
                        enemy.setX(dataIn.readDouble());
                        enemy.setY(dataIn.readDouble());
                    }
                }
            } catch (IOException ex) {
                System.out.println("IOException from RFS run()");
            }
        }
        public void waitForStartMsg() {
            try {
                String startMsg = dataIn.readUTF();
                System.out.println("Message from server: " + startMsg);

                Thread readThread = new Thread(rfsRunnable);
                Thread writeThread = new Thread(wtsRunnable);
                readThread.start();
                writeThread.start();

            } catch (IOException ex){
                System.out.println("IOException from waitForStartMsg()");
            }
        }
    }
    private class WriteToServer implements Runnable{
        private DataOutputStream dataOut;

        public WriteToServer(DataOutputStream out){
            dataOut = out;
            System.out.println("WTS Runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (me != null) {
                        dataOut.writeDouble(me.getX());
                        dataOut.writeDouble(me.getY());
                        dataOut.flush();
                    }

                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        System.out.println("InterruptedException from WTS run()");
                    }
                }

            } catch (IOException ex) {
                System.out.println("IOException from WTS run()");
            }
        }
    }

    public static void main(String[] args) {
        PlayerFrame pf = new PlayerFrame();

        pf.createPlayers();
        pf.GUI();
        pf.connect();


    }

}
