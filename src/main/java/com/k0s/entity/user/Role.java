package com.k0s.entity.user;

public enum Role {

    ADMIN("admin"),
    USER ("user"),
    GUEST("guest");

    private final String role;

    Role( String role) {
        this.role = role;
    }

    public static Role getRole(String role) {
//        Role user = GUEST;
        if(role == null || "".equals(role)){
            return GUEST;
        }
        for (Role roles : Role.values()){
            if (roles.role.equals(role)){
//                user = roles;
                return roles;
            }
        }

//        return user;
        return GUEST;
    }

    @Override
    public String toString() {
        return role;
    }
}
