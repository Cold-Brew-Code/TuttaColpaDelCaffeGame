/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.io.Serializable;
import java.util.Set;
import javax.swing.ImageIcon;

/**
 *
 * @author giova
 */
public class ItemRead extends Item implements Serializable {
    
    private String content;

    /**
     * 
     * @param content
     * @param uses
     * @param id
     * @param name
     * @param description
     * @param alias
     * @param utilizzi
     * @param immagine 
     */
    public ItemRead(String content, int uses, int id, String name, String description, Set<String> alias, int utilizzi, ImageIcon immagine) {
        super(uses, id, name, description, alias, utilizzi, immagine);
        this.content = content;
    }


    /**
     * 
     * @return 
     */
    
    public String getContent() {
        return content;
    }
    
    /**
     * 
     * @param content 
     */
    
    public void setContent(String content) {
        this.content = content;
    }
    
    
    
}
