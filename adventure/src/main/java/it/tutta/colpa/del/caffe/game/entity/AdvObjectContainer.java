/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.entity;


import it.tutta.colpa.del.caffe.game.exception.InventoryException;

import java.io.Serializable;
import java.util.*;

/**
 * @author giovanni
 * classe degli oggetti contenitori 
 */
public class AdvObjectContainer extends AdvObject implements Open, Serializable {

    private Map<AdvObject, Integer> map = new HashMap<>();
    private boolean open = false;

    /**
     * @param id
     */
    public AdvObjectContainer(int id) {
        super(id);
    }

    /**
     * @param id
     * @param name
     */
    public AdvObjectContainer(int id, String name) {
        super(id, name);
    }

    /**
     * @param id
     * @param name
     * @param description
     */
    public AdvObjectContainer(int id, String name, String description, String imagePath) {
        super(id, name, description, imagePath);
    }

    /**
     * @param id
     * @param name
     * @param description
     * @param alias
     */
    public AdvObjectContainer(int id, String name, String description, Set<String> alias) {
        super(id, name, description, alias);
    }

    /**
     * @return
     */
    public Map<AdvObject, Integer> getList() {
        return map;
    }

    /**
     * @param map
     */
    public void setMap(Map<AdvObject, Integer> map) {
        this.map = map;
    }

    /**
     * @param o
     * @param quantity
     */
    public void add(AdvObject o, int quantity) {
        map.put(o, quantity);
    }

    /**
     * @param o
     * @param quantity
     */
    public void remove(AdvObject o, int quantity) {
        if (!map.containsKey(o)) {
            throw new InventoryException("Attenzione: l'oggetto non è presente nell'inventario");
        }
        if (quantity > map.get(o)) {
            throw new InventoryException("Attenzione: non hai abbastanza oggetti nell'inventario");
        } else if (quantity == map.get(o)) {
            map.remove(o);
        } else {
            map.replace(o, map.get(o) - quantity);
        }
    }

    /**
     * @param o
     */
    public void remove(AdvObject o) {
        if (!map.containsKey(o)) {
            throw new InventoryException("Attenzione: l'oggetto non è presente nell'inventario");
        }
        if (1 == map.get(o)) {
            map.remove(o);
        } else {
            map.replace(o, map.get(o) - 1);
        }
    }
    
    /**
    * Restituisce un dizionario con l'oggetto specificato e la sua quantità.
    * Se l'oggetto non è presente, lancia un'eccezione.
    *
    * @param o oggetto da cercare
    * @return mappa contenente solo l'oggetto cercato e la sua quantità
    */
    public Map<AdvObject, Integer> getObject(AdvObject o) {
       if (!map.containsKey(o)) {
           throw new IllegalArgumentException("L'oggetto '" + o.getName() + "' non è presente nel contenitore.");
       }

       int quantity = map.get(o);
       Map<AdvObject, Integer> result = new HashMap<>();
       result.put(o, quantity);
       return result;
    }
    
    
    
    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void setOpen(boolean open) {
        this.open = open;
    }

}
