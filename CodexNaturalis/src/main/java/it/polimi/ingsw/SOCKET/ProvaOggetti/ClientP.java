package it.polimi.ingsw.SOCKET.ProvaOggetti;
/*
import it.polimi.ingsw.SOCKET.Message.CreatePlayerMessage;

import java.io.*;
import java.net.Socket;

public class ClientP {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String host = "localhost";
        int port = 12345; // Replace with your server port



        // Create a message object
        MyMessage message = new MyMessage("Hello from client!");
        CreatePlayerMessage message_2 = new CreatePlayerMessage("prova","tretre");

        // Connect to server
        Socket socket = new Socket(host, port);

        // Send object to server
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(out);
        objectOut.writeObject(message);
        objectOut.flush();

        objectOut.writeObject(message_2);
        objectOut.flush();

        // (Optional) Receive and handle response from server
        // ...

        // Close connection
        objectOut.close();
        out.close();
        socket.close();
    }
}

// Replace with your actual message class
class MyMessage implements Serializable {
    private final String content;

    public MyMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}


 */