package com.liyang.player.function;

import java.util.Random;
import java.util.ArrayList;
import com.liyang.player.controller.Controller;
import com.liyang.player.factory.MediaPlayerFactory;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class PlayMusic {
   
    private Label label; 
    private Slider slider; 
    private Slider vslider;
    private ImageView before;
    private ImageView next;
    private ImageView play;
    private ListView<String> list;
    private ArrayList<String> lastMusic;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying=false;
    private ChangeListener<Duration> timeListener;
    private String selectedItem;
    
    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }
    
    public PlayMusic(Controller controller,String path)
    {   
    slider=controller.getSlider();
    vslider=controller.getVslider();
    label=controller.getLabel();
    before=controller.getBeforeButton();
    play=controller.getPlayButton();
    next=controller.getNextButton();
    list=controller.getListOfMusic();
    
    lastMusic=new ArrayList<>();
    vslider.setValue(50);
    label.setText("暂未播放");
    playMusicByButtonHandler(path);
    playMusicByItemHandler(path);
    playLastMusicHandler(path);
    playNextMusicHandler(path);
    timeLine ();
}


void playMusicByButtonHandler(String path){
    
    play.setOnMouseClicked(event->{
        
        onMouseClickedUsual(event);   
        
        if(isPlaying){
            if(selectedItem==list.getSelectionModel().getSelectedItem()){
                mediaPlayer.pause();
                play.setImage(new Image("image/play.png"));
                isPlaying=false;
                
            }
            else{
                mediaPlayer.stop();
                selectedItem = list.getSelectionModel().getSelectedItem();
                label.setText("正在播放："+selectedItem);
                String musicPath = path + "/"+selectedItem;
                mediaPlayer = MediaPlayerFactory.createMediaPlayer(musicPath);
                initiaSlider();     
                playHistory(selectedItem);  
                mediaPlayer.play();
                mediaPlayer.setOnEndOfMedia(()->{
                    mediaPlayer.stop();
                    playRandomMusic(path);
                });
                isPlaying=true;
                play.setImage(new Image("image/stop.png"));
            }
            
        }
        
        else{
            if(mediaPlayer==null&&list.getSelectionModel().getSelectedItem()==null){
                label.setText("请选择歌曲");
            }
            else if(selectedItem!=list.getSelectionModel().getSelectedItem()){
                selectedItem = list.getSelectionModel().getSelectedItem();
                label.setText("正在播放："+selectedItem);
                String musicPath =path + selectedItem;
                mediaPlayer = MediaPlayerFactory.createMediaPlayer(musicPath);
                initiaVolume();                    
                initiaSlider();
                playHistory(selectedItem);
                mediaPlayer.play();
                mediaPlayer.setOnEndOfMedia(()->{
                    mediaPlayer.stop();
                    playRandomMusic(path);
                });
                isPlaying=true;
                play.setImage(new Image("image/stop.png"));
            }
            else{
                mediaPlayer.play();
                isPlaying=true;
                play.setImage(new Image("image/stop.png"));
                mediaPlayer.setOnEndOfMedia(()->{
                    mediaPlayer.stop();
                    playRandomMusic(path);
                });
            }
        }
});  
}

void playMusicByItemHandler(String path){
    list.setOnMouseClicked(event->{
        if(event.getClickCount()==2){   
            if(isPlaying){  
            mediaPlayer.stop();
            }  
            selectedItem = list.getSelectionModel().getSelectedItem();
            label.setText("正在播放："+selectedItem);   
            String musicPath =path+"/"+selectedItem;
            mediaPlayer = MediaPlayerFactory.createMediaPlayer(musicPath);               
            initiaVolume();
            playHistory(selectedItem);
            initiaSlider();
            mediaPlayer.play();
            play.setImage(new Image("image/stop.png"));
            isPlaying=true;
            mediaPlayer.setOnEndOfMedia(()->{
                mediaPlayer.stop();
                playRandomMusic(path);
            });
            
        }
     });
}

