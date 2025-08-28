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

public class SaveLoad {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String SAVE_DIRECTORY = getSaveDirectory();

    private static final Logger logger = Logger.getLogger(SaveLoad.class.getName());

    private static String getSaveDirectory() {
        String projectPath = System.getProperty("user.dir");
        String savePath = projectPath + "/src/main/resources/saves/";

        // Debug: stampa il percorso per verifica
        System.out.println("Percorso salvataggi: " + savePath);

        File savesDir = new File(savePath);
        if (!savesDir.exists()) {
            savesDir.mkdirs();
        }
        return savePath;
    }

    public static String saveObject(Object object) throws IOException {
        Path savesDir = Paths.get(SAVE_DIRECTORY);
        if (!Files.exists(savesDir)) {
            Files.createDirectories(savesDir);
            logger.info("Directory salvataggi creata: " + SAVE_DIRECTORY);
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
        String filePath = SAVE_DIRECTORY + fileName;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return in.readObject();
        } catch (FileNotFoundException e) {
            throw new IOException("File di salvataggio non trovato: " + fileName, e);
        }
    }

    public static boolean deleteSave(String fileName) {
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