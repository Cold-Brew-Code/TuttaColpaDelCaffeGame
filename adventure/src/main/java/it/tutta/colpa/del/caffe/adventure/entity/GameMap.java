
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.entity;
import it.tutta.colpa.del.caffe.adventure.exception.GameMapException;
import it.tutta.colpa.del.caffe.adventure.utility.ArcoGrafo;
import it.tutta.colpa.del.caffe.adventure.utility.Direzione;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.Graph;
/**
 * 
 * @author giovanni
 */
public class GameMap {
    
    private final Graph<Room, ArcoGrafo> grafo;
    
    public GameMap (){
        this.grafo= new DefaultDirectedGraph<>(ArcoGrafo.class);
    } 
    
    
    public void aggiungiStanza(Room stanza){
        
        this.grafo.addVertex(stanza);
    }
    
    public void collegaStanze(Room stanzaP, Room stanzaA, Direzione d){
        
        this.grafo.addEdge(stanzaP, stanzaA, new ArcoGrafo(d));        
    }
    
    public Room getStanzaArrivo(Room stanzaP, Direzione d){
        //prendo tutti gli archi che partono da stanzaP 
        for (ArcoGrafo arco : grafo.outgoingEdgesOf(stanzaP)) {
            if (arco.getEtichetta() == d) {
               //se è uguale ho trovato la stanza che cercavo
                return grafo.getEdgeTarget(arco);
            }
        }
        // Nessun arco con quell'etichetta
        return null;
    }
    
    public Room getPiano(int numeroP) throws GameMapException{
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
    
    
    
    
}
