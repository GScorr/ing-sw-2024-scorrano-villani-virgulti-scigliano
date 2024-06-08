package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;

public class GameController2 extends GenericSceneController {

    @FXML
    private GridPane gameGrid;

    @FXML
    public void startInitialize() throws IOException {
        int count = 1;
        int tmp = 0;
        while(count<=client.getMiniModel().getMyGameField().card_inserted){
        // Initialize the 45x45 grid with images
        for (int i = 0; i < 45; i++) {
            for (int j = 0; j < 45; j++) {
                    if(client.getMiniModel().getMyGameField().getCell(i,j, Constants.MATRIXDIM).getOrder_above()==count) {
                        tmp = count;
                        count = 1500;
                        System.out.println("In posizione " + i + " " + j + "sono sopra e ho count corretto");
                        if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().flipped) {
                            System.out.println("Inserisco da flipped in " + i + j);
                            addImageToGrid(i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().back_side_path);
                        } else {
                            System.out.println("Inserisco da non flipped in " + i + j);
                            addImageToGrid(i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().front_side_path);

                        }


                    }
                    else if(client.getMiniModel().getMyGameField().getCell(i,j, Constants.MATRIXDIM).getOrder_below()==count){
                        tmp = count;
                        count = 1500;
                        System.out.println("In posizione " + i + " " + j + "sono sotto e ho count corretto");
                        if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().flipped) {
                            System.out.println(client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().back_side_path);
                            System.out.println("Inserisco da flipp in " + i + j);
                            addImageToGrid(i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().back_side_path);

                        } else {
                            System.out.println("Inserisco da non flipp in " + i + j);
                            System.out.println(client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().front_side_path);
                            addImageToGrid(i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCardDown().front_side_path);
                        }
                    }
                    if(client.getMiniModel().getMyGameField().getCell(i,j, Constants.MATRIXDIM).getOrder_below()!=0) {
                        System.out.println(client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getOrder_below() + "orderbelow in posizione " + i + " " + j);
                    }
                }
            }
            count=tmp;
        count++;
        }
    }

    private void addImageToGrid(int row, int col, String imagePath) {
        File file = new File(imagePath);
        Image image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(49.65 * 2);
        imageView.setFitHeight(33.1 * 2);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(imageView);

        gameGrid.add(stackPane, col, row, 2, 2); // Occupa 2 colonne e 2 righe
    }



}