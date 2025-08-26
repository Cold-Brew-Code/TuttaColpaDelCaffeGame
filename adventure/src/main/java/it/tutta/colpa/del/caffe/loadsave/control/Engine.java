package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.control.Controller;
import java.util.List;
import java.io.File;

public class Engine implements LoadController {
    private Controller mainPageController;
    private GUI choseSavePage;
    private it.tutta.colpa.del.caffe.loadsave.boundary.ChoseSavePage savePage;

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

                choseSavePage.close();
                it.tutta.colpa.del.caffe.game.GameHandler.loadGame(
                        (it.tutta.colpa.del.caffe.start.control.Engine) mainPageController,
                        loadedGame);
            } else {
                choseSavePage.notifyError("Errore", "File di salvataggio non valido");
            }
        } catch (Exception e) {
            choseSavePage.notifyError("Errore di Caricamento",
                    "Impossibile caricare il salvataggio: " + e.getMessage());
        }
    }

    @Override
    public void deleteSave(String fileName) {
        try {
            String saveDir = "./src/main/resources/saves/";
            File fileToDelete = new File(saveDir + fileName);

            if (fileToDelete.exists() && fileToDelete.delete()) {
                takeSaves(); // aggiorna la lista
                choseSavePage.showInformation("Successo", "Salvataggio eliminato con successo!");
            } else {
                choseSavePage.notifyError("Errore", "Impossibile eliminare il salvataggio");
            }
        } catch (Exception e) {
            choseSavePage.notifyError("Errore", "Errore durante l'eliminazione: " + e.getMessage());
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
            choseSavePage.notifyError("Errore", "Impossibile caricare la lista dei salvataggi");
        }
    }

    @Override
    public void cancelOperation() {
        closeGUI();
        mainPageController.openGUI();
    }

    @Override
    public void openGUI() {
        takeSaves();
        choseSavePage.open();
    }

    @Override
    public void closeGUI() {
        choseSavePage.close();
        mainPageController.openGUI();
    }
}