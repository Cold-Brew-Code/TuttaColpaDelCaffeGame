package it.tutta.colpa.del.caffe.adventure.control;

import java.util.Set;

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
        if (Set.of(CommandType.NORD, CommandType.SOUTH, CommandType.EAST, CommandType.WEST, CommandType.UP, CommandType.DOWN).contains(commandType)) {
            try {
                 System.out.println("Comando: " + commandType+"\n"+commandName +"\n proca1"+ parserOutput.getCommand().getType());

                if (commandType != null) {
                    switch (commandType) {
                        case NORD:
                            description.getGameMap().setCurrentRoom(
                                    description.getGameMap().getStanzaArrivo(Direzione.NORD)
                            );
                            break;
                        case SOUTH:
                            description.getGameMap().setCurrentRoom(
                                    description.getGameMap().getStanzaArrivo(Direzione.SUD));
                            break;
                        case EAST:
                            description.getGameMap().setCurrentRoom(
                                    description.getGameMap().getStanzaArrivo(Direzione.EST));
                            break;
                        case WEST:
                            description.getGameMap().setCurrentRoom(
                                    description.getGameMap().getStanzaArrivo(Direzione.OVEST));
                            break;
                        case UP:
                            System.out.println("Stanza corrente: " + description.getGameMap().getCurrentRoom().getName());
                            description.getGameMap().debug();
                            description.getGameMap().setCurrentRoom(
                                    description.getGameMap().getStanzaArrivo(Direzione.SOPRA));

                            break;
                        case DOWN:
                            description.getGameMap().setCurrentRoom(
                                    description.getGameMap().getStanzaArrivo(Direzione.SOTTO));
                            break;
                        default:
                            throw new Exception("Errore generico");
                    }
                    msg.append("\n").append(description.getCurrentRoom().getDescription());
                }
            } catch (GameMapException ex) {
                msg.append(ex.getMessage());
                msg.append(ex.getMessage());
            } catch (Exception ex) {
                msg.append(ex.getMessage());
            }
        }
        return msg.toString();
    }


}