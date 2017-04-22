package org.itstep.pps2701.enums;

public enum WATCH_TYPE {
    MECHANIC, QUARTZ;

    public static WATCH_TYPE getWatch_type(String stringName) {
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
