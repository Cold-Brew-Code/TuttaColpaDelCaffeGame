package it.tutta.colpa.del.caffe.loadsave.control;

import java.util.List;

import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;

/**
 * @author giovav
 * @since 19/07/25
 */

public interface LoadController extends Controller {
    GameDescription load(String saveName) throws Exception;

    boolean deleteSave(String saveName);

    List<String> getSaveList() throws Exception;

    void cancelOperation();
}
