package it.tutta.colpa.del.caffe.adventure.control;

import java.util.Set;

import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.Direzione;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

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
                        LiftObserver ob = new LiftObserver();
                        parserOutput.setCommand(new Command("ascensore"));
                        msg.append(ob.update(description, parserOutput));
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
                            msg.append("Ops la stanza è chiusa");
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
