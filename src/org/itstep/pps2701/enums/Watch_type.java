package org.itstep.pps2701.enums;

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
