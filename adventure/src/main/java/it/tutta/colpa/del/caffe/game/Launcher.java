package it.tutta.colpa.del.caffe.game;

import it.tutta.colpa.del.caffe.game.boundary.GameGUI;
import it.tutta.colpa.del.caffe.game.boundary.GamePage;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.control.Engine;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

/**
 * @author giovav
 * @since 14/07/25 La classe {@code Launcher} si occupa di avviare la schermata
 * di gioco principale.
 * <p>
 * Quando viene istanziata, chiude la GUI iniziale (menu principale) e apre la
 * schermata di gioco {@link GamePage}, collegandola al controller
 * {@link Engine}.
 * </p>
 */
public class Launcher {

    /**
     * Costruttore della classe {@code Launcher}.
     * <p>
     * - Chiude la GUI iniziale tramite il controller passato come parametro.
     * <br>
     * - Crea una nuova istanza di {@link GamePage}. <br>
     * - Inizializza un nuovo {@link Engine} come controller per la pagina di
     * gioco. <br>
     * - Collega la pagina di gioco al controller appena creato.
     * </p>
     *
     * @param mainPageController il controller della pagina principale (menu
     * iniziale), usato per chiudere la GUI e inizializzare il nuovo engine
     */
    public Launcher(MainPageController mainPageController) {
        mainPageController.closeGUI();

        GameGUI bo = new GamePage();
        Controller controller = new Engine(mainPageController, bo);
        controller.toString();
        bo.linkController(controller);
    }
}
