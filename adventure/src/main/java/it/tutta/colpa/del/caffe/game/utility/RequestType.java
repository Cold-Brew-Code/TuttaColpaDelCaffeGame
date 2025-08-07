/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.utility;

/**
 *
 * @author giova
 */
/**
     * Enumera i tipi di richieste che possono essere inviate dal client al server.
     * L'uso di un enum garantisce type-safety e centralizza le azioni possibili.
     */
     public enum RequestType {
        GAME_MAP,
        NPCs,
        ITEMS,
        CLOSE_CONNECTION,
        COMMANDS,
        UPDATED_LOOK,
        ITEM
    }