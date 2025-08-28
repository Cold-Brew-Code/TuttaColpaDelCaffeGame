package it.tutta.colpa.del.caffe.game;

import it.tutta.colpa.del.caffe.game.boundary.GameGUI;
import it.tutta.colpa.del.caffe.game.boundary.GamePage;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.start.control.Engine;

/**
 * La classe {@code GameHandler} si occupa di gestire l'avvio di una nuova
 * partita.
 * <p>
 * Quando viene istanziata:
 * <ul>
 * <li>chiude la GUI principale (menu iniziale) tramite il controller
 * {@link Engine};</li>
 * <li>crea una nuova schermata di gioco {@link GamePage};</li>
 * <li>inizializza un nuovo controller
 * {@link it.tutta.colpa.del.caffe.game.control.Engine} per la gestione della
 * logica di gioco;</li>
 * <li>collega la schermata di gioco al nuovo controller.</li>
 * </ul>
 * </p>
 *
 * @author giovav
 * @since 14/07/25
 */
public class GameHandler {

    /**
     * Costruttore che avvia una nuova partita.
     */
    public GameHandler(Engine mainPageController) {
        mainPageController.closeGUI();
        GameGUI bo = new GamePage();
        Controller controller = new it.tutta.colpa.del.caffe.game.control.Engine(mainPageController, bo);
        bo.linkController(controller);
    }

    /**
     * Metodo statico per caricare una partita salvata.
     */
    public static void loadGame(Engine mainPageController, Object loadedGame) {
        if (!(loadedGame instanceof it.tutta.colpa.del.caffe.game.entity.GameDescription)) {
            throw new IllegalArgumentException("Oggetto di caricamento non valido");
        }

        it.tutta.colpa.del.caffe.game.entity.GameDescription gameDescription = (it.tutta.colpa.del.caffe.game.entity.GameDescription) loadedGame;

        mainPageController.closeGUI();
        GameGUI bo = new GamePage();

        Controller controller = new it.tutta.colpa.del.caffe.game.control.Engine(
                mainPageController,
                bo,
                gameDescription);

        bo.linkController(controller);
    }
}