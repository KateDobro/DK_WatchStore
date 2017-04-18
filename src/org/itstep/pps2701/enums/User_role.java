package org.itstep.pps2701.enums;


public enum User_role {
    USER, ADMIN;

    public static User_role getUser_role(String stringName) {

        switch (stringName) {
            case "USER":
                return USER;
            case "ADMIN":
                return ADMIN;
            default:
                return null;
        }
    }

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
