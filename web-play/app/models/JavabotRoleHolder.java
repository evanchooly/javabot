package models;

import java.util.Arrays;
import java.util.List;

import models.deadbolt.Role;
import models.deadbolt.RoleHolder;

public class JavabotRoleHolder implements RoleHolder {
    public static final String BOT_ADMIN = "botAdmin";
    public static final String BOT_OWNER = "botOwner";

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