package ChatPack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    // Read input from clients
    private BufferedReader reader;

    // Send data to other clients
    private BufferedWriter writer;
    
    private String username;


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = reader.readLine();

            clientHandlers.add(this);

            BroadcastMessage("SERVER : " + username + "has entered the chat.");
        } catch (IOException e) {
            closeEverything(socket,reader,writer);
        }
    }


    @Override
    public void run(){
        String messageReceivedFromClient;


        while(socket.isConnected()){
            try{
                messageReceivedFromClient = reader.readLine();
                BroadcastMessage(messageReceivedFromClient);
            }catch (IOException e) {
                closeEverything(socket,reader,writer);
                break;
            }
        }
    }


    public void BroadcastMessage(String messageToSend){
        for(ClientHandler cHandler:clientHandlers){
            try{
                if(!cHandler.username.equals(username)){
                    cHandler.writer.write(messageToSend);
                    cHandler.writer.newLine();
                    cHandler.writer.flush();
                }
            }
            catch (IOException e) {
                closeEverything(socket,reader,writer);
                break;
            }
        }
    }


    public void RemoveClientHandler(){
        clientHandlers.remove(this);
        BroadcastMessage("SERVER : " + username + " has left the chat.");
    }


    public void closeEverything(Socket s, BufferedReader r, BufferedWriter w){
        RemoveClientHandler();
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
    

}
