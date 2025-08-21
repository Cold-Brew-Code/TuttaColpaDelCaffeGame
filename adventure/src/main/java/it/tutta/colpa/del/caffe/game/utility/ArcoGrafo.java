/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.utility;

import org.jgrapht.graph.DefaultEdge;

/**
 * La classe {@code ArcoGrafo} rappresenta un arco etichettato di un grafo.
 * <p>
 * Estende {@link org.jgrapht.graph.DefaultEdge} e aggiunge un attributo
 * {@link Direzione} come etichetta dell’arco, utile per distinguere i
 * collegamenti in base alla direzione o al tipo di connessione.
 * </p>
 *
 * <p>
 * Esempio d'uso:</p>
 * <pre>{@code
 * ArcoGrafo arco = new ArcoGrafo(Direzione.NORD);
 * System.out.println(arco.getEtichetta()); // stampa "NORD"
 * }</pre>
 *
 * @author giova
 */
public class ArcoGrafo extends DefaultEdge {

    /**
     * Etichetta associata all'arco, rappresentata come una {@link Direzione}.
     */
    private Direzione etichetta;

    /**
     * Costruisce un arco etichettato con la direzione specificata.
     *
     * @param etichetta la direzione da associare a questo arco
     */
    public ArcoGrafo(Direzione etichetta) {
        this.etichetta = etichetta;
    }

    /**
     * Restituisce l’etichetta (direzione) associata all’arco.
     *
     * @return l'etichetta di tipo {@link Direzione}
     */
    public Direzione getEtichetta() {
        return etichetta;
    }

    /**
     * Restituisce una rappresentazione testuale dell’arco, corrispondente alla
     * sua etichetta.
     *
     * @return una stringa con il nome della direzione
     */
    @Override
    public String toString() {
        return etichetta.toString();
    }
}
