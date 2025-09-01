package it.tutta.colpa.del.caffe.loadsave.control;

import java.io.*;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.net.URISyntaxException;
import java.net.URL;

public class SaveLoad {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String SAVE_DIRECTORY_NAME = "TuttaColpaDelCaffeSaves";
    private static final String SAVE_DIRECTORY = getSaveDirectory();

    private static final Logger logger = Logger.getLogger(SaveLoad.class.getName());

    private static String getSaveDirectory() {
        try {
            // Ottieni il percorso della directory dove si trova il JAR
            URL location = SaveLoad.class.getProtectionDomain().getCodeSource().getLocation();
            Path jarPath = Paths.get(location.toURI());
            Path jarDir = jarPath.getParent();

            // Crea il percorso per la nuova cartella di salvataggi
            Path savePath = jarDir.resolve(SAVE_DIRECTORY_NAME);

            // Verifica e crea la cartella se non esiste
            if (!Files.exists(savePath)) {
                Files.createDirectories(savePath);
                logger.info("Directory di salvataggio creata: " + savePath);
            }
            return savePath.toString() + File.separator;
        } catch (URISyntaxException | IOException e) {
            logger.severe("Errore nella creazione della directory di salvataggio: " + e.getMessage());
            return null;
        }
    }

    public static String saveObject(Object object) throws IOException {
        if (SAVE_DIRECTORY == null) {
            throw new IOException("La directory di salvataggio non è disponibile.");
        }

        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fileName = timestamp + ".save";
        String filePath = SAVE_DIRECTORY + fileName;

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(object);
            logger.info("Salvataggio creato: " + filePath);
            return fileName;
        } catch (IOException e) {
            logger.severe("Errore durante il salvataggio: " + e.getMessage());
            throw e;
        }
    }

    public static Object loadObject(String fileName) throws IOException, ClassNotFoundException {
        if (SAVE_DIRECTORY == null) {
            throw new IOException("La directory di salvataggio non è disponibile.");
        }
        String filePath = SAVE_DIRECTORY + fileName;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return in.readObject();
        } catch (FileNotFoundException e) {
            throw new IOException("File di salvataggio non trovato: " + fileName, e);
        }
    }

    public static boolean deleteSave(String fileName) {
        if (SAVE_DIRECTORY == null) {
            logger.warning("Impossibile eliminare il file, la directory di salvataggio non è disponibile.");
            return false;
        }
        File file = new File(SAVE_DIRECTORY + fileName);
        boolean deleted = file.exists() && file.delete();
        if (deleted) {
            logger.info("Salvataggio eliminato: " + fileName);
        } else {
            logger.warning("Tentativo di eliminazione fallito: " + fileName);
        }
        return deleted;
    }

    public static List<String> getSaveFiles() {
        List<String> saveFiles = new ArrayList<>();
        if (SAVE_DIRECTORY == null) {
            return saveFiles;
        }
        File savesDir = new File(SAVE_DIRECTORY);

        if (savesDir.exists() && savesDir.isDirectory()) {
            File[] files = savesDir.listFiles((dir, name) -> name.endsWith(".save"));
            if (files != null) {
                for (File file : files) {
                    saveFiles.add(file.getName());
                }
            }
        }
        return saveFiles;
    }
}