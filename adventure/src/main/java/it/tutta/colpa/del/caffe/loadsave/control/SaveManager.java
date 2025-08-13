package it.tutta.colpa.del.caffe.loadsave.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    public List<String> listSaves() throws FileNotFoundException {
        File dir = new File(SaveLoad.SAVES_DIR); // usa la costante da SaveLoad (evito ridondanza)
        if (!dir.exists()) {
            throw new FileNotFoundException("Directory dei salvataggi non trovata: " + SaveLoad.SAVES_DIR);
        }

        if (!dir.isDirectory()) {
            throw new FileNotFoundException("Path non è una directory: " + SaveLoad.SAVES_DIR);
        }

        List<String> saves = new ArrayList<>();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".save"));

        if (files != null) {
            for (File file : files) {
                saves.add(file.getName());
            }
        }
        return saves;
    }

    public boolean deleteSave(String saveName) {
        if (!saveName.endsWith(".save")) {
            saveName += ".save";
        }
        File saveFile = new File(SaveLoad.SAVES_DIR, saveName);
        return saveFile.exists() && saveFile.delete();
    }

    public String formatDisplayName(String filename) {
        return filename.replace(".save", "")
                .replace("_", " alle ore ")
                .replace("-", "/");
    }
}