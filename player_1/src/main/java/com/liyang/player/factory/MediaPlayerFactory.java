package com.liyang.player.factory;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MediaPlayerFactory {

    public static MediaPlayer createMediaPlayer(String filePath) {
       
        Media media = new Media(new File(filePath).toURI().toString());
        
        
        return new MediaPlayer(media);
    }



}