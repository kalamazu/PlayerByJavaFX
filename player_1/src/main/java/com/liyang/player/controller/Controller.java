package com.liyang.player.controller;

import com.liyang.player.function.PlayMusic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;


public class Controller {
    private PlayMusic playMusic;


    private boolean isMaximized = false; 
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Label label;
    @FXML
    private Slider slider;
    @FXML
    private Slider vslider;
    @FXML
    private ImageView before;
    @FXML
    private ImageView next;  
    @FXML
    private ImageView play;
    @FXML
    private ListView<String> list;
    @FXML
    private AnchorPane Pane;
    @FXML
    private MenuItem openMusicFolder;
    @FXML
    private MenuItem closeMusicFolder;

public Slider getSlider(){
        return this.slider;
    }
public  Slider getVslider(){
        return this.vslider;
    }
public  ImageView getPlayButton(){
        return this.play;
    }
public ImageView getNextButton(){
    return this.next;
}
public ImageView getBeforeButton(){
    return this.before;
}
public ListView<String> getListOfMusic(){
    return this.list;
}
public Label getLabel(){
    return this.label;
}
public PlayMusic getPlayMusic(PlayMusic playMusic){
    return this.playMusic;
}
public MenuItem getOpenItem(){
    return this.openMusicFolder;
}
public MenuItem getCloseItem(){
    return this.closeMusicFolder;
}


@FXML
private void initialize(){
      
    }

@FXML
private void Close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


@FXML
private void Minimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

@FXML
private void Maxmize(ActionEvent event) {
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (isMaximized) {
            // 如果窗口已经最大化，就恢复原来的大小
            stage.setMaximized(false);
            isMaximized = false;
        } else {
            // 如果窗口没有最大化，就最大化窗口
            stage.setMaximized(true);
            isMaximized = true;
        }
    }
    
@FXML
private void onMousePressedForMove(MouseEvent event) {
    // 记录鼠标按下时的偏移量
    xOffset = event.getSceneX();
    yOffset = event.getSceneY();
}

@FXML
private void onMouseDraggedForMove(MouseEvent event) {
    // 获取当前窗口
    Stage stage = (Stage) Pane.getScene().getWindow();
    // 移动窗口
    stage.setX(event.getScreenX() - xOffset);
    stage.setY(event.getScreenY() - yOffset);
}

}

