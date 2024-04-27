package it.polimi.ingsw.SOCKET.ProvaOggettiAR;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

                // Ricezione dell'oggetto dal client
                MyMessage receivedObject = (MyMessage) inputStream.readObject();
                System.out.println("Received object from client: " + receivedObject);

                // Simuliamo un controllo sull'oggetto ricevuto
                boolean objectReceivedSuccessfully = receivedObject != null; // Modifica questa logica secondo le tue esigenze

                // Invio della risposta al client
                outputStream.writeBoolean(objectReceivedSuccessfully);
                outputStream.flush();

                clientSocket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

