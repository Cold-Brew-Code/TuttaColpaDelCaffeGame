/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.tutta.colpa.del.caffe.game.exception.InventoryException;

/**
 * Rappresenta l'inventario del giocatore nel gioco. L'inventario contiene
 * oggetti di tipo {@link GeneralItem} con le rispettive quantità. Implementa
 * {@link Iterable} per poter iterare sugli oggetti contenuti.
 *
 * Gestisce aggiunta, rimozione e controllo delle quantità degli oggetti. Limita
 * il numero di oggetti differenti a 4.
 *
 * @author pierpaolo
 */
public class Inventory implements Serializable, Iterable<GeneralItem> {

    private Map<GeneralItem, Integer> inventory = new HashMap<>();

    /**
     * Restituisce la mappa interna che rappresenta l'inventario.
     *
     * @return una mappa che associa ogni oggetto alla sua quantità
     */
    public Map<GeneralItem, Integer> getInventory() {
        return inventory;
    }

    /**
     * Imposta la mappa interna dell'inventario.
     *
     * @param inventory la nuova mappa da impostare
     */
    public void setList(Map<GeneralItem, Integer> inventory) {
        this.inventory = inventory;
    }

    /**
     * Aggiunge un oggetto all'inventario con la quantità specificata. Se
     * l'oggetto è già presente, incrementa la quantità.
     *
     * @param o l'oggetto da aggiungere
     * @param quantity la quantità da aggiungere (deve essere positiva)
     * @throws InventoryException se l'inventario è pieno o la quantità non è
     * valida
     */
    public void add(GeneralItem o, int quantity) throws InventoryException {
        if (this.inventory.size() >= 4 && !inventory.containsKey(o)) {//  l'inventario  pieno e stai cercando di aggiungere un nuovo oggetto
            throw new InventoryException("Attenzione: l'inventario è pieno");
        }
        if (quantity <= 0) {
            throw new InventoryException("La quantità deve essere positiva.");
        }
        // se ho già l'oggetto all'interno incremento solo la quantità 
        inventory.merge(o, quantity, (a, b) -> Integer.sum(a, b));
    }

    /**
     * Rimuove una quantità specifica di un oggetto dall'inventario.
     *
     * @param o l'oggetto da rimuovere
     * @param quantity la quantità da rimuovere
     * @throws InventoryException se l'oggetto non è presente o non ci sono
     * abbastanza copie
     */
    public void remove(GeneralItem o, int quantity) throws InventoryException {
        if (!inventory.containsKey(o)) {
            throw new InventoryException("Attenzione: l'oggetto non è presente nell'inventario");
        }
        int currentQuantity = inventory.get(o);
        if (quantity > currentQuantity) {
            throw new InventoryException("Attenzione: non hai abbastanza oggetti nell'inventario");
        } else if (quantity == currentQuantity) {
            inventory.remove(o);
        } else {
            inventory.replace(o, currentQuantity - quantity); // Altrimenti, aggiorna la quantità
        }
    }

    /**
     * Rimuove una singola copia di un oggetto dall'inventario.
     *
     * @param o l'oggetto da rimuovere
     * @throws InventoryException se l'oggetto non è presente
     */
    public void remove(GeneralItem o) throws InventoryException {
        if (!inventory.containsKey(o)) {
            throw new InventoryException("Attenzione: l'oggetto non è presente nell'inventario");
        }
        int currentQuantity = inventory.get(o);
        if (currentQuantity == 1) {
            inventory.remove(o);
        } else {
            inventory.replace(o, currentQuantity - 1);
        }
    }

    /**
     * Restituisce la quantità disponibile di un oggetto nell'inventario.
     *
     * @param element l'oggetto di cui controllare la quantità
     * @return la quantità disponibile (0 se l'oggetto non è presente)
     */
    public int getQuantity(GeneralItem element) {
        if (this.inventory.containsKey(element)) {
            return this.inventory.get(element);
        }
        return 0;
    }

    /**
     * Verifica se un oggetto è presente nell'inventario.
     *
     * @param item l'oggetto di tipo GeneralItem da cercare nell'inventario
     * @return true se l'oggetto è presente nell'inventario, false altrimenti
     */
    public boolean contains(GeneralItem item) {
        return inventory.containsKey(item);
    }

    /**
     * public int getQuantity(int objID){ GeneralItem element = new
     * GeneralItem(objID); if(this.inventory.containsKey(element)){ return
     * this.inventory.get(element); } return 0; }
     */
    /**
     * Restituisce un iteratore sugli oggetti presenti nell'inventario.
     *
     * @return un {@link Iterator} sugli oggetti
     */
    @Override
    public Iterator<GeneralItem> iterator() {
        // Restituisce l'iteratore delle chiavi della mappa.
        // Il set di chiavi (keySet) è già una collezione iterabile.
        return inventory.keySet().iterator();
    }
}
