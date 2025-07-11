package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.adventure.entity.*;
import it.tutta.colpa.del.caffe.adventure.utility.*;
import java.util.HashMap;
import java.util.Map;

public class MoveObserver implements GameObserver {

    // enum per tradurre i comandi in direzioni del grafo
    private enum Direzione {
        NORD, SUD, EST, OVEST
    }

    private static final String WALL_MESSAGE = "Da quella parte non si può andare c'è un muro!\nNon hai ancora acquisito i poteri per oltrepassare i muri...";
    // mappa che collega ogni stanza alla sua GameMap (andrà popolata con
    // registerRoomMap)
    private static final Map<Room, GameMap> roomMapRegistry = new HashMap<>();

    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        // 1. ottengo la stanza corrente e la mappa associata
        Room current = description.getCurrentRoom();
        GameMap gameMap = roomMapRegistry.get(current);
        if (gameMap == null)
            return "Mappa non disponibile";

        // 2. converto il comando in direzione (NORD/SUD/EST/OVEST)
        Direzione direction = convertToDirection(parserOutput.getCommand().getType());
        if (direction == null)
            return "";

        // 3. cerco la stanza collegata nella direzione specificata
        Room destination = findDestinationInGraph(gameMap, current, direction);
        if (destination != null) {
            description.setCurrentRoom(destination);
            return ""; // movimento ok
        }

        return WALL_MESSAGE;
    }

    /**
     * Cerca una stanza nel grafo analizzando gli archi uscenti:
     * 1. accede al grafo
     * 2. scansiona tutti gli archi dalla stanza corrente
     * 3. confronta le direzioni degli archi con quella richiesta
     */
    private Room findDestinationInGraph(GameMap gameMap, Room current, Direzione direction) {
        try {
            // accedo al grafo sottostante in GameMap
            var grafo = GameMap.class.getDeclaredField("grafo");
            grafo.setAccessible(true);
            Object grafoInstance = grafo.get(gameMap);

            // ottengo tutti gli archi uscenti dalla stanza corrente
            var edges = (Iterable<?>) grafoInstance.getClass()
                    .getMethod("outgoingEdgesOf", Object.class)
                    .invoke(grafoInstance, current);

            // cerca un arco con la direzione corrispondente
            for (Object arco : edges) {
                Object edgeDirection = arco.getClass()
                        .getMethod("getEtichetta")
                        .invoke(arco);

                if (edgeDirection.equals(direction)) {
                    return (Room) grafoInstance.getClass()
                            .getMethod("getEdgeTarget", Object.class)
                            .invoke(grafoInstance, arco);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // converto da CommandType a Direzione
    private Direzione convertToDirection(CommandType cmd) {
        switch (cmd) {
            case NORD:
                return Direzione.NORD;
            case SOUTH:
                return Direzione.SUD;
            case EAST:
                return Direzione.EST;
            case WEST:
                return Direzione.OVEST;
            default:
                return null;
        }
    }

    // va chiamato per ogni stanza durante l'inizializzazione del gioco
    public static void registerRoomMap(Room room, GameMap map) {
        roomMapRegistry.put(room, map);
    }
}