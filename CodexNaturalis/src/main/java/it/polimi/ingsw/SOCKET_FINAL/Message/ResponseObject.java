package it.polimi.ingsw.SOCKET_FINAL.Message;

import java.io.Serializable;
import java.util.ArrayList;
/*
da eliminare probabilmente no usage
 */
public class ResponseObject implements Serializable {

    public String string_message;
    public boolean bool_message;

    public Integer int_message;

    public ArrayList<Object> array_list_message;

    public ResponseObject() {
    }

    public String getString_message() {
        return string_message;
    }

    public boolean isBool_message() {
        return bool_message;
    }

    public Integer getInt_message() {
        return int_message;
    }

    public ArrayList<Object> getArray_list_message() {
        return array_list_message;
    }

    public void setString_message(String string_message) {
        this.string_message = string_message;
    }

    public void setBool_message(boolean bool_message) {
        this.bool_message = bool_message;
    }

    public void setInt_message(Integer int_message) {
        this.int_message = int_message;
    }

    public void setArray_list_message(ArrayList<Object> array_list_message) {
           this.array_list_message = array_list_message;
    }
}
