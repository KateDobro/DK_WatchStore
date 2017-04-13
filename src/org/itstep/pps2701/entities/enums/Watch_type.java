package org.itstep.pps2701.entities.enums;

/**
 * Created by DK-HOME on 07.04.2017.
 */
public enum Watch_type {
    MECHANIC, QUARTZ;

    public String getStringName(){
        switch(this){
            case MECHANIC:
                return "Механические";
            case QUARTZ:
                return "Кварцевые";
            default:
                return "Непонятно |:";
        }
    }
}
