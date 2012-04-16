package models;

import models.deadbolt.Role;
import models.deadbolt.RoleHolder;

import java.util.Arrays;
import java.util.List;

public class JavabotRoleHolder implements RoleHolder {
    public static final String BOT_ADMIN = "botAdmin";

    public List<? extends Role> getRoles() {
        return Arrays.asList(new JavabotRole(BOT_ADMIN));
    }

    private static class JavabotRole implements Role {
        String name;

        public JavabotRole(String name) {
            this.name = name;
        }

        public String getRoleName() {
            return name;
        }
    }
}