
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta un oggetto generico all'interno del gioco. Ogni oggetto ha un
 * identificatore unico, un nome, una descrizione, eventuali alias, un'immagine
 * associata e proprietà di visibilità e possibilità di essere raccolto.
 *
 * Gli alias permettono di riferirsi allo stesso oggetto con nomi alternativi.
 * La proprietà {@code pickupable} indica se l'oggetto può essere raccolto dal
 * giocatore. La proprietà {@code visibile} indica se l'oggetto è visibile nella
 * stanza o nell'ambiente.
 *
 * @author giovanni
 */
public class GeneralItem implements Serializable {

    private final int id;
    private String name;
    private String description;
    private Set<String> alias;
    private boolean visibile = false;
    private String immagine;
    private boolean pickupable = false;

    /**
     * Costruisce un oggetto generico completo con tutti i dettagli principali.
     *
     * @param id identificatore unico dell'oggetto
     * @param name nome dell'oggetto
     * @param description descrizione dell'oggetto
     * @param alias insieme di nomi alternativi per riferirsi all'oggetto
     * @param immagine nome del file immagine associato all'oggetto (verrà
     * aggiunto il percorso "/images/")
     */
    public GeneralItem(int id, String name, String description, Set<String> alias, String immagine) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.alias = alias;
        this.immagine = "/images/" + immagine;
    }

    /**
     * Costruisce un oggetto generico con solo l'identificatore. Utile per
     * creare riferimenti placeholder o per confronti tramite id.
     *
     * @param id identificatore unico dell'oggetto
     */
    public GeneralItem(int id) {
        this.id = id;

    }

    /**
     * Restituisce true se l'oggetto può essere raccolto dal giocatore.
     *
     * @return true se l'oggetto è pickupable, false altrimenti
     */
    public boolean isPickupable() {
        return pickupable;
    }

    /**
     * Imposta se l'oggetto può essere raccolto dal giocatore.
     *
     * @param pickupable true per rendere l'oggetto raccolgibile, false
     * altrimenti
     */
    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }

    /**
     * Restituisce true se l'oggetto è visibile nell'ambiente.
     *
     * @return true se l'oggetto è visibile, false altrimenti
     */
    public boolean isVisibile() {
        return visibile;
    }

    /**
     * Imposta la visibilità dell'oggetto nell'ambiente.
     *
     * @param visibile true per rendere l'oggetto visibile, false per
     * nasconderlo
     */
    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }

    /**
     * Restituisce il percorso dell'immagine associata all'oggetto.
     *
     * @return percorso dell'immagine
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * Imposta il percorso dell'immagine associata all'oggetto.
     *
     * @param immagine percorso dell'immagine (può includere il prefisso
     * "/images/")
     */
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
        if (obj instanceof GeneralItem other) {
            return this.id == other.id;
        }
        return false;
    }

}
