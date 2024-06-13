package it.polimi.ingsw.SOCKET_FINAL;

import com.google.gson.Gson;

/**
 * SocketException class extends `RuntimeException` and includes an error ID and message.
 * It also provides a method to convert the exception object to a JSON string for potential network communication.
 */
public class SocketException extends RuntimeException {

    private int id;
    private String message;

    public SocketException(int id, String message) {
        super(message);
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getJSONErrorMessage() {
        Gson gson = new Gson();
        try {
            return gson.toJson(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert error message to JSON", e);
        }

    }

}
