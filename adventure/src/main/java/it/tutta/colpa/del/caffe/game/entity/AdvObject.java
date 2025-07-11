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
 * @author pierpaolo
 */
public class AdvObject implements Serializable {

    private final int id;
    private String name;
    private String description;
    private Set<String> alias;
    private boolean openable = false;
    private boolean pickupable = true;
    private boolean pushable = false;
    private boolean open = false;
    private boolean push = false;
    private boolean leggibile = false;
    private boolean cliccabile = false;
    private boolean visibile = false;
    private boolean componibile = false;
    private int utilizzi;
    private boolean contenitore = false;
    private ImageIcon immagine;

    /**
     * @param id
     */
    public AdvObject(int id) {
        this.id = id;
    }

    /**
     * @param id
     * @param name
     */
    public AdvObject(int id, String name) {
        this.id = id;
        this.name = name;
        }

    /**
     * @param id
     * @param name
     * @param description
     */
    public AdvObject(int id, String name, String description, String image_name) {
        this.id = id;
        this.name = name;
        this.description = description;
        ImageIcon img=new ImageIcon((new ImageIcon(getClass().getResource("/images/"+image_name)))
                .getImage()
                .getScaledInstance(951, javax.swing.GroupLayout.DEFAULT_SIZE, Image.SCALE_SMOOTH));
    }

    /**
     * @param id
     * @param name
     * @param description
     * @param alias
     */
    public AdvObject(int id, String name, String description, Set<String> alias) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.alias = alias;
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
    public boolean isOpenable() {
        return openable;
    }

    /**
     * @param openable
     */
    public void setOpenable(boolean openable) {
        this.openable = openable;
    }

    /**
     * @return
     */
    public boolean isPickupable() {
        return pickupable;
    }

    /**
     * @param pickupable
     */
    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }

    /**
     * @return
     */
    public boolean isPushable() {
        return pushable;
    }

    /**
     * @param pushable
     */
    public void setPushable(boolean pushable) {
        this.pushable = pushable;
    }

    /**
     * @return
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * @param open
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * @return
     */
    public boolean isPush() {
        return push;
    }

    /**
     * @param push
     */
    public void setPush(boolean push) {
        this.push = push;
    }


    public void setLeggibile(boolean leggibile) {
        this.leggibile = leggibile;
    }

    public boolean isLeggibile() {
        return leggibile;
    }

    public void setCliccabile(boolean cliccabile) {
        this.cliccabile = cliccabile;
    }

    public boolean isCliccabile() {
        return cliccabile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }

    public boolean isVisibile() {
        return visibile;
    }

    public void setComponibile(boolean componibile) {
        this.componibile = componibile;
    }

    public boolean isComponibile() {
        return componibile;
    }

    /**
     * @param utilizzi
     */
    public void setUtilizzi(int utilizzi) {
        this.utilizzi = utilizzi;
    }

    /**
     * @return
     */
    public int getUtilizzi() {
        return utilizzi;
    }

    public void setContenitore(boolean contenitore) {
        this.contenitore = contenitore;
    }

    public boolean isContenitore() {
        return contenitore;
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
        final AdvObject other = (AdvObject) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
