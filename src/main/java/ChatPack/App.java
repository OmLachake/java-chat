package ChatPack;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println( "SERVER -- OM -- " );
        ServerSocket serverSocket = new ServerSocket(1234);

        Server server = new Server(serverSocket);
        server.Start();
    }
}
