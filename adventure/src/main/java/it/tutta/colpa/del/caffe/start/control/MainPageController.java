package it.tutta.colpa.del.caffe.start.control;

import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;

/**
 * @author giovav
 * @since 19/07/25
 */
public interface MainPageController extends Controller {
    public void quit();

    public void loadGame();

    public void startGame();

    public void startGameFromSave(GameDescription loadedGame);
}
