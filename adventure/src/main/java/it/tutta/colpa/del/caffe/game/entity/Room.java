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
 *
 * @author pierpaolo
 */
public class Room implements Serializable {

    private final int id;
    private String name;
    private String description;
    private String look;
    private boolean visible = true;
    private boolean denied_entry=false;
    private String imagePath;
    private Map<AdvObject,Integer> objects = new HashMap<>();
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
    public Room(int id, String name, String description,String image_name) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = "/images/" + image_name;
    }


    public Room(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath=null;
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
    public Map<AdvObject, Integer> getObjects() {
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
    public AdvObject getObject(int id) {
        for (AdvObject o : objects.keySet()) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    public boolean isDeniedEntry() {
        return denied_entry;
    }

    public void setDeniedEntry(boolean denied_entry) {
        this.denied_entry = denied_entry;
    }

    public List<NPC> getNPCs() {
        return NPCs;
    }

    public void setNPCs(List<NPC> NPCs) {
        this.NPCs = NPCs;
    }

    public void setObjects(Map<AdvObject, Integer> objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
