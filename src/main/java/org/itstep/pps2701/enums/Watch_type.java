package org.itstep.pps2701.enums;

public enum Watch_type {
    MECHANIC, QUARTZ;

    public static Watch_type getWatch_type(String stringName) {
        switch (stringName) {
            case "MECHANIC":
                return MECHANIC;
            case "QUARTZ":
                return QUARTZ;
            default:
                return null;
        }
    }

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
