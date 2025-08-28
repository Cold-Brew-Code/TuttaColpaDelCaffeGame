package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.control.Controller;
import java.util.List;
import java.io.File;
import javax.swing.JOptionPane;

public class Engine implements LoadController {
    private Controller mainPageController;
    private GUI choseSavePage;
    private it.tutta.colpa.del.caffe.loadsave.boundary.ChoseSavePage savePage;
    private boolean loadWasSuccessful = false;

    public Engine(Controller mainPageController, GUI choseSavePage) {
        this.mainPageController = mainPageController;
        this.choseSavePage = choseSavePage;

        if (choseSavePage instanceof it.tutta.colpa.del.caffe.loadsave.boundary.ChoseSavePage) {
            this.savePage = (it.tutta.colpa.del.caffe.loadsave.boundary.ChoseSavePage) choseSavePage;
        }

        this.choseSavePage.open();
        takeSaves();
    }

    @Override
    public void load(String saveFileName) {
        try {
            Object loadedObject = SaveLoad.loadObject(saveFileName);

            if (loadedObject instanceof it.tutta.colpa.del.caffe.game.entity.GameDescription) {
                it.tutta.colpa.del.caffe.game.entity.GameDescription loadedGame = (it.tutta.colpa.del.caffe.game.entity.GameDescription) loadedObject;

                this.loadWasSuccessful = true;

                choseSavePage.close();
                it.tutta.colpa.del.caffe.game.GameHandler.loadGame(
                        (it.tutta.colpa.del.caffe.start.control.Engine) mainPageController,
                        loadedGame);
            } else {
                // Usa JOptionPane direttamente invece di chiamare notifyError su GUI
                JOptionPane.showMessageDialog(null, "File di salvataggio non valido", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // Usa JOptionPane direttamente invece di chiamare notifyError su GUI
            JOptionPane.showMessageDialog(null, "Impossibile caricare il salvataggio: " + e.getMessage(),
                    "Errore di Caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteSave(String fileName) {
        try {
            String saveDir = "./src/main/resources/saves/";
            File fileToDelete = new File(saveDir + fileName);

            if (fileToDelete.exists() && fileToDelete.delete()) {
                takeSaves();
                // Usa JOptionPane direttamente invece di chiamare showInformation su GUI
                JOptionPane.showMessageDialog(null, "Salvataggio eliminato con successo!", "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Usa JOptionPane direttamente invece di chiamare notifyError su GUI
                JOptionPane.showMessageDialog(null, "Impossibile eliminare il salvataggio", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // Usa JOptionPane direttamente invece di chiamare notifyError su GUI
            JOptionPane.showMessageDialog(null, "Errore durante l'eliminazione: " + e.getMessage(), "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void takeSaves() {
        try {
            List<String> saveFiles = SaveLoad.getSaveFiles();
            if (savePage != null) {
                savePage.updateSaveList(saveFiles);
            }
        } catch (Exception e) {
            // Usa JOptionPane direttamente invece di chiamare notifyError su GUI
            JOptionPane.showMessageDialog(null, "Impossibile caricare la lista dei salvataggi", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void cancelOperation() {
        if (loadWasSuccessful) {
            return;
        }
        closeGUI();
    }

    @Override
    public void openGUI() {
        takeSaves();
        choseSavePage.open();
    }

    @Override
    public void closeGUI() {
        choseSavePage.close();
        if (!loadWasSuccessful) {
            mainPageController.openGUI();
        }
    }
}