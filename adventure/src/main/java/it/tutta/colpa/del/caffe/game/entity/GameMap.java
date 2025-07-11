/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.adventure.utility.ArcoGrafo;
import it.tutta.colpa.del.caffe.adventure.utility.Direzione;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.Graph;

import java.io.Serializable;
import java.util.Set;

/**
 * 
 * @author giovanni
 */
public class GameMap implements Serializable {
    
    private final Graph<Room, ArcoGrafo> grafo;
    private Room currentRoom;
    
    public GameMap (){
        this.grafo= new DefaultDirectedGraph<>(ArcoGrafo.class);
    } 
    
    
    public void aggiungiStanza(Room stanza){
        this.grafo.addVertex(stanza);
    }

    public void aggiungiStanza(Room stanza, boolean current){
        this.grafo.addVertex(stanza);
        this.currentRoom=stanza;
    }
    
    public void collegaStanze(Room stanzaP, Room stanzaA, Direzione d){
        this.grafo.addEdge(stanzaP, stanzaA, new ArcoGrafo(d));        
    }
    
    public Room getStanzaArrivo(Direzione d) throws GameMapException{
        for (ArcoGrafo arco : grafo.outgoingEdgesOf(currentRoom)) {
            if (arco.getEtichetta() == d) {
                return grafo.getEdgeTarget(arco);
            }
        }
        throw new GameMapException("Non puoi andare in quella direzione!");
    }

    public Room prendiAscensore(int pianoArrivo) throws GameMapException{
        if(!Set.of(4,6,8,10,14,17,20,25).contains(getCurrentRoom().getId())){
            throw new GameMapException("Non puoi prendere l'ascensore qui");
        }
        return getPiano(pianoArrivo);
    }
    private Room getPiano(int numeroP) throws GameMapException{
        final String piano;
        switch(numeroP){
            case 1:
                piano="Primo";
                break;
            case 2:
                piano= "Secondo";
                break;
            case 3:
                piano= "Terzo";
                break;
            case 4: 
                piano= "Quarto";
                break;
            case 5:
                piano= "Quinto";
                break;
            case 6:
                piano= "Sesto";
                break;
            case 7: 
                piano= "Settimo";
            default: 
                throw new GameMapException("Piano specificato inesistente.");
                    }
        return this.grafo.vertexSet()
                         .stream()
                         .filter(r -> r.getName().equals(new StringBuilder(piano+" piano").toString()))
                         .findFirst()
                         .orElse(null);
 
    }


    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void debug(){
        // Stampa i nodi
        System.out.println("Vertici: " + this.grafo.vertexSet());

        // Stampa gli archi
        System.out.println("Archi:");
        for (ArcoGrafo edge : this.grafo.edgeSet()) {
            System.out.println(this.grafo.getEdgeSource(edge) + " -"+edge.getEtichetta().toString()+"- " + this.grafo.getEdgeTarget(edge));
        }
    }
}
