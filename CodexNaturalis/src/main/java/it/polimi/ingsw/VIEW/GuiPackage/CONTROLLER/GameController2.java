package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import it.polimi.ingsw.CONSTANTS.Constants;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;

public class GameController2 extends GenericSceneController {

    @FXML
    private GridPane gameGrid;

    @FXML
    public void startInitialize() throws IOException {
        // Initialize the 45x45 grid with images
        for (int i = 0; i < 44; i++) {
            for (int j = 0; j < 44; j++) {
                if(client.getMiniModel().getMyGameField().getCell(i,j, Constants.MATRIXDIM).getCard().back_side_path != null) {
                    if(client.getMiniModel().getMyGameField().getCell(i,j, Constants.MATRIXDIM).getCard().equals(client.getMiniModel().getMyGameField().getCell(i+1,j+1, Constants.MATRIXDIM).getCard())) {
                        System.out.println(client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard() + " " + i + " " + j);
                        if (client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().flipped) {
                            addImageToGrid(i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().back_side_path);
                        } else {

                            addImageToGrid(i, j, client.getMiniModel().getMyGameField().getCell(i, j, Constants.MATRIXDIM).getCard().front_side_path);
                        }
                    }
                }
            }
        }
    }

    private void addImageToGrid(int row, int col, String imagePath) {

        File file = new File(imagePath);
        Image image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20 * 2);
        imageView.setFitHeight(20 * 2);
        imageView.setPreserveRatio(true);

        Pane pane = new Pane(imageView);
        pane.setPrefSize(20 * 2, 20 * 2);
        pane.setMinSize(20 * 2, 20 * 2);
        pane.setMaxSize(20 * 2, 20 * 2);

        gameGrid.add(pane, col, row);
    }
}