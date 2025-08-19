package it.tutta.colpa.del.caffe.loadsave.control;

import java.io.File;
import java.util.List;

public class SaveManager {
    public List<String> listSaves() {
        return SaveLoad.getSaveList();
    }

    public boolean deleteSave(String saveName) {
        if (!saveName.endsWith(".save")) {
            saveName += ".save";
        }
        File saveFile = new File(SaveLoad.SAVES_DIR, saveName);
        return saveFile.delete();
    }

    public String formatDisplayName(String filename) {
        try {
            String cleanName = filename.replace("save_", "").replace(".save", "");
            String[] parts = cleanName.split("_");

            if (parts.length == 2) {
                String date = parts[0].replace("-", "/");
                String time = parts[1].replace("-", ":");
                return "Salvataggio del " + date + " alle " + time;
            }
        } catch (Exception e) {
            System.err.println("Errore formattazione nome salvataggio: " + filename);
        }
        return filename;
    }
}