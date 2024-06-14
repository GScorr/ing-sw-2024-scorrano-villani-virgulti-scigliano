package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.*;

import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class EndGameController extends GenericSceneController{

    @FXML
    private TableView<Player> leaderboard;

    @FXML
    private TableColumn<Player, String> playerNameColumn;

    @FXML
    private TableColumn<Player, Integer> playerScoreColumn;


    @FXML
    private TableColumn<Player, Integer> positionColumn;

    private Map<String,Integer> player_to_position = new HashMap<>();

    private int lastindex = 1;

    @FXML
    public void startInitialize() throws IOException {



        List<GameField> gameFields = client.getMiniModel().getGame_fields();
        List<Player> finalStanding = new ArrayList<>();
        for (GameField g : gameFields) {
            finalStanding.add(g.getPlayer());
        }

        Collections.sort(finalStanding, Comparator.comparingInt(Player::getPlayerPoints).reversed());

        int idx = 0;
        for(Player p: finalStanding){

            if(idx != 0){
                if (p.getPlayerPoints() == finalStanding.get(idx-1).getPlayerPoints()) {
                    player_to_position.put(p.getName(),lastindex);
                    System.out.println("a " + p.getName());
                }
                else{
                    player_to_position.put(p.getName(),idx+1);
                    lastindex = idx+1;
                }
            }
            else{
                player_to_position.put(p.getName(),1);
            }
            idx++;
        }




        // Sort players by points in descending order

        leaderboard.getItems().addAll(finalStanding);

        positionColumn.setCellValueFactory(cellData -> {
            String playerName = cellData.getValue().getName();
            Integer position = player_to_position.get(playerName);
            if (position != null) {
                return new SimpleIntegerProperty(position).asObject();
            } else {
                return new SimpleIntegerProperty(0).asObject(); // o qualsiasi valore di default
            }
        });
        playerNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        playerScoreColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getPlayerPoints()).asObject());


        int myPlayerIndex = player_to_position.get(client.getMiniModel().getMy_player().getName());
        System.out.println(myPlayerIndex);

        // Se il giocatore attuale è primo, mostra Congratulations e animazione
        if (myPlayerIndex == 1) {
            showCongratulationsAnimation();
        }
    }





    private void showCongratulationsAnimation() {
        // Creazione del testo di congratulazioni
        Text congratulationsText = new Text("Congratulations!");
        congratulationsText.setFill(Color.DARKGOLDENROD);
        congratulationsText.setFont(Font.font("Georgia", FontWeight.BOLD, 54));

        // Posizionamento del testo al di fuori del leaderboard
        congratulationsText.setTranslateY(-50);
        congratulationsText.setOpacity(0);

        // Aggiunta del testo al contenitore principale
        ((Pane) leaderboard.getParent()).getChildren().add(congratulationsText);

        // Animazione di scorrimento e fade-in del testo
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), congratulationsText);
        translateTransition.setFromX(-200);
        translateTransition.setToX(150);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), congratulationsText);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        // Combina le transizioni
        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, fadeTransition);
        parallelTransition.setCycleCount(1);

        // Avvia l'animazione
        parallelTransition.play();
    }


}