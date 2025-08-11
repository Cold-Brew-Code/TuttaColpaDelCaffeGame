/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.game.utility.ArcoGrafo;
import it.tutta.colpa.del.caffe.game.utility.Direzione;

/**
 *
 * @author giovanni
 */
public class GameMap implements Serializable {

    private final Graph<Room, ArcoGrafo> grafo;
    private Room currentRoom;

    public GameMap() {
        this.grafo = new DefaultDirectedGraph<>(ArcoGrafo.class);
    }

    public void aggiungiStanza(Room stanza) {
        this.grafo.addVertex(stanza);
    }

    public void aggiungiStanza(Room stanza, boolean current) {
        this.grafo.addVertex(stanza);
        this.currentRoom = stanza;
    }

    public void collegaStanze(Room stanzaP, Room stanzaA, Direzione d) {
        this.grafo.addEdge(stanzaP, stanzaA, new ArcoGrafo(d));
    }

    public Room getStanzaArrivo(Direzione d) throws GameMapException {
        for (ArcoGrafo arco : grafo.outgoingEdgesOf(currentRoom)) {
            if (arco.getEtichetta() == d) {
                return grafo.getEdgeTarget(arco);
            }
        }
        throw new GameMapException("Non puoi andare in quella direzione!");
    }

    public Room prendiAscensore(int pianoArrivo) throws GameMapException {
        if (!Set.of(4, 6, 8, 10, 14, 17, 20, 25).contains(getCurrentRoom().getId())) {
            throw new GameMapException("Non puoi prendere l'ascensore qui");
        }
        return getPiano(pianoArrivo);
    }

    public Room getPiano(int numeroP) throws GameMapException {
        final String piano;
        switch (numeroP) {
            case 0 ->
                piano = "Dipartimento di Informatica";
            case 1 ->
                piano = "Primo";
            case 2 ->
                piano = "Secondo";
            case 3 ->
                piano = "Terzo";
            case 4 ->
                piano = "Quarto";
            case 5 ->
                piano = "Quinto";
            case 6 ->
                piano = "Sesto";
            case 7 ->
                piano = "Settimo";
            default ->
                throw new GameMapException("Piano specificato inesistente.");
        }
        return this.grafo.vertexSet()
                .stream()
                .filter(r -> r.getName().equals(new StringBuilder(piano + " piano").toString())
                || r.getName().equals(piano))
                .findFirst()
                .orElse(null);

    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void debug() {
        // Stampa i nodi
        System.out.println("Vertici: " + this.grafo.vertexSet());

        // Stampa gli archi
        System.out.println("Archi:");
        for (ArcoGrafo edge : this.grafo.edgeSet()) {
            System.out.println(this.grafo.getEdgeSource(edge) + " -" + edge.getEtichetta().toString() + "- " + this.grafo.getEdgeTarget(edge)
                    + "\n entrambiel?\t" + this.grafo.getEdgeTarget(edge).isDeniedEntry());
        }
    }

    /**
     * Stampa per ogni stanza presente nella mappa di gioco tutte le direzioni
     * disponibili e le rispettive stanze di destinazione.
     * <p>
     * Per ogni nodo (stanza) del grafo, vengono analizzati gli archi uscenti e
     * viene stampata una lista delle direzioni in cui è possibile muoversi
     * dalla stanza corrente, insieme al nome della stanza di destinazione per
     * ciascuna direzione.
     * </p>
     * <p>
     * Se una stanza non ha direzioni disponibili, viene indicato
     * esplicitamente.
     * </p>
     *
     * Esempio di output:
     * <pre>
     * Dalla stanza Aula puoi andare in:
     *   -> NORD verso
     * Biblioteca
     *
     *   -> EST verso
     * Corridoio
     * </pre>
     */
    public void stampaDirezioniPerStanza() {
        for (Room room : this.grafo.vertexSet()) {
            System.out.print("Dalla stanza " + room.getName() + " puoi andare in: \n");

            // prendo tutti gli archi uscenti dal nodo i-esimo
            Set<ArcoGrafo> uscenti = this.grafo.outgoingEdgesOf(room);
            if (uscenti.isEmpty()) {
                System.out.println("nessuna direzione.");
            } else {
                for (ArcoGrafo arco : uscenti) {
                    Direzione direzione = arco.getEtichetta(); // mi prendo l'etichetta
                    Room destinazione = grafo.getEdgeTarget(arco); // mi prendo la stanza in cui arrivo da quella direzione
                    System.out.println("  -> " + direzione + " verso \n" + destinazione.getName() + "\n");
                }
            }
        }
    }

    public List<Room> getStanzeRaggiungibiliDallaStanzaCorrente() {
        if (currentRoom == null) {
            throw new GameMapException("Non puoi andare da nessuna parte!");
        }

        return grafo.outgoingEdgesOf(currentRoom)
                .stream()
                .map(grafo::getEdgeTarget)
                .collect(Collectors.toList());
    }

    public Room getRoom(int id) {
        for (Room r : this.grafo.vertexSet()) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public int getPianoCorrente() throws GameMapException {
        if (currentRoom == null) {
            throw new GameMapException("Stanza corrente non definita.");
        }

        String nome = currentRoom.getName().toLowerCase();

        if (nome.contains("dipartimento di informatica")) {
            return 0;
        } else if (nome.contains("primo")) {
            return 1;
        } else if (nome.contains("secondo")) {
            return 2;
        } else if (nome.contains("terzo")) {
            return 3;
        } else if (nome.contains("quarto")) {
            return 4;
        } else if (nome.contains("quinto")) {
            return 5;
        } else if (nome.contains("sesto")) {
            return 6;
        } else if (nome.contains("settimo")) {
            return 7;
        } else {
            throw new GameMapException("Non puoi prendere l'ascensore qui");
        }
    }

}
