package it.polimi.ingsw.SOCKET.ProvaOggettiAR;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 12345;

        try {
            Socket socket = new Socket(host, port);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Oggetto da inviare al server
            MyMessage objectToSend = new MyMessage("ciao"); // Modifica questo con l'oggetto che desideri inviare

            // Invio dell'oggetto al server
            outputStream.writeObject(objectToSend);
            outputStream.flush();

            // Ricezione della risposta dal server
            boolean response = inputStream.readBoolean();
            System.out.println("Server response: " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}

class MyMessage implements Serializable {
    private final String content;

    public MyMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

