/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.boundary.DialogueGUI;
import it.tutta.colpa.del.caffe.game.boundary.DialoguePage;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.control.DialogueController;
import it.tutta.colpa.del.caffe.game.entity.*;
import it.tutta.colpa.del.caffe.game.exception.DialogueException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

import java.util.List;

/**
 * @author giova
 */
public class TalkObserver implements GameObserver {

    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        if (parserOutput.getCommand().getType() == CommandType.TALK_TO) {
            StringBuilder msg = new StringBuilder();
            NPC npc = parserOutput.getNpc();
            if (npc != null) {
                if (isNPCinCurrentRoom(npc, description.getCurrentRoom())) {
                    if (npc.getId() == 8) { // id = 8 <=> NPC è Professore MAP

                    } else {
                        msg.append(this.runDialogue(npc));
                    }
                } else {
                    msg.append("Hai le allucinazioni? " + npc.getNome() + " non è in questa stanza!");
                }
            } else {
                msg.append("Con chi vuoi parlare? Non ho capito... scrivi parla con <nome NPC>");
            }
            return msg.toString();
        }
        return "";
    }

    private boolean isNPCinCurrentRoom(NPC npc, Room room) {
        return room.getNPCs().contains(npc);
    }

    private String runDialogue(NPC npc) {
        StringBuilder msg = new StringBuilder();
        try {
            Dialogo dialogue = npc.getDialogoCorr();
            new DialogueHandler(npc.getNome(), dialogue);
        } catch (DialogueException e) {
            msg.append(e.getMessage());
        }
        return " ";
    }

    private String runQuiz() {
        StringBuilder msg = new StringBuilder();

        return msg.toString();
    }

    private class DialogueHandler implements DialogueController {
        private final DialogueGUI GUI;
        private final String NPCName;
        private final Dialogo dialogue;

        public DialogueHandler(String NPCName, Dialogo dialogue) {
            this.dialogue = dialogue;
            this.NPCName = NPCName;
            this.GUI = new DialoguePage(null, true);
            GUI.linkController(this);
            showCurrentDialogue();
            this.GUI.setPageClosable(false);
            openGUI();
        }

        @Override
        public void openGUI() {
            this.GUI.open();
        }

        @Override
        public void closeGUI() {
            this.GUI.close();
        }

        @Override
        public void answerChosen(String answer) {
            try {
                dialogue.setNextStatementFromAnswer(answer);
            } catch (DialogueException e) {
                System.err.println("DialogueException " + e.getMessage());
            }
            showCurrentDialogue();
            if (dialogue.getCurrentAssociatedPossibleAnswers().isEmpty()) {
                dialogue.setActivity(false);
                this.GUI.setPageClosable(true);
            }
        }

        public void showCurrentDialogue(){
            GUI.addNPCStatement(NPCName, dialogue.getCurrentNode());
            GUI.addUserPossibleAnswers(dialogue.getCurrentAssociatedPossibleAnswers());
        }

    }
}
