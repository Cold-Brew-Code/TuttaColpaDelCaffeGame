
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.entity;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import it.tutta.colpa.del.caffe.game.exception.InventoryException;

/**
 * classe degli oggetti contenitori
 * @author giovanni
 */
public class ContainerItem extends GeneralItem implements Serializable {

    private Map<GeneralItem, Integer> containedItems ;
    private boolean open = false;

    /**
     *
     * @param id
     * @param name
     * @param description
     * @param alias
     * @param immagine
     * @param containedItems
     * @param open
     */
    public ContainerItem(int id, String name, String description, Set<String> alias, String immagine, Map<GeneralItem, Integer> containedItems, boolean open) {
        super(id, name, description, alias, immagine);
        this.containedItems = containedItems;
        this.open = open;
    }

    /**
     * @return
     */

    public Map<GeneralItem, Integer> getList() {
        return containedItems;
    }

    /**
     * @param containedItems
     */

    public void setMap(Map<GeneralItem, Integer> containedItems) {
        this.containedItems = containedItems;
    }

    /**
     * @param o
     * @param quantity
     */

    public void add(GeneralItem o, int quantity) {
        containedItems.put(o, quantity);
    }

    /**
     * @param o
     * @param quantity
     */

    public void remove(GeneralItem o, int quantity) {
        if (!containedItems.containsKey(o)) {
            throw new InventoryException("Attenzione: l'oggetto non è presente nell'inventario");
        }
        if (quantity > containedItems.get(o)) {
            throw new InventoryException("Attenzione: non hai abbastanza oggetti nell'inventario");
        } else if (quantity == containedItems.get(o)) {
            containedItems.remove(o);
        } else {
            containedItems.replace(o, containedItems.get(o) - quantity);
        }
    }

    /**
     * @param o
     */

    public void remove(GeneralItem o) {
        if (!containedItems.containsKey(o)) {
            throw new InventoryException("Attenzione: l'oggetto non è presente nell'inventario");
        }
        if (1 == containedItems.get(o)) {
            containedItems.remove(o);
        } else {
            containedItems.replace(o, containedItems.get(o) - 1);
        }
    }

    /**
     * Restituisce un dizionario con l'oggetto specificato e la sua quantità.
     * Se l'oggetto non è presente, lancia un'eccezione.
     *
     * @param o oggetto da cercare
     * @return mappa contenente solo l'oggetto cercato e la sua quantità
     */
    public Map<GeneralItem, Integer> getObject(GeneralItem o) {
        if (!containedItems.containsKey(o)) {
            throw new IllegalArgumentException("L'oggetto '" + o.getName() + "' non è presente nel contenitore.");
        }

        int quantity = containedItems.get(o);
        Map<GeneralItem, Integer> result = new HashMap<>();
        result.put(o, quantity);
        return result;
    }

    /**
     *
     * @return
     */

    public boolean isOpen() {
        return open;
    }

    /**
     *
     * @param open
     */

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean containsObject(GeneralItem item) {
        return containedItems.containsKey(item);
    }

    public GeneralItem containsObjectById(int id) {
    for (GeneralItem gi : containedItems.keySet()) {
        if (gi.getId() == id) {
            return gi;
        }
    }
    return null;
}



}