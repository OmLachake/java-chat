package ChatPack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket server_socket;

    Server(ServerSocket s){
        this.server_socket = s;
    }

    public void Start(){
        try{
            while(!server_socket.isClosed()){
                Socket socket = server_socket.accept();
                System.err.println("New Client has Connected");
                ClientHandler cHandler = new ClientHandler(socket);

                Thread thread = new Thread(cHandler);
                thread.start();


            }
        } catch (Exception e) {
            CloseServerSocket();
        }
    }


    public void CloseServerSocket(){
        try{
            if(server_socket!=null){
                server_socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
}
