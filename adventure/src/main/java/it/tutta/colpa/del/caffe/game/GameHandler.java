package it.tutta.colpa.del.caffe.game;

import it.tutta.colpa.del.caffe.game.boundary.GameGUI;
import it.tutta.colpa.del.caffe.game.boundary.GamePage;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.control.Engine;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

/**
 * @author giovav
 * @since 14/07/25
 */
public class GameHandler {

    // Costruttore per nuova partita
    public GameHandler(MainPageController mainPageController) {
        mainPageController.closeGUI();
        GameGUI bo = new GamePage();
        Controller controller = new Engine(mainPageController, bo);
        bo.linkController(controller);
    }

    // 🆕 Costruttore per partita caricata
    public GameHandler(MainPageController mainPageController, GameDescription loadedGame) {
        mainPageController.closeGUI();
        GameGUI bo = new GamePage();
        Controller controller = new Engine(loadedGame, mainPageController, bo);
        bo.linkController(controller);
    }
}