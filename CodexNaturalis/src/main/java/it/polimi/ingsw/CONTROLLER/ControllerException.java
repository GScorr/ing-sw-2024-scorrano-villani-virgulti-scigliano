package it.polimi.ingsw.CONTROLLER;
import com.google.gson.*;

public class ControllerException extends RuntimeException {

    private int id;
    private String message;

    public ControllerException(int id, String message) {
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