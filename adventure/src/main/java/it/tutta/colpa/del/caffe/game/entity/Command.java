/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import it.tutta.colpa.del.caffe.game.utility.CommandType;

/**
 *
 * @author pierpaolo
 */
public class Command implements Serializable {

    private final CommandType type;

    private final String name;

    private Set<String> alias;

    /**
     *
     * @param name
     */
    public Command(String name) {
        this.type = Command.TypeStringToEnum(name);
        this.name = name;
    }

    /**
     *
     * @param type
     * @param name
     * @param alias
     */
    public Command(CommandType type, String name, Set<String> alias) {
        this.type = type;
        this.name = name;
        this.alias = alias;
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
     * @return
     */
    public Set<String> getAlias() {
        return alias;
    }

    /**
     *
     * @param alias
     */
    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    /**
     *
     * @param alias
     */
    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

    /**
     *
     * @return
     */
    public CommandType getType() {
        return type;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.type);
        return hash;
    }

    private static CommandType TypeStringToEnum(String command){

        String cmd = command.toLowerCase().trim();
        if(cmd.equals("nord")){
            return CommandType.NORD;
        }if(cmd.equals("sud")){
            return CommandType.SOUTH;
        }if(cmd.equals("est")){
            return CommandType.EAST;
        }if(cmd.equals("ovest")){
            return CommandType.WEST;
        }if(cmd.equals("sali")){
            return CommandType.UP;
        }if(cmd.equals("scendi")){
            return CommandType.DOWN;
        }if(cmd.equals("osserva")){
            return CommandType.LOOK_AT;
        }if(cmd.equals("raccogli")){
            return CommandType.PICK_UP;
        }if(cmd.equals("apri")){
            return CommandType.OPEN;
        }if(cmd.equals("premi")){
            return CommandType.PUSH;
        }if(cmd.equals("combina")){
            return CommandType.MERGE;
        }if(cmd.equals("leggi")){
            return CommandType.READ;
        }if(cmd.equals("parla")){
            return CommandType.TALK_TO;
        }if(cmd.equals("lascia")){
            return CommandType.LEAVE;
        }if(cmd.equals("usa")){
            return CommandType.USE;
        }if(cmd.equals("ascensore")){
            return CommandType.LIFT;
        }
        return null;
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
        final Command other = (Command) obj;
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

}
