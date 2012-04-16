package controllers;

import controllers.deadbolt.AbstractDeadboltHandler;
import models.JavabotRoleHolder;
import models.deadbolt.RoleHolder;

import java.util.List;

public class JavabotDeadboltHandler extends AbstractDeadboltHandler {
    public void beforeRoleCheck() {
        Admin.oauth();
    }

    @Override
    public RoleHolder getRoleHolder() {
        Admin.TwitterContext twitterContext = Admin.getTwitterContext();
        List<models.Admin> list = models.Admin.find("userName   ", twitterContext.screenName).fetch();
        if (!list.isEmpty()) {
            return new JavabotRoleHolder();
        } else {
            return super.getRoleHolder();
        }
    }
}
