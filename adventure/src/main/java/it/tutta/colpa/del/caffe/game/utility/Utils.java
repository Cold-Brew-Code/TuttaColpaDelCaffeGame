/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe che contenente metodi statici per operazioni su file e
 * stringhe. Fornisce funzionalità di caricamento di liste da file e parsing di
 * stringhe con rimozione di stopwords.
 *
 * @author giova
 */
public class Utils {

    /**
     * Carica il contenuto di un file in un {@link Set} di stringhe.
     * <p>
     * Ogni riga del file viene letta, convertita in minuscolo e aggiunta al
     * set. Le righe duplicate saranno automaticamente eliminate grazie alla
     * natura del {@link Set}.
     * </p>
     *
     * @param file il file da leggere
     * @return un {@link Set} contenente tutte le righe del file in minuscolo
     * @throws IOException se si verifica un errore durante la lettura del file
     */
    public static Set<String> loadFileListInSet(File file) throws IOException {
        Set<String> set = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.ready()) {
            set.add(reader.readLine().trim().toLowerCase());
        }
        reader.close();
        return set;
    }

    /**
     * Parsea una stringa in token, rimuovendo eventuali stopwords.
     * <p>
     * La stringa viene convertita in minuscolo e divisa in parole utilizzando
     * gli spazi come separatori. Tutte le parole presenti nell'insieme
     * {@code stopwords} vengono scartate.
     * </p>
     *
     * @param string la stringa da parsare
     * @param stopwords un {@link Set} contenente le parole da ignorare
     * @return una {@link List} di token filtrati, mantenendo l'ordine originale
     */
    public static List<String> parseString(String string, Set<String> stopwords) {
        List<String> tokens = new ArrayList<>();
        String[] split = string.toLowerCase().split("\\s+");
        for (String t : split) {
            if (!stopwords.contains(t)) {
                tokens.add(t);
            }
        }
        return tokens;
    }
}
