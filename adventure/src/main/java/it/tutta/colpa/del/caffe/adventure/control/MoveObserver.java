package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.Room;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.Direzione;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;

public class MoveObserver implements GameObserver {

    @Override
    public StringBuilder update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        try {
            CommandType commandType = parserOutput.getCommand().getType();
            String commandName = parserOutput.getCommand().getName();

            // Se non è un comando principale, verifica se è un alias
            if (commandType == null && parserOutput.getCommand().getAlias() != null) {
                commandType = resolveAlias(commandName);
            }

            if (commandType != null) {
                switch (commandType) {
                    case NORD:
                        description.getGameMap().setCurrentRoom(
                                description.getGameMap().getStanzaArrivo(Direzione.NORD));
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
                        break;
                }
            }
        } catch (GameMapException ex) {
            return msg.append(ex.getMessage());
        }

        return msg;
    }

    private CommandType resolveAlias(String alias) {
        if (alias == null)
            return null;

        return switch (alias.toLowerCase()) {
            case "n" -> CommandType.NORD;
            case "s" -> CommandType.SOUTH;
            case "e" -> CommandType.EAST;
            case "w" -> CommandType.WEST;
            case "u" -> CommandType.UP;
            case "d" -> CommandType.DOWN;
            default -> null;
        };
    }
}