/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.utility;

import org.jgrapht.graph.DefaultEdge;

/**
 * La classe {@code StringArcoGrafo} rappresenta un arco di un grafo con
 * un'etichetta di tipo {@link String}.
 * <p>
 * Estende {@link org.jgrapht.graph.DefaultEdge} per essere utilizzata
 * all'interno di strutture dati del framework JGraphT.
 * </p>
 *
 * <p>
 * L'etichetta consente di associare un'informazione testuale all'arco, utile
 * per rappresentare collegamenti con descrizioni o nomi.
 * </p>
 *
 * <p>
 * Esempio d'uso:</p>
 * <pre>{@code
 * Graph<String, StringArcoGrafo> grafo = new DefaultDirectedGraph<>(StringArcoGrafo.class);
 * grafo.addVertex("A");
 * grafo.addVertex("B");
 * grafo.addEdge("A", "B", new StringArcoGrafo("collegamento AB"));
 * }</pre>
 *
 * @author giova
 */
public class StringArcoGrafo extends DefaultEdge {

    private String etichetta;

    /**
     * Costruisce un arco con etichetta testuale.
     *
     * @param etichetta la stringa associata all'arco
     */
    public StringArcoGrafo(String etichetta) {
        this.etichetta = etichetta;
    }

    /**
     * Restituisce l'etichetta dell'arco.
     *
     * @return la stringa che etichetta l'arco
     */
    public String getEtichetta() {
        return etichetta;
    }

    /**
     * Restituisce la rappresentazione testuale dell'arco.
     *
     * @return l'etichetta dell'arco come stringa
     */
    @Override
    public String toString() {
        return etichetta;
    }
}