void playLastMusicHandler(String path){
    before.setOnMouseClicked(event->{
        onMouseClickedUsual(event);
        if(isPlaying){
            mediaPlayer.stop();
        }
        if(lastMusic.size()==0||lastMusic.size()==1){label.setText("没有上一首了");return;}
        
         selectedItem = lastMusic.get(lastMusic.size()-2);
        if(selectedItem==null){
           
            label.setText("没有记录");
        }
            if (selectedItem != null) {
                String musicPath = path+"/"+ selectedItem;
                mediaPlayer = MediaPlayerFactory.createMediaPlayer(musicPath);
                label.setText("正在播放："+selectedItem);
                initiaSlider();     
                initiaVolume();
                mediaPlayer.play();
                isPlaying=true;
                play.setImage(new Image("image/stop.png"));
            }
            
        
        lastMusic.remove(lastMusic.size()-1);
    });
}

void playNextMusicHandler(String path){
    
    next.setOnMouseClicked(event->{
        if(isPlaying){
            mediaPlayer.stop();
        }       
        onMouseClickedUsual(event);
        playRandomMusic(path);
    });
}

void playHistory(String selectedItem){
    lastMusic.add(selectedItem);
}

void initiaSlider(){

    mediaPlayer.setOnReady(() -> {
        // 获取歌曲的总时长
        Duration totalDuration = mediaPlayer.getTotalDuration();
        // 设置进度条的最大值为歌曲的总时长（以秒为单位）
        slider.setMax(totalDuration.toSeconds());
    });
    
    timeListener = (observable, oldValue, newValue) -> {
        slider.setValue(newValue.toSeconds());
    };//设置对slider的监听
    
    mediaPlayer.currentTimeProperty().addListener(timeListener);
      //为mediaPlayer添加监听

    slider.setOnMousePressed(event -> {
        // 获取鼠标在进度条上的位置
        mediaPlayer.currentTimeProperty().removeListener(timeListener);
        double mouseX = event.getX();
        double totalWidth = slider.getWidth();
        double percentage = mouseX / totalWidth;
        double newValue = percentage * (slider.getMax() - slider.getMin()) + slider.getMin();
        
        // 设置滑块位置，但不改变播放进度
        slider.setValue(newValue);
    }); 
    slider.setOnMouseReleased(event -> {
        // 获取鼠标在进度条上的位置
        double mouseX = event.getX();
        double totalWidth = slider.getWidth();
        double percentage = mouseX / totalWidth;
        double newValue = percentage * (slider.getMax() - slider.getMin()) + slider.getMin();
        
        // 更新播放进度到鼠标位置
        mediaPlayer.seek(Duration.seconds(newValue));
        mediaPlayer.currentTimeProperty().addListener(timeListener);
    });

    slider.styleProperty().bind(Bindings.createStringBinding(() -> {
        double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100.0;
        return String.format("-slider-track-color: linear-gradient(to right, -slider-filled-track-color 0%%, "
        + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
        percentage, percentage);
    }, slider.valueProperty(), slider.minProperty(), slider.maxProperty()));

    vslider.styleProperty().bind(Bindings.createStringBinding(() -> {
        double percentage = (vslider.getValue() - vslider.getMin()) / (vslider.getMax() - vslider.getMin()) * 100.0;
        return String.format("-slider-track-color: linear-gradient(to right, -slider-filled-track-color 0%%, "
        + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
        percentage, percentage);
    }, vslider.valueProperty(), vslider.minProperty(), vslider.maxProperty()));
}

void initiaSliderForButton(){
    mediaPlayer.currentTimeProperty().removeListener(timeListener);
    timeListener = (observable, oldValue, newValue) -> {
        slider.setValue(newValue.toSeconds());
    };
    initiaSlider();
    
}

