
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import it.tutta.colpa.del.caffe.game.exception.ItemException;
import java.io.Serializable;
import java.util.Set;
import javax.swing.ImageIcon;

/**
 *
 * @author giova
 */
public class Item  extends GeneralItem implements Serializable {
    
    private int uses ; 

    public Item(int id, String name, String description, Set<String> alias, int uses, String immagine) {
        super(id, name, description, alias, immagine);
        this.uses = uses;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }
    
    /**
    * Decrementa il numero di usi dell'oggetto di una unità.
    * Se il numero di usi diventerebbe negativo, lancia un'eccezione.
    *
    * @throws ItemException se il numero di usi è già zero o negativo
    */
    public void decreaseUses() throws ItemException {
        if (this.uses <= 0) {
            throw new ItemException ("Non puoi più utilizzare quest' oggetto.");
        }
        setUses(this.uses - 1);
    }
}

    

    

