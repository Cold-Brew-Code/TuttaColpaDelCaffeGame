package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

/**
 * Engine specifico per il caricamento dal menu
 */
public class LoadEngine extends Engine {
    private final MainPageController mainController;

    public LoadEngine(MainPageController mainController, GUI choseSavePage) {
        super(mainController, choseSavePage);
        this.mainController = mainController;
    }

    @Override
    public GameDescription load(String saveName) throws Exception {
        GameDescription loadedGame = SaveLoad.loadGame(saveName);

        closeGUI();

        mainController.startGameFromSave(loadedGame);

        return loadedGame;
    }
}