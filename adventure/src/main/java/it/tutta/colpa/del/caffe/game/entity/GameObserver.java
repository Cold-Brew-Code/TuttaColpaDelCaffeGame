/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 *
 * @author pierpaolo
 */
public interface GameObserver {

    /**
     *
     * @param description
     * @param parserOutput
     * @return
     */

    public String update(GameDescription description, ParserOutput parserOutput);

}
