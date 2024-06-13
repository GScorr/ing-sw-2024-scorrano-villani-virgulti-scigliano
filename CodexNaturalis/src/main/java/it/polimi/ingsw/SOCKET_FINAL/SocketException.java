//package it.polimi.ingsw.SOCKET_FINAL;

import com.google.gson.Gson;

/*
probabilomente da eliminare
 */

/*
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

    /**
     *
     * @return
     */
/*
    public String getJSONErrorMessage() {
        Gson gson = new Gson();
        try {
            return gson.toJson(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert error message to JSON", e);
        }

    }
}

 */