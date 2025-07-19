package it.tutta.colpa.del.caffe.start.control;

import it.tutta.colpa.del.caffe.game.Launcher;
import it.tutta.colpa.del.caffe.start.boundary.MainPage;

/**
 * @author giovav
 * @since 16/07/25
 */
public class MainPageController {
    private MainPage frame;

    public MainPageController() {}

    public void startGame(){
        Launcher gameLauncher = new Launcher(this);
    }

    public void loadGame(){

        closeWindow();
    }

    public void quit(){
        System.exit(0);
    }

    public void openWindow(){
        frame.open();
    }
    public void closeWindow(){
        frame.close();
    }
    public void setFrame(MainPage frame){
        this.frame=frame;
    }
}
