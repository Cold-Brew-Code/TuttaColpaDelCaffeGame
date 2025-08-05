package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveLoad {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    public static final String SAVES_DIR = System.getProperty("user.dir") + File.separator +
            "src" + File.separator + "main" + File.separator + "resources" + File.separator + "saves";

    static {
        File savesDir = new File(SAVES_DIR);
        if (!savesDir.exists()) {
            System.out.println("[INFO] Creazione cartella salvataggi: " + SAVES_DIR);
            if (!savesDir.mkdirs()) {
                System.err.println("[ERR] Impossibile creare la cartella salvataggi: " + SAVES_DIR);
                throw new ExceptionInInitializerError("Impossibile creare cartella salvataggi");
            }
        } else if (!savesDir.isDirectory()) {
            System.err.println("[ERR] Il percorso specificato non è una directory: " + SAVES_DIR);
            throw new ExceptionInInitializerError("Percorso salvataggi non è una directory");
        }
    }

    public static void saveGame(GameDescription description) throws IOException {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String nameFile = timestamp + ".save";
        String path = SAVES_DIR + File.separator + nameFile;

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