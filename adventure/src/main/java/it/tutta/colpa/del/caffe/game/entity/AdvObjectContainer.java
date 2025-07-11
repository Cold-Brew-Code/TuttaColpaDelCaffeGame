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
 * @author pierpaolo
 */
public class AdvObjectContainer extends AdvObject implements Serializable {

    private Map<AdvObject, Integer> map = new HashMap<>();

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
     */
    public void add(AdvObject o, int quantity) {
        map.put(o, quantity);
    }

    /**
     * @param o
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

}
