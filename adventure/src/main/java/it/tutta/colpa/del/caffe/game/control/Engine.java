package it.tutta.colpa.del.caffe.game.control;

import it.tutta.colpa.del.caffe.game.boundary.GamePage;
import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameMap;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.Direzione;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;

/**
 * @author giovav
 * @since 11/07/25
 */
public class Engine implements Serializable {
    private GamePage frame;
    private GameDescription description;

    @SuppressWarnings("unused")
    public Engine() {
        try {
            this.description = initGame();
        } catch (ServerCommunicationException e) {
            //apre JDialogPane
        }
    }

    @SuppressWarnings("unused")
    public Engine(String filePath) {

    }


    @SuppressWarnings("unchecked")
    private GameDescription initGame() throws ServerCommunicationException {
        GameMap gm = null;
        List<Command> commands = null;
        try (Socket socket = new Socket("localhost", 49152)) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.println("mappa");
            Object answer = in.readObject();

            boolean reCheck = true;
            int timesChecked = 0;

            // ask for GameMap
            while ((reCheck) && (timesChecked < 5)) {
                if (answer instanceof GameMap) {
                    gm = (GameMap) answer;
                    reCheck = false;
                } else if (answer instanceof String) {
                    System.err.println("[Server] Si è verificato un errore: " + answer);
                } else {
                    System.err.println("Si è verificato un errore.");
                }
                timesChecked++;
            }

            reCheck = true;
            timesChecked = 0;

            out.println("comandi");
            answer = in.readObject();

            //asks for Commands
            while ((gm != null) && (reCheck) && (timesChecked < 5)) {
                if (answer instanceof List<?> answerList) {
                    boolean areAllCommands = answerList.stream()
                            .allMatch(e -> e instanceof Command);
                    if (areAllCommands) {
                        commands = (List<Command>) answerList;
                        reCheck = false;
                    } else {
                        System.err.println("L'oggetto ricevuto non è una lista di comandi.");
                    }
                } else if (answer instanceof String) {
                    System.err.println("[Server] Si è verificato un errore: " + answer);
                } else {
                    System.err.println("Si è verificato un errore.");
                }
                timesChecked++;
            }

            if (gm == null || commands == null) {
                throw new ServerCommunicationException("Comunicazione client-server fallita");
            } else {
                System.out.println("[debug] tutto okay amo, funziono!!!");
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to connect to the server.");
        }

        GameDescription gd = new GameDescription(gm, commands);
        //observer attachment
        return gd;
    }

    @SuppressWarnings("unused")
    private void initGame(String savePath) {
        //returns GameDescription
    }

    @SuppressWarnings("unused")
    private boolean moveFromHere(Direzione direction) {
        try {
            description.getGameMap().getStanzaArrivo(direction);
        } catch (GameMapException e) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unused")
    private boolean moveWithElevator(int floor) {
        try {
            description.getGameMap().prendiAscensore(floor);
        } catch (GameMapException e) {
            return false;
        }
        return true;
    }
}
