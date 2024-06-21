package it.polimi.ingsw.VIEW.GuiPackage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class BackgroundMusic {

    private static MediaPlayer mediaPlayer;

    public static void initialize() {
        if (mediaPlayer == null) {
            String musicFile = BackgroundMusic.class.getResource("/background.mp3").toString(); // Percorso del file musicale nel classpath
            Media sound = new Media(musicFile);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop continuo
            mediaPlayer.play();
        }
    }

    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}