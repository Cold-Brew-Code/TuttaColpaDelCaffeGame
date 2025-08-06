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

public class Inventory  implements Serializable, Iterable<GeneralItem> {

    private Map<GeneralItem, Integer> inventory = new HashMap<>();



    public Map<GeneralItem, Integer> getInventory() {
        return inventory;
    }

    public void setList(Map<GeneralItem, Integer> inventory) {
        this.inventory = inventory;
    }

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

    public int getQuantity(GeneralItem element){
        if(this.inventory.containsKey(element)){
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
    public int getQuantity(int objID){
        GeneralItem element = new GeneralItem(objID);
        if(this.inventory.containsKey(element)){
            return this.inventory.get(element);
        }
        return 0;
    }*/

    @Override
    public Iterator<GeneralItem> iterator() {
        // Restituisce l'iteratore delle chiavi della mappa.
        // Il set di chiavi (keySet) è già una collezione iterabile.
        return inventory.keySet().iterator();
    }
}