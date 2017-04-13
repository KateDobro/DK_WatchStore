package org.itstep.pps2701.entities.enums;

/**
 * Created by DK-HOME on 07.04.2017.
 */
public enum User_role {
    ADMIN, USER;

    public String getStringName(){
        switch(this){
            case USER:
                return "Пользователь";
            case ADMIN:
                return "Администратор";
            default:
                return "Непонятно |:";
        }
    };
}
