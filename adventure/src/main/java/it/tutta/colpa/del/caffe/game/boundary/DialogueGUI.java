package it.tutta.colpa.del.caffe.game.boundary;

import it.tutta.colpa.del.caffe.game.control.Controller;
import java.util.List;

public interface DialogueGUI {

    /**
     * Record per rappresentare una possibile risposta dell'utente,
     * che include il testo e un booleano per indicare se è selezionabile.
     * @param text Il testo della risposta.
     * @param isEnabled true se la risposta è selezionabile, false altrimenti.
     */
    record PossibleAnswer(String text, boolean isEnabled) {}

    void addNPCStatement(String npcName, String statement);

    void addUserStatement(String userName, String statement);

    void addUserPossibleAnswers(List<PossibleAnswer> statements);

    void open();

    void close();

    void linkController(Controller c);

    void lockPage();
    void relasePage();
}