void onMouseClickedUsual(MouseEvent event){
    
    ImageView imageButton=(ImageView)event.getSource();
    double originalScaleX=imageButton.getScaleX();
    double originalScaleY=imageButton.getScaleY();
    // 创建缩小动画
    
    ScaleTransition shrinkTransition = new ScaleTransition(Duration.seconds(0.1), imageButton);
    shrinkTransition.setToX(0.8 * originalScaleX);
    shrinkTransition.setToY(0.8 * originalScaleY);
    
    // 创建恢复动画
    ScaleTransition restoreTransition = new ScaleTransition(Duration.seconds(0.1), imageButton);
    restoreTransition.setToX(originalScaleX);
    restoreTransition.setToY(originalScaleY);
   
    // 创建一个顺序动画来按顺序播放缩小和恢复动画
    SequentialTransition sequentialTransition = new SequentialTransition(shrinkTransition, restoreTransition);
   
    // 播放动画
    sequentialTransition.play();

   }

void initiaVolume(){
     
    mediaPlayer.setVolume(vslider.getValue()/100);
 
    vslider.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
         // 当值正在改变时
         double volumeValue = vslider.getValue();
     
         // 映射vslider的值到音量范围（假设0到1之间）
         double volume = mapVolume(volumeValue);
    
         // 将音量应用到mediaPlayer上
         mediaPlayer.setVolume(volume);
     }); 
    vslider.valueProperty().addListener((observable, oldValue, newValue) -> {
        // 当值正在改变时
        double volumeValue = vslider.getValue();
    
        // 映射vslider的值到音量范围（假设0到1之间）
        double volume = mapVolume(volumeValue);
   
        // 将音量应用到mediaPlayer上
        mediaPlayer.setVolume(volume);
    });    
    
 }

private double mapVolume(double sliderValue) {
    // 假设vslider的范围是0到100，将其映射到0到1之间
    double minSliderValue = 0;
    double maxSliderValue = 100;
    double minVolume = 0;
    double maxVolume = 1;
    
    // 线性映射
    double volume = minVolume + (maxVolume - minVolume) * ((sliderValue - minSliderValue) / (maxSliderValue - minSliderValue));
    
    return volume;
}

void timeLine (){
    double endX = label.getParent().getBoundsInLocal().getWidth() - label.getWidth();
    Duration duration = Duration.seconds(5); // 动画持续时间

    // 创建 TranslateTransition，指定标签的起始和结束位置
    TranslateTransition transition = new TranslateTransition(duration, label);

    // 设置标签的初始位置为父容器的左侧外部
    label.setTranslateX(-label.getWidth());

    // 设置标签的结束位置为父容器的右侧外部
    transition.setToX(endX);

    // 设置循环次数为无限循环
    transition.setCycleCount(TranslateTransition.INDEFINITE);

    // 设置自动反向播放，即从左侧到右侧再从右侧到左侧
    transition.setAutoReverse(true);

    // 播放动画
    transition.play();
}

void playRandomMusic(String path){
    String randomItem = null;
    ObservableList<String> listItems = list.getItems();
    if(!listItems.isEmpty()){
        Random random = new Random();
        int randomIndex = random.nextInt(listItems.size());
        randomItem = listItems.get(randomIndex); // 将随机项存储在数组的第一个位置
    }
    if (randomItem != null) { // 检查随机项是否为空
        selectedItem = randomItem;
        label.setText("正在播放：" + selectedItem);
        String musicPath = path+"/"+ selectedItem;
        mediaPlayer = MediaPlayerFactory.createMediaPlayer(musicPath);
        initiaSlider();     
        initiaVolume();
        playHistory(selectedItem);  
        mediaPlayer.play();
        isPlaying = true;
        mediaPlayer.setOnEndOfMedia(()->{
            mediaPlayer.stop();
            playRandomMusic(path);
        });
        play.setImage(new Image("image/stop.png"));
    } else {
        System.out.println("音乐列表为空，无法播放随机音乐。");
    }
}
}
