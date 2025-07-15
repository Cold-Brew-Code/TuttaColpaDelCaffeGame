
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.NPC;
import it.tutta.colpa.del.caffe.game.entity.Room;


/**
 *
 * @author giovanni
 */
public class Parser {

    private final Set<String> stopwords;
    private final List<Command> commands;

    public Parser(Set<String> stopwords, List<Command> commands) {
        this.stopwords = stopwords;
        this.commands = commands;
    }

    // Cerca se un token corrisponde a un comando o un suo alias
    private Command checkForCommand(String token) {
        return commands.stream()
                .filter(cmd -> cmd.getName().equals(token) || cmd.getAlias().contains(token))
                .findFirst()
                .orElse(null);
    }

    // Cerca in tutti gli oggetti se la sequenza di token combacia con il nome o alias
    private String[] findItem(String[] token, List<GeneralItem> items) {
        List<String> findObj = new ArrayList<>();

        items.stream()
                .filter(item -> {
                    // Copio la lista di alias + nome (senza modificare l'originale)
                    List<String> aliasList = new ArrayList<>(item.getAlias());
                    aliasList.add(item.getName());

                    // Creo regex con tutti gli alias/nome (quote per evitare problemi con caratteri speciali)
                    String regex = aliasList.stream()
                            .reduce((a, b) -> a + "|" + b)
                            .orElse("");
                    Pattern p = Pattern.compile(regex);

                    // Se almeno una combinazione dei token matcha, questo oggetto è "trovato"
                    return tentativo(p, token);
                })
                .forEach(item -> findObj.add(item.getName())); // per ogni oggetto trovato aggiungo il suo nome alla lista 

        return findObj.toArray(new String[0]); // converto la lista array di 
    }

    // metcha il il token con l'espressione regoalre e se non va prende la stringa successiva 
    private boolean tentativo(Pattern p, String[] token) {
        // provo tutte le poossibili sottosequenze di token partendo dalla prima posizione poi dalla seconda ecc 
        for (int start = 0; start < token.length; start++) {
            StringBuilder sb = new StringBuilder();
            for (int end = start; end < token.length; end++) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(token[end]);// aggiungo la stringa successiva a sb
                String current = sb.toString(); // restituisco la sequenza di caratteri di sb 
                if (p.matcher(current).matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    private NPC findNpc(String[] tokens, List<Room> rooms) {
        for (Room room : rooms) {
            for (NPC npc : room.getNPCs()) {
                String npcName = npc.getNome().toLowerCase();

                // provo tutte le possibili sottosequenze di token
                for (int start = 0; start < tokens.length; start++) {
                    StringBuilder sb = new StringBuilder();
                    for (int end = start; end < tokens.length; end++) {
                        if (sb.length() > 0) {
                            sb.append(" ");
                        }
                        sb.append(tokens[end].toLowerCase());
                        String current = sb.toString();

                        if (current.equals(npcName)) {
                            return npc; // trovato
                        }
                    }
                }
            }
        }
        return null; // nessun NPC trovato
    }


    /**
     *
     * @param command
     * @param commands
     * @param objects
     * @param inventory
     * @return
     */

    public ParserOutput parse(String command, List<Command> commands, List<GeneralItem> objects, List<GeneralItem> inventory) {
        List<String> list = Utils.parseString(command, stopwords);
        String[] tokens = list.toArray(new String[0]);
        return new ParserOutput(null, null);

    }

}
