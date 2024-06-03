module PSP31 {

    requires java.management;
    requires java.rmi;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires com.google.gson;
    exports it.polimi.ingsw.MODEL;
    exports it.polimi.ingsw.VIEW;
    exports it.polimi.ingsw.SOCKET_FINAL;
    exports it.polimi.ingsw.RMI_FINAL;

    exports it.polimi.ingsw.CONSTANTS;
    exports it.polimi.ingsw.CONTROLLER;


    //può essere che al posto di questa linea bisogna aggiungere tutte le varie cartelle come:
    // exports it.polimi.ingsw

    exports it.polimi.ingsw;
    exports it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;
    opens it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER to javafx.fxml;
    opens it.polimi.ingsw.VIEW to javafx.fxml;
    exports it.polimi.ingsw.RMI_FINAL.MESSAGES;
    exports it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;
    exports it.polimi.ingsw.VIEW.GuiPackage;
    opens it.polimi.ingsw.VIEW.GuiPackage to javafx.fxml;

}