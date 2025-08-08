package it.tutta.colpa.del.caffe.game.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.NPC;
import it.tutta.colpa.del.caffe.game.exception.ParserException;

/**
 * La classe Parser è responsabile dell'analisi del testo di input dell'utente.
 * Interpreta i comandi, gli oggetti e gli NPC menzionati, trasformando una
 * stringa di testo grezza in un oggetto {@link ParserOutput} strutturato
 * che può essere facilmente gestito dalla logica del gioco.
 *
 * @author giovanni
 */
public class Parser {

    private final Set<String> stopwords;
    private final List<Command> commands;
    private final List<GeneralItem> items;
    private final List<NPC> NPCs;

    /**
     * Costruisce un nuovo Parser.
     *
     * @param stopwords Un insieme di parole comuni (es. "il", "un") da ignorare durante l'analisi.
     * @param commands  La lista di tutti i comandi validi nel gioco.
     * @param items     La lista di tutti gli oggetti (GeneralItem) presenti nel gioco.
     * @param NPCs      La lista di tutti i personaggi non giocanti (NPC) presenti nel gioco.
     */
    public Parser(Set<String> stopwords, List<Command> commands, List<GeneralItem> items, List<NPC> NPCs) {
        this.stopwords = stopwords;
        this.commands = commands;
        this.items = items;
        this.NPCs = NPCs;
    }

    /**
     * Controlla se un dato token corrisponde a un comando conosciuto o a uno dei suoi alias.
     *
     * @param token La stringa da verificare.
     * @return L'oggetto {@link Command} corrispondente se trovato, altrimenti {@code null}.
     */
    private Command checkForCommand(String token) throws ParserException{
        Command c = commands.stream()
                .filter(cmd -> cmd.getName().equals(token) || cmd.getAlias().contains(token))
                .findFirst()
                .orElse(null);
        if(c==null) throw new ParserException("Il comando che hai inserito non è valido!");
        return c;
    }

    /**
     * Cerca corrispondenze di oggetti all'interno di un array di token.
     * Il metodo confronta tutte le sottosequenze dei token con i nomi e gli alias degli oggetti di gioco.
     *
     * @param token L'array di token derivato dall'input dell'utente.
     * @return Un array di stringhe contenente i nomi degli oggetti trovati.
     */
    private String[] findItem(String[] token) throws ParserException  {
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
        //if(findObj.isEmpty()) throw new ParserException("nome oggetto non valido!");
        return findObj.toArray(new String[0]); // converto la lista array di 
    }

    /**
     * Metodo di supporto che verifica se una qualsiasi sottosequenza contigua di token corrisponde a un pattern regex.
     *
     * @param p     Il {@link Pattern} regex compilato da confrontare.
     * @param token L'array di token da esaminare.
     * @return {@code true} se viene trovata una corrispondenza, altrimenti {@code false}.
     */
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

    /**
     * Cerca un NPC specifico all'interno di un array di token.
     * Confronta le sottosequenze dei token con i nomi degli NPC.
     *
     * @param tokens L'array di token derivato dall'input dell'utente.
     * @return L'oggetto {@link NPC} trovato, o {@code null} se nessun NPC corrisponde.
     */
    private NPC findNpc(String[] tokens) {
        for (NPC npc : this.NPCs) {
            String regex = stopwords.stream()
                    .map(s -> java.util.regex.Pattern.quote(s))  // per evitare problemi con caratteri speciali
                    .reduce((a, b) -> a + "|" + b)
                    .orElse("");

            String npcName = npc.getNome().toLowerCase().replaceAll(regex,"");

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
                        System.out.println(npcName);
                        return npc; // trovato
                    }
                }
            }

        }
        return null; // nessun NPC trovato
    }

    /**
     * Analizza una stringa di comando dell'utente, la suddivide in token e identifica
     * il comando principale, gli oggetti e/o gli NPC a cui si riferisce.
     *
     * @param command La stringa di input completa fornita dall'utente.
     * @return Un oggetto {@link ParserOutput} che incapsula il risultato dell'analisi.
     * Questo oggetto conterrà il comando identificato ed eventuali oggetti o NPC
     * associati. Se il comando non è valido, l'oggetto ParserOutput lo indicherà.
     */
    public ParserOutput parse(String command) throws ParserException {
        List<String> list = Utils.parseString(command, stopwords);
        String[] tokens = list.toArray(new String[0]);
        if (tokens.length==0){
            throw new ParserException("Il comando che hai inserito non è valido!");
        }
        Command cd = checkForCommand(tokens[0]);// xkè il comando è sempre in prima posizione
        System.out.println("ho trovato:\n"+ cd+ cd.getAlias()+cd.getName()+cd.getType());
        if (cd != null) {
            NPC npcP = findNpc(tokens);
            if (tokens.length > 1) {
                String[] obj = findItem(tokens);
                if (obj.length == 1) {
                    System.out.println("0");

                    // chiamo il construttore di parserOutput con solo un oggetto
                    return new ParserOutput(cd, items.stream().filter(item
                            -> item.getName().equals(obj[0])
                    ).findFirst().orElse(null));

                } else if (obj.length == 2) {
                    System.out.println("1");
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
            System.err.println("Errore: comando non riconosciuto o input non valido.");
            return new ParserOutput(null, (GeneralItem) null);
        }
    }

}