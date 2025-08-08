/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.boundary.DialogueGUI;
import it.tutta.colpa.del.caffe.game.boundary.DialoguePage;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.control.DialogueController;
import it.tutta.colpa.del.caffe.game.control.ServerInterface;
import it.tutta.colpa.del.caffe.game.entity.*;
import it.tutta.colpa.del.caffe.game.exception.DialogueException;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import it.tutta.colpa.del.caffe.game.utility.RequestType;

import java.util.List;

/**
 * @author giova
 */
public class TalkObserver implements GameObserver {

    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        if (parserOutput.getCommand().getType() == CommandType.TALK_TO) {
            StringBuilder msg = new StringBuilder();
            NPC parserOutputNpc = parserOutput.getNpc();
            if (parserOutputNpc != null) {
                if (isNPCinCurrentRoom(parserOutputNpc, description.getCurrentRoom())) {
                    NPC npc = description.getCurrentRoom()
                                         .getNPCs()
                                         .stream()
                                         .filter(npc1 ->(npc1.getId()==parserOutputNpc.getId()))
                                         .findFirst()
                                         .get();
                    if (npc.getId() == 8) { // id = 8 <=> NPC è Professore MAP

                    } else {
                        msg.append(this.runDialogue(npc, description));
                    }
                } else {
                    msg.append("Hai le allucinazioni? " + parserOutputNpc.getNome() + " non è in questa stanza!");
                }
            } else if (parserOutputNpc == null && description.getCurrentRoom().getNPCs().size() == 1) {
                NPC npc = description.getCurrentRoom().getNPCs().get(0);
                try {
                    if (npc.getId() == 8) { // id = 8 <=> NPC è Professore MAP

                    } else {
                        msg.append(this.runDialogue(npc, description));
                    }
                } catch (NullPointerException e) {
                    msg.append("Non c'è nessuno con cui parlare quì!");
                }
            } else if (description.getCurrentRoom().getNPCs().isEmpty()) {
                msg.append("\nNon c'è nessuno con cui parlare qui!");
            } else {
                msg.append("Con chi vuoi parlare? Non ho capito...");
                msg.append("\nPuoi parlare con:");
                for (NPC nPc : description.getCurrentRoom().getNPCs()) {
                    msg.append("\n").append(nPc.getNome());
                }
            }
            return msg.toString();
        }
        return "";
    }

    private boolean isNPCinCurrentRoom(NPC npc, Room room) {
        return room.getNPCs().contains(npc);
    }

    private String runDialogue(NPC npc, GameDescription description) {
        StringBuilder msg = new StringBuilder(" ");
        try {
            Dialogo dialogue = npc.getDialogoCorr();

            new DialogueHandler(npc.getNome(), dialogue, description);
        } catch (DialogueException e) {
            msg = new StringBuilder(e.getMessage());
        }
        return msg.toString();
    }


    private String runQuiz() {
        StringBuilder msg = new StringBuilder();

        return msg.toString();
    }

    private class DialogueHandler implements DialogueController {
        private final DialogueGUI GUI;
        private final String NPCName;
        private final Dialogo dialogue;
        private final GameDescription description;

        public DialogueHandler(String NPCName, Dialogo dialogue, GameDescription description) {
            this.dialogue = dialogue;
            this.NPCName = NPCName;
            this.GUI = new DialoguePage(null, true);
            this.description = description;
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
        public void answerChosen(final String answer) {
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

        public void showCurrentDialogue() {
            GUI.addNPCStatement(NPCName, dialogue.getCurrentNode());
            GUI.addUserPossibleAnswers(dialogue.getCurrentAssociatedPossibleAnswers());
        }

        private void eventHandler(int eventID, Room currentRoom, String lastProducedStatement) {
            try {
                ServerInterface serverInterface = new ServerInterface("localhost", 49152);
                String updatedLook = serverInterface.requestToServer(RequestType.UPDATED_LOOK, eventID);
                switch (eventID) {
                    case 4:
                        if (lastProducedStatement.equals("La gittata è massima quando 𝜃=45")) {
                            currentRoom.setLook(updatedLook);

                        } else {
                            currentRoom.setLook("");
                        }
                        break;
                    case 10:
                        if (lastProducedStatement.equals("")) {
                            currentRoom.setLook(updatedLook);
                        } else {
                            currentRoom.setLook("");
                        }
                        break;
                    case 11:
                        if (lastProducedStatement.equals("La gittata è massima quando 𝜃=45")) {
                            currentRoom.setLook(updatedLook);
                        } else {
                            currentRoom.setLook("");
                        }
                        break;
                    case 12:
                        if (lastProducedStatement.equals("La gittata è massima quando 𝜃=45")) {
                            currentRoom.setLook(updatedLook);
                        } else {
                            currentRoom.setLook("");
                        }
                        break;
                    case 13:
                        if (lastProducedStatement.equals("La gittata è massima quando 𝜃=45")) {
                            currentRoom.setLook(updatedLook);
                        } else {
                            currentRoom.setLook("");
                        }
                        break;
                }
            } catch (ServerCommunicationException e) {
                System.err.println("Modifiche non effettuate, richiesta al server non pervenuta: " + e.getMessage());
            }
        }
    }
}
