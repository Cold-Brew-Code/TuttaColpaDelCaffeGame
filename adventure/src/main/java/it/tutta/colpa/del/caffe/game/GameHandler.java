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
     *
     * @param mainPageController il motore principale del menu iniziale, usato
     * per chiudere la GUI principale e passare il controllo al nuovo
     * {@link Engine} di gioco
     */
    public GameHandler(Engine mainPageController) {
        mainPageController.closeGUI();

        GameGUI bo = new GamePage();
        Controller controller = new it.tutta.colpa.del.caffe.game.control.Engine(mainPageController, bo);
        controller.toString();
        bo.linkController(controller);
    }
}
