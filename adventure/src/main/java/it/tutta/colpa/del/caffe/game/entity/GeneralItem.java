
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.entity;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author giovanni
 */
public  abstract class GeneralItem implements Serializable {

    private final int id;
    private String name;
    private String description;
    private Set<String> alias;
    private boolean visibile = false;
    private String immagine;
    private boolean pickupable= false; 

    public GeneralItem(int id, String name, String description, Set<String> alias, String immagine) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.alias = alias;
        this.immagine = "/images/"+immagine;
    }
    
    public GeneralItem(int id){
    this.id = id;
    
    }
    

    
    
    public boolean isPickupable() {
        return pickupable;
    }
    

    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }
    

    public boolean isVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }

    public String getImmagine() {
        return immagine;
    }

    
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setNome(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescrizione(String description) {
        this.description = description;
    }


    /**
     * @return
     */
    public Set<String> getAlias() {
        return alias;
    }

    /**
     * @param alias
     */
    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    /**
     * @param alias
     */
    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

    /**
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
        return hash;
    }

    /**
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
        final GeneralItem other = (GeneralItem) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
