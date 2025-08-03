package it.tutta.colpa.del.caffe.start.control;

import it.tutta.colpa.del.caffe.game.GameHandler;
import it.tutta.colpa.del.caffe.loadsave.ChoseSaveHandler;
import it.tutta.colpa.del.caffe.loadsave.control.LoadController;
import it.tutta.colpa.del.caffe.start.boundary.MainPage;

/**
 * @author giovav
 * @since 16/07/25
 */
public class Engine implements MainPageController{
    private MainPage frame;

    public Engine(MainPage frame) {
        this.frame=frame;
    }

    public void startGame(){
        GameHandler gameHandler = new GameHandler(this);
    }

    public void loadGame(){
        ChoseSaveHandler csv = new ChoseSaveHandler(this);
        closeGUI();
    }

    public void quit(){
        System.exit(0);
    }

    public void openGUI(){
        frame.open();
    }
    public void closeGUI(){
        frame.close();
    }
}
