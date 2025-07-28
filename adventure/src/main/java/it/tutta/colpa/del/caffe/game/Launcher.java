package it.tutta.colpa.del.caffe.game;

import it.tutta.colpa.del.caffe.game.boundary.GameGUI;
import it.tutta.colpa.del.caffe.game.boundary.GamePage;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.control.Engine;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

import javax.swing.*;

/**
 * @author giovav
 * @since 14/07/25
 */
public class Launcher {
    public Launcher(MainPageController mainPageController){
        mainPageController.closeGUI();

        GameGUI bo = new GamePage();
        Controller controller=new Engine(mainPageController, bo);
        controller.toString();
        bo.linkController(controller);
    }
}
