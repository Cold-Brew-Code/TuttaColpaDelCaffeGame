package it.tutta.colpa.del.caffe.game;

import it.tutta.colpa.del.caffe.game.boundary.GameGUI;
import it.tutta.colpa.del.caffe.game.boundary.GamePage;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.start.control.Engine;

/**
 * @author giovav
 * @since 14/07/25
 */
public class GameHandler {
    public GameHandler(Engine mainPageController){
        mainPageController.closeGUI();

        GameGUI bo = new GamePage();
        Controller controller=new it.tutta.colpa.del.caffe.game.control.Engine(mainPageController, bo);
        controller.toString();
        bo.linkController(controller);
    }
}
