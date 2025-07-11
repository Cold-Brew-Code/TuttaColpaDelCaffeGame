/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.adventure.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pierpaolo
 */
public class AdvObjectContainer extends AdvObject {

    private Map<AdvObject, Integer> objects = new HashMap<>();

    // Costruttori identici ai tuoi:
    public AdvObjectContainer(int id) {
        super(id);
    }

    public AdvObjectContainer(int id, String name) {
        super(id, name);
    }

    public AdvObjectContainer(int id, String name, String description) {
        super(id, name, description);
    }

    public AdvObjectContainer(int id, String name, String description, Set<String> alias) {
        super(id, name, description, alias);
    }

    /**
     * @return la mappa degli oggetti e quantità
     */
    public Map<AdvObject, Integer> getObjects() {
        return objects;
    }

    /**
     * @param objects imposta la mappa degli oggetti
     */
    public void setObjects(Map<AdvObject, Integer> objects) {
        this.objects = objects;
    }

    /**
     * Aggiunge una quantità di un oggetto al contenitore
     * @param o oggetto
     * @param quantity quantità da aggiungere
     */
    public void add(AdvObject o, int quantity) {
        objects.merge(o, quantity, Integer::sum); // se già presente somma
    }

    /**
     * Rimuove una quantità di un oggetto dal contenitore
     * @param o oggetto
     * @param quantity quantità da rimuovere
     */
    public void remove(AdvObject o, int quantity) {
        if (!objects.containsKey(o)) return;

        int current = objects.get(o);
        if (quantity >= current) {
            objects.remove(o);
        } else {
            objects.put(o, current - quantity);
        }
    }

    /**
     * Rimuove completamente l'oggetto
     * @param o
     */
    public void remove(AdvObject o) {
        objects.remove(o);
    }
}

