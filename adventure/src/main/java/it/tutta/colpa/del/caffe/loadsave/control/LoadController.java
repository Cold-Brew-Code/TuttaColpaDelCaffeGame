package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;

import java.io.IOException;
import java.util.List;

/**
 * @author giovav
 * @since 19/07/25
 */
public interface LoadController extends Controller {
    void save(GameDescription gameDescription) throws IOException;

    GameDescription load(String saveName) throws Exception;

    boolean deleteSave(String saveName);

    List<String> getSaveList() throws Exception;

    void cancelOperation();
}