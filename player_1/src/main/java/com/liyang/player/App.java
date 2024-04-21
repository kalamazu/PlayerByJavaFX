package com.liyang.player;
import com.liyang.player.controller.Controller;
import com.liyang.player.function.OpenFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author kala
 * @version 1.0
 * @date 2021/3/2 10:27
 * @projectName: JavaFxDemo
 * @className: App
 * @package: com.liyang.player
 * @github: 
 * @blog: 
 */
public class App extends Application
{
    public static void main( String[] args )
    {
            launch(args);
    }

    @Override
    public void init() throws Exception {
        
       
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("firstui.fxml"));
        Controller controller=new Controller();
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        stage.setTitle("桌面应用");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("image/t.jpg"));
        stage.setScene(scene);
        
        new OpenFile(controller, stage);
        
        stage.show();
    }



    @Override
    public void stop() throws Exception {
      
        super.stop();
    }
    
}
