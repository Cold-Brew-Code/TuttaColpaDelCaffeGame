/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.adventure.entity;

import it.tutta.colpa.del.caffe.adventure.exception.InventoryException;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private Map<AdvObject, Integer> inventory = new HashMap<>();

    public Map<AdvObject, Integer> getInventory() {
        return inventory;
    }

    public void setList(Map<AdvObject, Integer> inventory) {
        this.inventory = inventory;
    }

    public void add(AdvObject o, int quantity) throws InventoryException {
        if (this.inventory.size() >= 4) {
            throw new InventoryException("Attenzione: l'inventario è pieno");
        }
        inventory.put(o, quantity);
    }

    public void remove(AdvObject o, int quantity) throws InventoryException {
        if (!inventory.containsKey(o)) {
            throw new InventoryException("Attenzione: l'oggetto non è presente nell'inventario");
        }
        if (quantity > inventory.get(o)) {
            throw new InventoryException("Attenzione: non hai abbastanza oggetti nell'inventario");
        } else if (quantity == inventory.get(o)) {
            inventory.remove(o);
        }
        inventory.replace(o, inventory.get(o) - quantity);
    }

    public void remove(AdvObject o) throws InventoryException {
        if (!inventory.containsKey(o)) {
            throw new InventoryException("Attenzione: l'oggetto non è presente nell'inventario");
        }
        if (1 == inventory.get(o)) {
            inventory.remove(o);
        }
        inventory.replace(o, inventory.get(o) - 1);
    }

    public int getQuantity(AdvObject element) {
        if (this.inventory.containsKey(element)) {
            return this.inventory.get(element);
        }
        return 0;
    }

    public int getQuantity(int objID) {
        AdvObject element = new AdvObject(objID);
        if (this.inventory.containsKey(element)) {
            return this.inventory.get(element);
        }
        return 0;
    }
}