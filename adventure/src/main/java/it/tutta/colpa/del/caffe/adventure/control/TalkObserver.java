/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.boundary.DialogueGUI;
import it.tutta.colpa.del.caffe.game.boundary.DialoguePage;
import it.tutta.colpa.del.caffe.game.control.DialogueController;
import it.tutta.colpa.del.caffe.game.control.ServerInterface;
import it.tutta.colpa.del.caffe.game.entity.*;
import it.tutta.colpa.del.caffe.game.exception.DialogueException;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import it.tutta.colpa.del.caffe.game.utility.RequestType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                            .filter(npc1 -> (npc1.getId() == parserOutputNpc.getId()))
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
                this.dialogueEndedEvent(this.dialogue.getId(), null); // !!!!!!!!!!!!!!
                this.GUI.setPageClosable(true);
            }
        }

        public void showCurrentDialogue() {
            GUI.addNPCStatement(NPCName, dialogue.getCurrentNode());
            List<DialogueGUI.PossibleAnswer> answers = buildConditionalAnswers();
            GUI.addUserPossibleAnswers(answers);
        }

        private List<DialogueGUI.PossibleAnswer> buildConditionalAnswers() {
            String textToDisable = null;
            int requiredItemId = -1;

            switch (dialogue.getId()) {
                case 3:
                    if (dialogue.getCurrentNode().equals("Hmm... forse.potrebbe esistere un bagno segreto. ma non diffondo segreti mistici in maniera gratuita.  Hai per caso un caffè per un povero portinaio stanco?")) {
                        textToDisable = "Si";
                        requiredItemId = 2;
                    }
                    break;
                case 10:
                    if (dialogue.getCurrentNode().equals("Potrei saperlo. Ma le verità profonde vanno pulite come i pavimenti: con varechina. Tu ce l'hai?")) {
                        textToDisable = "Ecco la candeggina. L'ho trovata nel laboratorio di robotica.";
                        requiredItemId = 4;
                    } else if (dialogue.getCurrentNode().equals("Ragazzo ultima chance , hai la candeggina??")) {
                        textToDisable = "Si";
                        requiredItemId = 4;
                    }
                    break;
            }

            if (textToDisable == null) {
                return dialogue.getCurrentAssociatedPossibleAnswers().stream()
                        .map(answer -> new DialogueGUI.PossibleAnswer(answer, true))
                        .collect(Collectors.toList());
            }

            // Creiamo delle costanti "final" con i valori determinati dallo switch.
            // Queste possono essere usate tranquillamente dalla lambda.
            final String final_textToDisable = textToDisable;
            final int final_requiredItemId = requiredItemId;

            boolean playerHasItem = description.getInventory().contains(new GeneralItem(final_requiredItemId));

            return dialogue.getCurrentAssociatedPossibleAnswers().stream()
                    .map(answer -> {
                        boolean isEnabled = playerHasItem || !answer.equals(final_textToDisable);
                        return new DialogueGUI.PossibleAnswer(answer, isEnabled);
                    })
                    .collect(Collectors.toList());
        }

        private void dialogueEndedEvent(int dialogueID, String lastProducedStatement) {
            try {
                switch (dialogueID) {
                    case 1:
                        // dialogo con studente di storia
                        lookEvent(13, description.getCurrentRoom(), lastProducedStatement);
                        break;
                    case 4:
                        // Bruno mostra la chiave
                        // da verificare se ha passato l'indovinello !!!!!!!!!!!!!!!!!!!!!
                        if (lastProducedStatement.equals("")) {
                            this.description.getGameMap().getRoom(4).getObject(9).setVisibile(true);
                        }
                        break;
                    case 5:
                        // indovinello studente bagno primo piano, mostra la mappa !!!!
                        lookEvent(11, description.getCurrentRoom(), lastProducedStatement);
                        break;
                    case 6:
                        // dialogo con Dario Tremolanti
                        lookEvent(4, description.getCurrentRoom(), lastProducedStatement);
                        break;
                    case 7:
                    case 8:
                    case 9:
                        if (Set.of("Giusto. P potrebbe essere NP… o forse no. Finché non lo dimostriamo, rimane il più grande enigma della nostra epoca. Vai pure, ti sei guadagnato il diritto di passare.",
                                "Bravo! In una matrice di adiacenza devi aggiungere o rimuovere un'intera riga e colonna: O(n^2)\\nCome promesso, vieni: facciamo saltare la fila… ma non dirlo in giro!",
                                "Bravo! Esatto: non puoi creare oggetti direttamente da una classe astratta. Dai, passa… corri! Che la forza sia con te (e col tuo intestino).").contains(lastProducedStatement)) {
                            this.description.getGameMap().getRoom(11).setDeniedEntry(false);
                        }
                        break;
                    case 10:
                        // dialogo con inserviente
                        lookEvent(10, description.getCurrentRoom(), lastProducedStatement);
                        break;
                    case 12:
                        // dialogo con dottor cravattone
                        lookEvent(12, description.getCurrentRoom(), lastProducedStatement);
                        break;
                }
            } catch (Exception ignored) {
            }
        }

        private void lookEvent(int eventID, Room currentRoom, String lastProducedStatement) {
            try {
                ServerInterface serverInterface = new ServerInterface("localhost", 49152);
                String updatedLook = serverInterface.requestToServer(RequestType.UPDATED_LOOK, eventID);
                if (lastProducedStatement.equals("La gittata è massima quando 𝜃=45") && eventID == 4) {
                    currentRoom.setLook(updatedLook);
                } else if (eventID == 4) {
                    currentRoom.setLook("");
                } else {
                    currentRoom.setLook(updatedLook);
                }
            } catch (ServerCommunicationException e) {
                System.err.println("Modifiche non effettuate, richiesta al server non pervenuta: " + e.getMessage());
            }
        }
    }
}
