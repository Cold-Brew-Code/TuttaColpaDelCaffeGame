
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rappresenta una stanza all'interno del gioco, contenente oggetti e NPC.
 * <p>
 * Ogni stanza ha un identificatore univoco, un nome, una descrizione testuale,
 * una descrizione dettagliata per il comando "look", visibilità, restrizioni di
 * accesso, un percorso per un'immagine e collezioni di oggetti e NPC presenti
 * nella stanza.
 * </p>
 *
 * @author giova
 */
public class Room implements Serializable {

    private final int id;
    private String name;
    private String description;
    private String look;
    private boolean visible = true;
    private boolean denied_entry = false;
    private String imagePath;
    private Map<GeneralItem, Integer> objects = new HashMap<>();
    private List<NPC> NPCs = new ArrayList<>();

    /**
     *
     * @param id
     */
    public Room(int id) {
        this.id = id;
    }

    /**
     *
     * @param id
     * @param name
     * @param description
     */
    public Room(int id, String name, String description, String image_name) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = "/images/" + image_name;
    }

    /**
     * Costruisce una stanza completa con tutti i dettagli.
     *
     * @param id identificatore univoco della stanza
     * @param name nome della stanza
     * @param description descrizione generica della stanza
     * @param look descrizione dettagliata per il comando "look"
     * @param visible indica se la stanza è visibile
     * @param denied_entry indica se l'accesso è negato
     * @param imageName nome del file immagine associato
     * @param objects mappa di oggetti presenti e le loro quantità
     * @param NPCs lista degli NPC presenti nella stanza
     */
    public Room(int id, String name, String description, String look, boolean visible, boolean denied_entry,
            String imageName, Map<GeneralItem, Integer> objects, List<NPC> NPCs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.look = look;
        this.visible = visible;
        this.denied_entry = denied_entry;
        this.imagePath = "/images/" + imageName;
        this.objects = objects;
        this.NPCs = NPCs;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     *
     * @param visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     *
     * @return
     */
    public Map<GeneralItem, Integer> getObjects() {
        return objects;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.id;
        return hash;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public String getLook() {
        return look;
    }

    /**
     *
     * @param look
     */
    public void setLook(String look) {
        this.look = look;
    }

    /**
     *
     * @param id
     * @return
     */
    public GeneralItem getObject(int id) {
        for (GeneralItem o : objects.keySet()) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    /**
     * @return true se l'accesso alla stanza è negato
     */
    public boolean isDeniedEntry() {
        return denied_entry;
    }

    /**
     * @param denied_entry imposta se l'accesso alla stanza è negato
     */
    public void setDeniedEntry(boolean denied_entry) {
        this.denied_entry = denied_entry;
    }

    /**
     * @return la lista degli NPC presenti nella stanza
     */
    public List<NPC> getNPCs() {
        return NPCs;
    }

    /**
     * @param NPCs lista degli NPC da impostare nella stanza
     */
    public void setNPCs(List<NPC> NPCs) {
        this.NPCs = NPCs;
    }

    /**
     * @param objects mappa di oggetti da impostare nella stanza
     */
    public void setObjects(Map<GeneralItem, Integer> objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "Room{"
                + "name='" + name + '\''
                + ", id=" + id
                + '}';
    }

    /**
     * Restituisce true se la stanza contiene un oggetto con l'ID specificato.
     *
     * @param id l'ID dell'oggetto da cercare
     * @return true se l'oggetto è presente, false altrimenti
     */
    public boolean hasObject(int id) {
        for (GeneralItem item : objects.keySet()) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return il percorso dell'immagine associata alla stanza
     */
    public String getImagePath() {
        return imagePath;
    }
}
