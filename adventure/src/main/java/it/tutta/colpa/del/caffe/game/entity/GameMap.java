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
 * Rappresenta la mappa del gioco utilizzando un grafo diretto. Ogni stanza è un
 * vertice del grafo e le connessioni tra stanze sono rappresentate da archi
 * etichettati con direzioni. La classe gestisce anche la stanza corrente del
 * giocatore.
 *
 * @author giovanni
 */
public class GameMap implements Serializable {

    private final Graph<Room, ArcoGrafo> grafo;
    private Room currentRoom;

    public GameMap() {
        this.grafo = new DefaultDirectedGraph<>(ArcoGrafo.class);
    }

    /**
     * Aggiunge una stanza al grafo senza modificarne lo stato corrente.
     *
     * @param stanza la stanza da aggiungere
     */
    public void aggiungiStanza(Room stanza) {
        this.grafo.addVertex(stanza);
    }

    /**
     * Aggiunge una stanza al grafo e la imposta come stanza corrente.
     *
     * @param stanza la stanza da aggiungere
     * @param current se true, imposta questa stanza come stanza corrente
     */
    public void aggiungiStanza(Room stanza, boolean current) {
        this.grafo.addVertex(stanza);
        this.currentRoom = stanza;
    }

    /**
     * Collega due stanze nel grafo tramite un arco diretto etichettato con una
     * direzione.
     *
     * @param stanzaP stanza di partenza
     * @param stanzaA stanza di arrivo
     * @param d direzione dell'arco
     */
    public void collegaStanze(Room stanzaP, Room stanzaA, Direzione d) {
        this.grafo.addEdge(stanzaP, stanzaA, new ArcoGrafo(d));
    }

    /**
     * Restituisce la stanza raggiungibile dalla stanza corrente nella direzione
     * specificata.
     *
     * @param d la direzione in cui muoversi
     * @return la stanza di arrivo
     * @throws GameMapException se non esiste alcuna stanza in quella direzione
     */
    public Room getStanzaArrivo(Direzione d) throws GameMapException {
        for (ArcoGrafo arco : grafo.outgoingEdgesOf(currentRoom)) {
            if (arco.getEtichetta() == d) {
                return grafo.getEdgeTarget(arco);
            }
        }
        throw new GameMapException("Non puoi andare in quella direzione!");
    }

    /**
     * Permette di prendere l'ascensore dalla stanza corrente e arrivare al
     * piano desiderato.
     *
     * @param pianoArrivo numero del piano desiderato
     * @return la stanza corrispondente al piano
     * @throws GameMapException se non ci si trova in una stanza con ascensore
     */
    public Room prendiAscensore(int pianoArrivo) throws GameMapException {
        if (!Set.of(4, 6, 8, 10, 14, 17, 20, 25).contains(getCurrentRoom().getId())) {
            throw new GameMapException("Non puoi prendere l'ascensore qui");
        }
        return getPiano(pianoArrivo);
    }

    /**
     * Restituisce la stanza corrispondente a un piano specifico.
     *
     * @param numeroP numero del piano
     * @return la stanza del piano richiesto, oppure null se non presente
     * @throws GameMapException se il numero del piano non esiste
     */
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

    /**
     * Restituisce la stanza corrente in cui si trova il giocatore.
     *
     * @return la stanza corrente
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Imposta la stanza corrente del giocatore.
     *
     * @param currentRoom la stanza da impostare come corrente
     */
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
    public String stampaDirezioniPerStanza() {
        StringBuilder msg = new StringBuilder();
        for (Room room : this.grafo.vertexSet()) {
            msg.append("Dalla stanza ").append(room.getName()).append(" puoi andare in: \n");

            // prendo tutti gli archi uscenti dal nodo i-esimo
            Set<ArcoGrafo> uscenti = this.grafo.outgoingEdgesOf(room);
            if (uscenti.isEmpty()) {
                msg.append("nessuna direzione.");
            } else {
                for (ArcoGrafo arco : uscenti) {
                    Direzione direzione = arco.getEtichetta(); // mi prendo l'etichetta
                    Room destinazione = grafo.getEdgeTarget(arco); // mi prendo la stanza in cui arrivo da quella direzione
                    msg.append("  -> ").append(direzione).append(" verso \n").append(destinazione.getName()).append("\n");
                }
            }
        }
        return msg.toString();
    }

    /**
     * Restituisce la lista di stanze raggiungibili dalla stanza corrente
     * seguendo le connessioni definite nel grafo.
     *
     * @return lista di stanze raggiungibili dalla stanza corrente
     * @throws GameMapException se la stanza corrente non è definita
     */
    public List<Room> getStanzeRaggiungibiliDallaStanzaCorrente() {
        if (currentRoom == null) {
            throw new GameMapException("Non puoi andare da nessuna parte!");
        }

        return grafo.outgoingEdgesOf(currentRoom)
                .stream()
                .map(grafo::getEdgeTarget)
                .collect(Collectors.toList());
    }

    /**
     * Restituisce la stanza identificata dall'id specificato.
     *
     * @param id l'identificativo della stanza
     * @return la stanza corrispondente all'id, oppure null se non esiste
     */
    public Room getRoom(int id) {
        for (Room r : this.grafo.vertexSet()) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    /**
     * Restituisce il numero del piano in cui si trova la stanza corrente. I
     * piani sono mappati come segue:
     * <ul>
     * <li>0 -> Dipartimento di Informatica</li>
     * <li>1 -> Primo piano</li>
     * <li>2 -> Secondo piano</li>
     * <li>3 -> Terzo piano</li>
     * <li>4 -> Quarto piano</li>
     * <li>5 -> Quinto piano</li>
     * <li>6 -> Sesto piano</li>
     * <li>7 -> Settimo piano</li>
     * </ul>
     *
     * @return il numero del piano corrente
     * @throws GameMapException se la stanza corrente non è definita o non
     * corrisponde a un piano valido
     */
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
