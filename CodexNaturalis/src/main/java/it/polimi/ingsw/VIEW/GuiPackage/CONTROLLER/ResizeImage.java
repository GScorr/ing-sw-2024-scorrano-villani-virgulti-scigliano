package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ResizeImage {
    public static Image resizeImage(Image originalImage, int targetWidth, int targetHeight) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        WritableImage resizedImage = new WritableImage(targetWidth, targetHeight);
        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = resizedImage.getPixelWriter();

        for (int y = 0; y < targetHeight; y++) {
            for (int x = 0; x < targetWidth; x++) {
                int srcX = (int) ((double) x / targetWidth * width);
                int srcY = (int) ((double) y / targetHeight * height);
                Color color = pixelReader.getColor(srcX, srcY);
                pixelWriter.setColor(x, y, color);
            }
        }

        return resizedImage;
    }
}
