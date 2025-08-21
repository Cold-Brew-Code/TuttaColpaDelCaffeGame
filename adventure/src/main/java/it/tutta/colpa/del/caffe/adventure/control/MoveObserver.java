package it.tutta.colpa.del.caffe.adventure.control;

import java.util.Set;

import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.Direzione;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 * Osservatore che gestisce i comandi di movimento del giocatore.
 * <p>
 * Implementa l'interfaccia {@link GameObserver} e permette di muoversi tra le
 * stanze della mappa tramite i comandi direzionali
 * ({@code NORD, SOUTH, EAST, WEST, UP, DOWN}).
 * </p>
 *
 * <p>
 * Funzionalità principali:</p>
 * <ul>
 * <li>Gestione del movimento nelle quattro direzioni cardinali.</li>
 * <li>Gestione della salita/discesa tra i piani, compreso l'uso dell'ascensore
 * tramite {@link LiftObserver}.</li>
 * <li>Controllo dell’accesso alle stanze: se l’ingresso è negato, viene
 * restituito un messaggio informativo (es. stanza chiusa o presenza di
 * ostacoli).</li>
 * <li>Gestione delle eccezioni dovute a errori di mappa o comandi non
 * validi.</li>
 * </ul>
 *
 * <p>
 * Esempi di comportamento:</p>
 * <ul>
 * <li>Se il giocatore prova a salire al piano 5 partendo dal 3 con {@code UP},
 * il movimento è valido e viene invocato l'ascensore.</li>
 * <li>Se il giocatore prova a scendere al piano 2 partendo dal 5 con
 * {@code DOWN}, il movimento è valido.</li>
 * <li>Se il comando di piano non è coerente (es. {@code UP} ma targetFloor <
 * piano corrente), viene restituito un messaggio di errore.</li> <li>Se una
 * stanza è chiusa o bloccata, viene mostrato un messa ggio (es. "Ops la stanza
 * è chiusa" oppure "C'è la fila! non puoi passare...").</li>
 * </ul>
 *
 * @author giovanni
 */
public class MoveObserver implements GameObserver {

    /**
     * Gestisce il comando di movimento del giocatore, aggiornando la posizione
     * corrente sulla mappa o restituendo un messaggio di errore se lo
     * spostamento non è possibile.
     *
     * @param description lo stato corrente del gioco, contenente mappa, stanze
     * e posizione attuale
     * @param parserOutput il risultato del parsing del comando dell'utente, che
     * specifica direzione o piano
     * @return una stringa con il messaggio da mostrare al giocatore
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        CommandType commandType = parserOutput.getCommand().getType();
        String commandName = parserOutput.getCommand().getName();
        System.out.println(parserOutput.getPiano());
        if (Set.of(CommandType.NORD, CommandType.SOUTH, CommandType.EAST, CommandType.WEST, CommandType.UP, CommandType.DOWN).contains(commandType)) {
            boolean close = false;
            try {
                System.out.println("Comando: " + commandType + "\n" + commandName + "\n proca1" + parserOutput.getCommand().getType());
                if (commandType != null) {
                    if (Set.of(CommandType.UP, CommandType.DOWN).contains(commandType) && parserOutput.getPiano() >= 0) {
                        int currPiano = description.getGameMap().getPianoCorrente();
                        int targetFloor = parserOutput.getPiano();
                        boolean validMove = false;
                        if (commandType == CommandType.UP && targetFloor > currPiano) {
                            validMove = true;

                        } else if (commandType == CommandType.DOWN && targetFloor < currPiano) {
                            validMove = true;

                        }
                        if (!validMove) {
                            msg.append("Errore: impossibile eseguire il comando, piano non coerente con la posizione attuale.\n");

                        } else {
                            LiftObserver ob = new LiftObserver();
                            parserOutput.setCommand(new Command("ascensore"));
                            msg.append(ob.update(description, parserOutput));
                        }

                    } else {
                        switch (commandType) {
                            case NORD -> {
                                if (description.getGameMap().getStanzaArrivo(Direzione.NORD).isDeniedEntry()) {

                                    description.getGameMap().setCurrentRoom(
                                            description.getGameMap().getStanzaArrivo(Direzione.NORD)
                                    );
                                } else {
                                    close = true;
                                }
                            }
                            case SOUTH -> {
                                if (description.getGameMap().getStanzaArrivo(Direzione.SUD).isDeniedEntry()) {
                                    description.getGameMap().setCurrentRoom(
                                            description.getGameMap().getStanzaArrivo(Direzione.SUD));
                                } else {
                                    close = true;
                                }
                            }
                            case EAST -> {
                                if (description.getGameMap().getStanzaArrivo(Direzione.EST).isDeniedEntry()) {
                                    description.getGameMap().setCurrentRoom(
                                            description.getGameMap().getStanzaArrivo(Direzione.EST));
                                } else {
                                    close = true;
                                }
                            }
                            case WEST -> {
                                if (description.getGameMap().getStanzaArrivo(Direzione.OVEST).isDeniedEntry()) {
                                    description.getGameMap().setCurrentRoom(
                                            description.getGameMap().getStanzaArrivo(Direzione.OVEST));
                                } else {
                                    close = true;
                                }
                            }
                            case UP -> {
                                System.out.println("Stanza corrente: " + description.getGameMap().getCurrentRoom().getName());
                                description.getGameMap().debug();
                                description.getGameMap().setCurrentRoom(
                                        description.getGameMap().getStanzaArrivo(Direzione.SOPRA));
                            }
                            case DOWN ->
                                description.getGameMap().setCurrentRoom(
                                        description.getGameMap().getStanzaArrivo(Direzione.SOTTO));
                            default ->
                                throw new Exception("Errore generico");
                        }
                        if (close) {
                            if (description.getGameMap().getStanzaArrivo(Direzione.OVEST).getId() == 11 && commandType == CommandType.WEST) {
                                msg.append("C'è la fila! non puoi passare...");
                            } else {
                                msg.append("Ops la stanza è chiusa");
                            }
                        } else {
                            msg.append("\n").append(description.getCurrentRoom().getDescription());

                        }
                    }
                }
            } catch (GameMapException ex) {
                msg.append(ex.getMessage());
            } catch (Exception ex) {
                msg.append(ex.getMessage());
            }
        }
        return msg.toString();
    }

}
