package it.polimi.ingsw.VIEW.GuiPackage;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ClickableCardImageView extends ImageView {
    private int row;
    private int col;
    private PlayCard playCard;
    private int i;

    public ClickableCardImageView(Image image, PlayCard playCard, int row, int col, int i) {
        super(image);
        this.row = row;
        this.col = col;
        this.playCard = playCard;
        this.i = i;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public PlayCard getPlayCard() {
        return playCard;
    }

    public int getI() {
        return i;
    }
}