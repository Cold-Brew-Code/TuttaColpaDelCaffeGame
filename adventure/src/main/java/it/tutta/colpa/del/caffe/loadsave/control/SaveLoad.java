package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveLoad {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String SAVES_DIR = System.getProperty("user.dir") + File.separator +
            "resources" + File.separator + "saves";

    public static void saveGame(GameDescription description) throws IOException {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String nameFile = timestamp + ".save";
        String path = SAVES_DIR + File.separator + nameFile;

        File savesDir = new File(SAVES_DIR);
        if (!savesDir.exists()) {
            savesDir.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(description);
        }
    }

    public static GameDescription loadGame(String saveName) throws IOException, ClassNotFoundException {
        if (!saveName.endsWith(".save")) {
            saveName += ".save";
        }

        File saveFile = new File(SAVES_DIR, saveName);
        if (!saveFile.exists()) {
            throw new FileNotFoundException("File di salvataggio non trovato: " + saveFile.getAbsolutePath());
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            return (GameDescription) ois.readObject();
        }
    }
}