package it.tutta.colpa.del.caffe.game.boundary;

import java.util.List;

/**
 * @author giovav
 * @since 07/08/25
 */
public interface DialogueGUI extends GUI{
    void addUserPossibleAnswers(List<String> statements);
    void addUserStatement(String userName, String statement);
    void addNPCStatement(String npcName, String statement);
}
