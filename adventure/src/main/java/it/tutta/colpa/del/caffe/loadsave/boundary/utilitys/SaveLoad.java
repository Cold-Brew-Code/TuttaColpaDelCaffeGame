package it.tutta.colpa.del.caffe.loadsave.boundary.utilitys;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileNotFoundException;

public class SaveLoad {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String SAVES_DIR = System.getProperty("user.dir") + File.separator +
            "resources" + File.separator + "saves";

    public static void saveGame(GameDescription description) throws IOException {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String nameFile = timestamp + ".save";
        String path = SAVES_DIR + File.separator + nameFile;

        // Crea la directory se non esiste
        File savesDir = new File(SAVES_DIR);
        if (!savesDir.exists()) {
            savesDir.mkdirs();
        }

        try (FileOutputStream outFile = new FileOutputStream(path);
                ObjectOutputStream objFile = new ObjectOutputStream(outFile)) {
            objFile.writeObject(description);
            System.out.println("Salvataggio completato in: " + path);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
            throw e;
        }
    }

    public static GameDescription loadGame(String saveName) throws IOException, ClassNotFoundException {
        // Verifica che il nome del file termini con .save
        if (!saveName.endsWith(".save")) {
            saveName += ".save";
        }

        String path = SAVES_DIR + File.separator + saveName;
        File saveFile = new File(path);

        if (!saveFile.exists()) {
            throw new FileNotFoundException("File di salvataggio non trovato: " + path);
        }

        try (FileInputStream inFile = new FileInputStream(path);
                ObjectInputStream objFile = new ObjectInputStream(inFile)) {

            GameDescription loadedGame = (GameDescription) objFile.readObject();
            System.out.println("Caricamento completato da: " + path);
            return loadedGame;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento: " + e.getMessage());
            throw e;
        }
    }

    public static GameDescription loadLatestSave() throws IOException, ClassNotFoundException {
        File savesDir = new File(SAVES_DIR);
        if (!savesDir.exists() || !savesDir.isDirectory()) {
            throw new FileNotFoundException("Cartella salvataggi non trovata");
        }

        File[] saveFiles = savesDir.listFiles((dir, name) -> name.endsWith(".save"));
        if (saveFiles == null || saveFiles.length == 0) {
            throw new FileNotFoundException("Nessun salvataggio trovato");
        }

        // Trova il file più recente
        File latestFile = saveFiles[0];
        for (File file : saveFiles) {
            if (file.lastModified() > latestFile.lastModified()) {
                latestFile = file;
            }
        }

        return loadGame(latestFile.getName());
    }
}