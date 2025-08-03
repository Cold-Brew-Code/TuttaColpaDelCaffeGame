package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.Room;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.Direzione;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;

import java.util.Set;

public class MoveObserver implements GameObserver {

    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        CommandType commandType = parserOutput.getCommand().getType();
        String commandName = parserOutput.getCommand().getName();
        if (Set.of(CommandType.NORD, CommandType.SOUTH, CommandType.EAST, CommandType.WEST, CommandType.UP, CommandType.DOWN).contains(parserOutput.getCommand().getType())) {
            try {

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
                            int currentFloor = 1;
                            Room currentRoom = description.getGameMap().getCurrentRoom();
                            for (int floor = 1; floor <= 7; floor++) {
                                if (description.getGameMap().getPiano(floor).equals(currentRoom)) {
                                    currentFloor = floor;
                                    break;
                                }
                            }
                            description.getGameMap().setCurrentRoom(
                                    description.getGameMap().prendiAscensore(currentFloor + 1));
                            break;
                        case DOWN:
                            currentFloor = 1;
                            currentRoom = description.getGameMap().getCurrentRoom();
                            for (int floor = 1; floor <= 7; floor++) {
                                if (description.getGameMap().getPiano(floor).equals(currentRoom)) {
                                    currentFloor = floor;
                                    break;
                                }
                            }
                            description.getGameMap().setCurrentRoom(
                                    description.getGameMap().prendiAscensore(currentFloor - 1));
                            break;
                        default:
                            throw new Exception("Errore generico");
                    }
                    msg.append("\n").append(description.getCurrentRoom().getDescription());
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