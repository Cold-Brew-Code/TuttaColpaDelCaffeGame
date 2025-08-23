/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import it.tutta.colpa.del.caffe.game.boundary.DialogueGUI;
import it.tutta.colpa.del.caffe.game.boundary.DialoguePage;
import it.tutta.colpa.del.caffe.game.entity.Dialogo;
import it.tutta.colpa.del.caffe.game.entity.DialogoQuiz;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.NPC;
import it.tutta.colpa.del.caffe.game.entity.Room;
import it.tutta.colpa.del.caffe.game.exception.ConnectionError;
import it.tutta.colpa.del.caffe.game.exception.DialogueException;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.rest.QuizNpc;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameStatus;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import it.tutta.colpa.del.caffe.game.utility.RequestType;

import javax.swing.*;

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
                        if (description.getStatus() == GameStatus.ESAME_DA_FARE) {
                            msg.append(this.runQuiz(npc, description));
                        } else {
                            msg.append("Non puoi sostenere l'esame se non vai prima in bagno");
                        }
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
                        if (description.getStatus() == GameStatus.ESAME_DA_FARE) {
                            msg.append(this.runQuiz(npc, description));
                        } else {
                            msg.append("Non puoi sostenere l'esame se non vai prima in bagno");
                        }
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
        StringBuilder msg = new StringBuilder();
        try {
            Dialogo dialogue = npc.getDialogoCorr();
            msg.append((new DialogueHandler(npc.getNome(), dialogue, description, true)).getReturnStatement());
            if (!dialogue.isActive()) {
                npc.consumedDialogue();
            }
            if (msg.isEmpty() || msg.toString() == "") {
                msg.append("Hai appena parlato con " + npc.getNome() + ", ogni secondo è di vitale importanza!");
            }
        } catch (DialogueException e) {
            msg = new StringBuilder(e.getMessage());
        }
        return msg.toString();
    }


    private String runQuiz(NPC npc, GameDescription description) {
        StringBuilder msg = new StringBuilder();
        try {
            QuizHandler quizHandler = new QuizHandler(npc.getNome(), npc.getDialogoCorr(), description);
            //quizHandler.startQuizHandler();
            msg.append(quizHandler.getReturnStatement());
        } catch (DialogueException e) {
            throw new RuntimeException(e);
        }
        return msg.toString();
    }

    private class DialogueHandler implements DialogueController {
        protected final DialogueGUI GUI;
        protected final String NPCName;
        private final Dialogo dialogue;
        protected final GameDescription description;
        protected final StringBuilder returnStatement = new StringBuilder();

        private final static String TO_DISABLE_ANSWER_DIALOGUE_3_STATEMENT = "Hmm... forse potrebbe esistere un bagno segreto. Ma non diffondo segreti mistici in maniera gratuita. Hai per caso un caffè per un povero portinaio stanco?";
        private final static String DIALOGUE_3_ANSWER_TO_DISABLE = "Sì";
        private final static String TO_DISABLE_ANSWER_DIALOGUE_10_STATEMENT_1 = "Potrei saperlo. Ma le verità profonde vanno pulite come i pavimenti: con candeggina. Tu ce l'hai?";
        private final static String DIALOGUE_10_STATEMENT_ANSWER_TO_DISABLE_1 = "Ecco la varechina. L'ho trovata nel laboratorio di robotica.";
        private final static String TO_DISABLE_ANSWER_DIALOGUE_10_STATEMENT_2 = "Ragazzo, ultima chance, hai la candeggina??";
        private final static String DIALOGUE_10_STATEMENT_ANSWER_TO_DISABLE_2 = "Sì";

        private final static String NODE_EVT_SHOW_KEY = "Bravo! L'ereditarietà multipla è bandita in Java: troppi casini col diamante, dicono. Va bene, prendi questa chiave: ti apre l'ascensore fino al settimo piano. Ma occhio: più sali, più i misteri si complicano.";
        private final static String NODE_EVT_SHOW_MAP = "Bravo. Hai fiuto per l'orientamento, oltre che per l'urgenza. C'è una mappa del dipartimento appesa nell'aula studio al piano terra, ma è coperta da un cartellone pubblicitario. Trovala e saprai dove andare.";
        private final static Set<String> EVT_UNLOCK_RESTROOM = Set.of("Giusto. P potrebbe essere NP… o forse no. Finché non lo dimostriamo, rimane il più grande enigma della nostra epoca. Vai pure, ti sei guadagnato il diritto di passare.",
                "Bravo! In una matrice di adiacenza devi aggiungere o rimuovere un'intera riga e colonna: O(n^2). Come promesso, vieni: facciamo saltare la fila… ma non dirlo in giro!",
                "Bravo! Esatto: non puoi creare oggetti direttamente da una classe astratta, poiché ha metodi non implementati. Dai, passa… corri! Che la forza sia con te (e col tuo intestino).");
        private final static String NODE_EVT_CORRECT_ANSWER_DIALOGUE_4 = "La gittata è massima quando \uD835\uDF03=45 gradi.";

        private final static String NODE_EVT_DROP_BLECH = "Questa sì che profuma di dedizione. Ascolta bene, ragazzo: Sette sono i piani, ma non tutti mostrano il vero. Dove il sapere si tiene alto, una porta si apre solo a chi ha la chiave della pulizia.";
        private final static String NODE_EVT_DROP_COFFEE = "Okay, ora sì che mi sento meglio. Allora ragazzo, ascolta: Si mormora che, al settimo cielo del sapere, esista un bagno così segreto che persino le mappe evitano di disegnarlo. La leggenda narra che la sua porta appaia solo a chi possiede un misterioso oggetto magico e la follia di usarlo.";

        private final static String NODE_SHOW_TOILETPAPER = "Vai. Corri. E ricorda: Il vero eroe non è chi trattiene…… ma chi arriva in tempo!";

        public DialogueHandler(String NPCName, Dialogo dialogue, GameDescription description, boolean openGUI) {
            this.dialogue = dialogue;
            this.NPCName = NPCName;
            this.GUI = new DialoguePage(null, true);
            this.description = description;
            GUI.linkController(this);
            if (!this.dialogue.getCurrentNode().equals(this.dialogue.getMainNode())) {
                printPreviewsStatements();
            }
            if (openGUI) {
                showCurrentDialogue();
                openGUI();
            }
        }


        void printPreviewsStatements() {
            List<String> dialogue = this.dialogue.getPreviewsStatement();
            for (int i = 0; i < dialogue.size(); i++) {
                if (i % 2 == 0) {
                    GUI.addNPCStatement(this.NPCName, dialogue.get(i));
                } else {
                    GUI.addUserStatement("Tu", dialogue.get(i));
                }
            }
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
            if (3 == dialogue.getId() && NODE_EVT_DROP_COFFEE.equals(dialogue.getCurrentNode())) {
                this.description.getInventory().remove(new GeneralItem(2), 1);
            } else if (10 == dialogue.getId() && NODE_EVT_DROP_BLECH.equals(dialogue.getCurrentNode())) {
                this.description.getInventory().remove(new GeneralItem(4), 1);
            }
            showCurrentDialogue();
            if (dialogue.getCurrentAssociatedPossibleAnswers().isEmpty()) {
                dialogue.setActivity(false);
                this.dialogueEndedEvent(this.dialogue.getId(), dialogue.getCurrentNode());
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
                    if (dialogue.getCurrentNode().equals(TO_DISABLE_ANSWER_DIALOGUE_3_STATEMENT)) {
                        textToDisable = DIALOGUE_3_ANSWER_TO_DISABLE;
                        requiredItemId = 2;
                    }
                    break;
                case 10:
                    if (dialogue.getCurrentNode().equals(TO_DISABLE_ANSWER_DIALOGUE_10_STATEMENT_1)) {
                        textToDisable = DIALOGUE_10_STATEMENT_ANSWER_TO_DISABLE_1;
                        requiredItemId = 4;
                    } else if (dialogue.getCurrentNode().equals(TO_DISABLE_ANSWER_DIALOGUE_10_STATEMENT_2)) {
                        textToDisable = DIALOGUE_10_STATEMENT_ANSWER_TO_DISABLE_2;
                        requiredItemId = 4;
                    }
                    break;
            }

            if (textToDisable == null) {
                return dialogue.getCurrentAssociatedPossibleAnswers().stream()
                        .map(answer -> new DialogueGUI.PossibleAnswer(answer, true))
                        .collect(Collectors.toList());
            }

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
                        if (lastProducedStatement.equals(NODE_EVT_SHOW_KEY)) {
                            this.description.getGameMap().getRoom(4).getObject(9).setVisibile(true);
                            this.returnStatement.append("Nella stanza c'è una chiave, raccoglila.");
                        }
                        break;
                    case 5:
                        // indovinello studente bagno primo piano, mostra la mappa !!!!
                        if (lastProducedStatement.equals(NODE_EVT_SHOW_MAP)) {
                            this.returnStatement.append("Cerca la mappa, è dietro un quadro di una delle aule studio!");
                            this.description.getGameMap().getRoom(5).getObject(1).setVisibile(true);
                        }
                        lookEvent(11, description.getCurrentRoom(), lastProducedStatement);
                        break;
                    case 6:
                        // dialogo con Dario Tremolanti
                        lookEvent(4, description.getCurrentRoom(), lastProducedStatement);
                        break;
                    case 7:
                    case 8:
                    case 9:
                        if (EVT_UNLOCK_RESTROOM.contains(lastProducedStatement)) {
                            this.description.getGameMap().getRoom(11).setDeniedEntry(true);
                        }
                        break;
                    case 10:
                        // dialogo con inserviente
                        lookEvent(10, description.getCurrentRoom(), lastProducedStatement);
                        break;
                    case 11:
                        if (lastProducedStatement.equals(NODE_SHOW_TOILETPAPER)) {
                            this.description.getGameMap().getRoom(10).getObject(13).setVisibile(true);
                        }
                        break;
                    case 12:
                        // dialogo con dottor cravattone
                        lookEvent(12, description.getCurrentRoom(), lastProducedStatement);
                        break;
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        private void lookEvent(int eventID, Room currentRoom, String lastProducedStatement) {
            try {
                ServerInterface serverInterface = new ServerInterface("localhost", 49152);
                String updatedLook = serverInterface.requestToServer(RequestType.UPDATED_LOOK, eventID);
                if (lastProducedStatement.equals(NODE_EVT_CORRECT_ANSWER_DIALOGUE_4) && eventID == 4) {
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

        public String getReturnStatement() {
            return returnStatement.toString();
        }
    }

    public class QuizHandler extends DialogueHandler {

        private final BlockingQueue<DialogoQuiz> quizQueue;
        private boolean isQuizRunning = false;
        private DialogoQuiz currentQuiz;
        private int quizScore = 0;
        private int attemptedQuiz = 0;
        private static final int MAX_DOMANDE = 5;

        public QuizHandler(String NPCName, Dialogo dialogue, GameDescription description) {
            super(NPCName, dialogue, description, false);
            quizQueue = new LinkedBlockingQueue<>();
            startQuizHandler();
            showCurrentDialogue();
            this.GUI.lockPage();
            openGUI();
        }

        private void startQuizHandler() {
            new Thread(() -> {
                System.err.println("Preload quiz thread started");
                for (int i = 0; i < MAX_DOMANDE; i++) {
                    DialogoQuiz quiz = null;
                    try {
                        quiz = QuizNpc.getQuiz();
                        System.err.println("Loaded quiz " + (i + 1));
                    } catch (ConnectionError e) {
                        System.err.println("Quiz caricato da memoria.");
                        quiz = QuizNpc.defaultQuizzes.get(i);
                    }finally {
                        try {
                            quizQueue.put(quiz);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                System.err.println("All quizzes requested");
            }).start();
        }

        private void runNextQuiz() {
            if (attemptedQuiz < MAX_DOMANDE) {
                super.GUI.addUserPossibleAnswers(Collections.emptyList());

                DialogoQuiz nextQuiz = quizQueue.poll();

                if (nextQuiz != null) {
                    displayQuiz(nextQuiz);
                } else {
                    super.GUI.addNPCStatement(super.NPCName, "Non mi dia fretta, sto pensando...");
                    new Thread(() -> {
                        try {
                            DialogoQuiz finalQuiz = quizQueue.take();
                            displayQuiz(finalQuiz);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.err.println("Quiz loading interrupted while waiting");
                        }
                    }).start();
                }
            } else {
                endQuiz();
            }
        }

        private void displayQuiz(DialogoQuiz quiz) {
            currentQuiz = quiz;
            System.out.println("[Debug]Domanda: " + currentQuiz.getDomanda());
            System.out.println("[Debug]Risposta corretta: " +
                    currentQuiz.getRisposte().get(currentQuiz.getIdCorretta()));

            super.GUI.addNPCStatement(super.NPCName, currentQuiz.getDomanda());
            super.GUI.addUserPossibleAnswers(
                    currentQuiz.getRisposte().stream()
                            .map(ans -> new DialogueGUI.PossibleAnswer(ans, true))
                            .collect(Collectors.toList())
            );
        }

        private void startQuiz() {
            this.isQuizRunning = true;
            super.GUI.addNPCStatement(super.NPCName, "Iniziamo con la prima domanda...");
            runNextQuiz();
        }

        private void endQuiz() {
            int score30 = (quizScore * 30) / MAX_DOMANDE;
            StringBuilder sb = new StringBuilder("L'esame è terminato, il punteggio che ha ottenuto è di " + score30 + "/30esimi pertanto lei è... ");
            if (score30 >= 18) {
                sb.append("PROMOSSO!!! complimenti.");
                this.description.setStatus(GameStatus.PROMOSSO);
            } else {
                sb.append("BOCCIATO!!!!");
                this.description.setStatus(GameStatus.BOCCIATO);
            }
            super.GUI.addNPCStatement(super.NPCName, sb.toString());
            super.GUI.addNPCStatement(super.NPCName, "L'esame è terminato, se ne vada!");
            super.returnStatement.append("Hai sostenuto l'esame con una votazione di ").append(score30).append(" 30esimi. ");
            super.returnStatement.append(score30 >= 18 ? "Hai vinto!" : "Hai perso!");
            this.GUI.relasePage();
        }

        @Override
        public void answerChosen(final String answer) {
            if (isQuizRunning) {
                if (currentQuiz.getRisposte().get(currentQuiz.getIdCorretta()).equals(answer)) {
                    quizScore++;
                    super.GUI.addNPCStatement(super.NPCName, currentQuiz.getMessaggioCorret());
                    System.out.println("Risposta corretta! Punteggio attuale: " + quizScore);
                } else {
                    super.GUI.addNPCStatement(super.NPCName, currentQuiz.getMessaggioErrato());
                    System.out.println("Risposta sbagliata! Punteggio attuale: " + quizScore);
                }
                attemptedQuiz++;
                runNextQuiz();
            } else {
                try {
                    super.dialogue.setNextStatementFromAnswer(answer);
                } catch (DialogueException e) {
                    System.err.println("DialogueException " + e.getMessage());
                    return;
                }

                if (super.dialogue.getCurrentAssociatedPossibleAnswers().isEmpty()) {
                    super.dialogue.setActivity(false);
                    super.dialogueEndedEvent(super.dialogue.getId(), super.dialogue.getCurrentNode());
                    startQuiz();
                } else {
                    showCurrentDialogue();
                }
            }
        }
    }
}