package it.tutta.colpa.del.caffe.game.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.NPC;

/**
 * @author giovanni
 */
public class Parser {

    private final Set<String> stopwords;
    private final List<Command> commands;
    private final List<GeneralItem> items;
    private final List<NPC> NPCs;

    public Parser(Set<String> stopwords, List<Command> commands, List<GeneralItem> items, List<NPC> NPCs) {
        this.stopwords = stopwords;
        this.commands = commands;
        this.items = items;
        this.NPCs = NPCs;
    }

    // Cerca se un token corrisponde a un comando o un suo alias
    private Command checkForCommand(String token) {
        return commands.stream()
                .filter(cmd -> cmd.getName().equals(token) || cmd.getAlias().contains(token))
                .findFirst()
                .orElse(null);
    }

    // Cerca in tutti gli oggetti se la sequenza di token combacia con il nome o alias
    private String[] findItem(String[] token) {
        List<String> findObj = new ArrayList<>();

        this.items.stream()
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

    // matcha il token con l'espressione regoalre e se non va prende la stringa successiva
    private boolean tentativo(Pattern p, String[] token) {
        // provo tutte le poossibili sottosequenze di token partendo dalla prima posizione poi dalla seconda ecc 
        for (int start = 0; start < token.length; start++) {
            StringBuilder sb = new StringBuilder();
            for (int end = start; end < token.length; end++) {
                if (!sb.isEmpty()) {
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

    private NPC findNpc(String[] tokens) {
        for (NPC npc : this.NPCs) {
            String npcName = npc.getNome().toLowerCase();

            // provo tutte le possibili sottosequenze di token
            for (int start = 0; start < tokens.length; start++) {
                StringBuilder sb = new StringBuilder();
                for (int end = start; end < tokens.length; end++) {
                    if (!sb.isEmpty()) {
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
        return null; // nessun NPC trovato
    }

    /**
     * @param command
     * @return
     */
    public ParserOutput parse(String command) {
        List<String> list = Utils.parseString(command, stopwords);
        String[] tokens = list.toArray(new String[0]);

        Command cd = checkForCommand(tokens[0]);// xkè il comando è sempre in prima posizione
        if (cd != null) {
            NPC npcP = findNpc(tokens);
            if (tokens.length > 0) {
                String[] obj = findItem(tokens);
                if (obj.length == 1) {
                    // chiamo il construttore di parserOutput con solo un oggetto
                    return new ParserOutput(cd, items.stream().filter(item
                            -> item.getName().equals(obj[0])
                    ).findFirst().orElse(null));

                } else if (obj.length == 2) {
                    // chiamo il costruttore che ha 2 oggetti

                    // prendo il primo oggetto
                    GeneralItem findItem1 = items.stream().filter(item
                            -> item.getName().equals(obj[0])
                    ).findFirst().orElse(null);

                    // prendo il secondo oggetto
                    GeneralItem findItem2 = items.stream().filter(item
                            -> item.getName().equals(obj[1])
                    ).findFirst().orElse(null);

                    return new ParserOutput(cd, findItem1, findItem2);


                } else if (npcP != null) {
                    // non ha trovato niente quindi non è stato indicato nessun oggetto provo con gli npc
                    // chiama il costruttore con comando ed NPC
                    return new ParserOutput(cd, npcP);
                } else {
                    // non esiste nessun npc nelle stanze errore
                    return new ParserOutput(cd, (NPC) null);
                }
            } else {
                // il semplice comando parla che se ci sono più npc da errore quando si fa talk observer
                // construttore di parserOutput con comadno e null
                return new ParserOutput(cd);
            }
        } else {
            // bocciato comando inesistente
            // costruttore di parseOutput con tutto null o errore
            return new ParserOutput(null, (GeneralItem) null);
        }
    }

}
