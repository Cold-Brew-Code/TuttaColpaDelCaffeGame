package it.tutta.colpa.del.caffe.game.utility;

import java.util.ArrayList;
import java.util.HashSet;
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
                    aliasList.add(item.getName().toLowerCase());

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
        StringBuilder regex=new StringBuilder("[\\s]+(");

        regex.append(
                stopwords.stream()
                        .reduce((a, b) -> a + "|" + b)
                        .orElse(""));

        regex.append(")[\\s]+");
        for (NPC npc : this.NPCs) {
            String npcName = npc.getNome().toLowerCase().replaceAll(regex.toString()," ");

            Set<String> aliasList = new HashSet<>(npc.getAlias());
            aliasList.add(npc.getNome().toLowerCase());

            // provo tutte le possibili sottosequenze di token
            for (int start = 0; start < tokens.length; start++) {
                StringBuilder sb = new StringBuilder();
                for (int end = start; end < tokens.length; end++) {
                    if (!sb.isEmpty()) {
                        sb.append(" ");
                    }
                    sb.append(tokens[end].toLowerCase());
                    String current = sb.toString();

                    if (aliasList.contains(current)) {
                        return npc; // trovato
                    }
                }
            }

        }
        return null; // nessun NPC trovato
    }

    /**
     * Cerca un numero compreso tra 1 e 7 nei token.
     * Se il numero è fuori dal range, lancia una ParserException.
     *
     * @param tokens Array di parole (token) da analizzare.
     * @return Il numero del piano (1–7) se presente, altrimenti -1.
     * @throws ParserException se il numero trovato è fuori dal range 1–7
     */
    private int findPiano(String[] tokens) throws ParserException {
        for (String token : tokens) {
            try {
                int numero = Integer.parseInt(token);
                if (numero >= 0 && numero <= 7) {
                    return numero;
                } else {
                    throw new ParserException("Piano non valido: " + numero + ". I piani validi sono da 1 a 7.");
                }
            } catch (NumberFormatException ignored) {
                // poichè se ci sono caratteri non numerici parseInt lancia subito eccezione in questo modo lo evitiamo
            }
        }
        return -1;
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
        Command cd = checkForCommand(tokens[0]);
        //System.out.println("ho trovato:\n"+ cd+ cd.getAlias()+cd.getName()+cd.getType());
        if (cd == null) {
            throw new ParserException("Errore: comando non riconosciuto o input non valido.");
        }
        NPC npcP = findNpc(tokens);
        int piano= findPiano(tokens);
        if (tokens.length > 1) {
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
            } else if(piano!=-1) {
                // non esiste nessun npc nelle stanze errore
                System.out.println("in parser"+piano);
                return new ParserOutput(cd, piano);
            } else {
                throw new ParserException("Oggetti o NPC non riconosciuti.");
            }
        } else {
            // il semplice comando parla che se ci sono più npc da errore quando si fa talk observer
            // construttore di parserOutput con comadno e null
            return new ParserOutput(cd);
        }
    }
}