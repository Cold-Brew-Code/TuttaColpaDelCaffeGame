package it.tutta.colpa.del.caffe.loadsave.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SaveLoad {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    public static final String SAVES_DIR = Paths.get(
            System.getProperty("user.dir"),
            "TuttaColpaDelCaffeGame",
            "adventure",
            "src",
            "main",
            "resources",
            "saves").toString();

    static {
        File savesDir = new File(SAVES_DIR);
        System.out.println("Percorso salvataggi assoluto: " + savesDir.getAbsolutePath());

        if (!savesDir.exists()) {
            System.out.println("Creazione cartella salvataggi...");
            if (savesDir.mkdirs()) {
                System.out.println("Cartella creata con successo");
            } else {
                System.err.println("ERRORE: Impossibile creare la cartella salvataggi");
            }
        }
    }

    public static String saveGame(GameDescription description) throws IOException {
        File savesDir = new File(SAVES_DIR);
        System.out.println("DEBUG - Tentativo salvataggio in: " + savesDir.getAbsolutePath());

        if (!savesDir.exists() && !savesDir.mkdirs()) {
            throw new IOException("Impossibile creare cartella salvataggi");
        }

        String fileName = "save_" + LocalDateTime.now().format(FORMATTER) + ".save";
        File saveFile = new File(savesDir, fileName);

        System.out.println("DEBUG - Creazione file: " + saveFile.getAbsolutePath());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            oos.writeObject(description);
            System.out.println("DEBUG - Salvataggio riuscito");
            return saveFile.getAbsolutePath();
        }
    }

    public static GameDescription loadGame(String saveName) throws IOException, ClassNotFoundException {
        if (!saveName.endsWith(".save")) {
            saveName += ".save";
        }

        File saveFile = new File(SAVES_DIR, saveName);
        System.out.println("Caricamento da: " + saveFile.getAbsolutePath());

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            return (GameDescription) ois.readObject();
        }
    }

    public static List<String> getSaveList() {
        File savesDir = new File(SAVES_DIR);
        String[] saveFiles = savesDir.list((dir, name) -> name.endsWith(".save"));

        if (saveFiles == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(saveFiles);
    }
}