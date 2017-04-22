package org.itstep.pps2701.enums;


public enum USER_ROLE {
    ROLE_USER, ROLE_ADMIN;

    public static USER_ROLE getUser_role(String stringName) {

        switch (stringName) {
            case "ROLE_USER":
                return ROLE_USER;
            case "ROLE_ADMIN":
                return ROLE_ADMIN;
            default:
                return null;
        }
    }

    public String getStringName(){
        switch(this){
            case ROLE_USER:
                return "Пользователь";
            case ROLE_ADMIN:
                return "Администратор";
            default:
                return "Непонятно |:";
        }
    };
}
