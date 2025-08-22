package it.tutta.colpa.del.caffe.game.control;

import java.util.Set;

import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.game.utility.*;

public class MoveObserver implements GameObserver {

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
                            case DOWN -> description.getGameMap().setCurrentRoom(
                                    description.getGameMap().getStanzaArrivo(Direzione.SOTTO));
                            default -> throw new Exception("Errore generico");
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
                    if (hasUsedRestroom(description)) {
                        description.setStatus(GameStatus.BAGNO_USATO);
                        msg.append("\n\nHai usato finalmente il bagno, liberando i tuoi impellenti bisogni.\nAdesso non ti resta che sostenere il tuo esame.\nCorri!!!!");
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

    private boolean hasUsedRestroom(GameDescription description) {
        return ((description.getCurrentRoom().getId() == 11 && (GameUtils.getObjectFromInventory(description.getInventory(), 13)) != null)
                || (description.getCurrentRoom().getId() == 28)) && !(description.getStatus() == GameStatus.BAGNO_USATO);
    }
}
