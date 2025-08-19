package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.control.GameController;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.loadsave.boundary.ChoseSavePage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * @author giovav
 * @since 19/07/25
 */
public class Engine implements LoadController {
    private Controller mainPageController;
    private GUI csp;
    private final SaveManager saveManager = new SaveManager();

    public Engine(Controller mainPageController, GUI choseSavePage) {
        this.mainPageController = mainPageController;
        this.csp = choseSavePage;
        csp.open();
    }

    @Override
    public void save(GameDescription gameDescription) throws IOException {
        debugSaveLocation();
        try {
            String savePath = SaveLoad.saveGame(gameDescription);

            JOptionPane.showMessageDialog(
                    null,
                    "Salvataggio creato in:\n" + new File(savePath).getAbsolutePath(),
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);

            if (csp instanceof ChoseSavePage) {
                ((ChoseSavePage) csp).refreshSaveList();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Errore durante il salvataggio:\n" + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    @Override
    public GameDescription load(String saveName) throws Exception {
        GameDescription loadedGame = SaveLoad.loadGame(saveName);

        closeGUI();

        if (mainPageController instanceof it.tutta.colpa.del.caffe.start.control.MainPageController) {
            it.tutta.colpa.del.caffe.start.control.MainPageController mainController = (it.tutta.colpa.del.caffe.start.control.MainPageController) mainPageController;

            mainController.startGameFromSave(loadedGame);
        }

        return loadedGame;
    }

    @Override
    public boolean deleteSave(String saveName) {
        return saveManager.deleteSave(saveName);
    }

    @Override
    public List<String> getSaveList() throws Exception {
        return saveManager.listSaves();
    }

    @Override
    public void cancelOperation() {
        closeGUI();
    }

    @Override
    public void openGUI() {
        // Già gestito nel costruttore
    }

    @Override
    public void closeGUI() {
        csp.close();
    }

    private void debugSaveLocation() {
        File savesDir = new File(SaveLoad.SAVES_DIR);
        System.out.println("=== DEBUG SAVEPATH ===");
        System.out.println("Percorso: " + savesDir.getAbsolutePath());
        System.out.println("Esiste: " + savesDir.exists());
        System.out.println("Scrivibile: " + savesDir.canWrite());
        System.out.println("Contenuto: " + Arrays.toString(savesDir.list()));
    }
}