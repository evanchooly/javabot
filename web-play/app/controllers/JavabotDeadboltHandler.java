package controllers;

import controllers.deadbolt.AbstractDeadboltHandler;
import models.Admin;
import models.JavabotRoleHolder;
import models.deadbolt.RoleHolder;

public class JavabotDeadboltHandler extends AbstractDeadboltHandler {
    public void beforeRoleCheck() {
    }

    @Override
    public RoleHolder getRoleHolder() {
        TwitterContext twitterContext = AdminController.getTwitterContext();
        return twitterContext != null
            && !Admin.find("userName", twitterContext.screenName).<Admin>fetch().isEmpty()
                ? new JavabotRoleHolder()
                : super.getRoleHolder();
    }
}
