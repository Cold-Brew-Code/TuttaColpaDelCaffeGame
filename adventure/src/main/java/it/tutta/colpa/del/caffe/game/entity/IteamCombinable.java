/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;

/**
 *
 * @author giova
 */
public class IteamCombinable  extends Item  implements Serializable{
    
    private List<Item> composedOf ; 
    
    /**
     * 
     * @param composedOf
     * @param uses
     * @param id
     * @param name
     * @param description
     * @param alias
     * @param utilizzi
     * @param immagine 
     */

    public IteamCombinable(List<Item> composedOf, int uses, int id, String name, String description, Set<String> alias, int utilizzi, ImageIcon immagine) {
        super(uses, id, name, description, alias, utilizzi, immagine);
        this.composedOf = composedOf;
    }

    /**
     *
     * @return 
     */
    public List<Item> getComposedOf() {
        return composedOf;
    }
    
    /**
     * 
     * @param composedOf 
     */

    public void setComposedOf(List<Item> composedOf) {
        this.composedOf = composedOf;
    }

    

   
    
    
   
    
}
