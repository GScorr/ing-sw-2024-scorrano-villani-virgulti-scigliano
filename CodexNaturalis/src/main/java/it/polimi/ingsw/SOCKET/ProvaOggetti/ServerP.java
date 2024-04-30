package it.polimi.ingsw.SOCKET.ProvaOggetti;
/*
import it.polimi.ingsw.SOCKET.Message.CreatePlayerMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerP {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = 12345; // Replace with your server port

        // Create server socket
        ServerSocket serverSocket = new ServerSocket(port);

        // Wait for client connection
        Socket clientSocket = serverSocket.accept();

        // Receive object from client
        InputStream in = clientSocket.getInputStream();
        ObjectInputStream objectIn = new ObjectInputStream(in);
        MyMessage receivedMessage = (MyMessage) objectIn.readObject();
        CreatePlayerMessage receivedMessage_2 = (CreatePlayerMessage)  objectIn.readObject();
        // Process the received message
        System.out.println("Received message from client: " + receivedMessage.getContent());
        System.out.println("Received message_2 from client: " + receivedMessage_2.nome);
        // (Optional) Send response to client
        // ...

        // Close connection
        objectIn.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}


 */
