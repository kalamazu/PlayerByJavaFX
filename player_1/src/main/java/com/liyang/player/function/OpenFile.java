package com.liyang.player.function;

import com.liyang.player.controller.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class OpenFile {
    private String path;
    private ListView<String> list;
    private PlayMusic playMusic;
    

    public OpenFile(Controller controller,Stage stage){
        openMusicFolder(controller,stage);
        closeMusicFolder(controller,stage);
        
    }
    void openMusicFolder(Controller controller,Stage stage){
        MenuItem openFile=controller.getOpenItem();
        list=controller.getListOfMusic();
        openFile.setOnAction(event -> {              
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("选择文件夹");
                File selectedDirectory = directoryChooser.showDialog(stage);
                if (selectedDirectory != null) {
                    if(list!=null&&playMusic==null){list.getItems().clear();}
                    if(playMusic!=null&&playMusic.getMediaPlayer()!=null){                           
                        playMusic.getMediaPlayer().stop();                          
                    }
                    this.path=selectedDirectory.getAbsolutePath();
                    initiaMusicFolder(path);
                    playMusic=new PlayMusic(controller,path);
                } else {
                    System.out.println("用户取消了选择");
                }                
                });
    }
    
    void closeMusicFolder(Controller controller,Stage stage){
        MenuItem closeFile=controller.getCloseItem();
        closeFile.setOnAction(event -> {
            if(list!=null){
                list.getItems().clear();
            }
            if(playMusic!=null){
                playMusic.closeMediaPlayer();
            }
        });
    }

    void initiaMusicFolder(String path){
        String musicFolderPath = path;
        File musicFolder = new File(musicFolderPath);//创建文件指针
        ObservableList<String> musicFileNames = FXCollections.observableArrayList();//创建专用列表
        if (musicFolder.exists() && musicFolder.isDirectory()) {
            // 音乐文件夹存在且是一个文件夹
            // 这里可以加载文件夹中的音乐文件并显示在界面上
            File[] musicFiles = musicFolder.listFiles();
            if (musicFiles != null) {
                for (File musicFile : musicFiles) {
                    if (musicFile.isFile()) {
                        musicFileNames.add(musicFile.getName());
                    }
                }
            }
        } else {
            // 音乐文件夹不存在或者不是一个文件夹
            System.out.println("Invalid music folder path: " + musicFolderPath);
        }
        list.setItems(musicFileNames);//为ListView设置内容
       
    }

}
