package it.tutta.colpa.del.caffe.game.control;

import it.tutta.colpa.del.caffe.game.exception.DialogueException;

/**
 * @author giovav
 * @since 07/08/25
 */
public interface DialogueController extends Controller{
    void answerChosen(String answer);
}
