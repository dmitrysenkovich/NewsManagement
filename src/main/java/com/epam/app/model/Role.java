package com.epam.app.model;

/**
 * Role model.
 */
public class Role {
    private int userId;
    private String roleName;

    public Role() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
