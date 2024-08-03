package ChatPack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    String username;


    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        }catch(IOException e){
            closeEverything(socket,reader,writer);
        }
    }

    public void SendMessage(){
        Scanner scanner = new Scanner(System.in);
        try{
            writer.write(username);
            writer.newLine();
            writer.flush();


            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                writer.write(username + " : " + messageToSend);
                writer.newLine();
                writer.flush();
            }
        }catch (Exception e) {
            closeEverything(socket,reader,writer);
            scanner.close();

        }
    }

    public void ListenMessages(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                String messageFromServer;
                while(socket.isConnected()){
                    try{
                        messageFromServer = reader.readLine();
                        System.out.println(messageFromServer);
                    }catch(IOException e){
                        closeEverything(socket, reader, writer);
                    }
                }
            }
        }).start();
    }


    public void closeEverything(Socket s, BufferedReader r, BufferedWriter w){
        try{
            if(s!=null){
                s.close();
            }

            if(r!=null){
                r.close();
            }


            if(w!=null){
                w.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Username :  ");
        String username = scanner.nextLine();


        try{
            Socket socket = new Socket("localhost",1234);
            Client client  = new Client(socket, username);

            client.ListenMessages();
            client.SendMessage();

        }catch(Exception e){
            e.printStackTrace();
            scanner.close();
        }

    }

}
