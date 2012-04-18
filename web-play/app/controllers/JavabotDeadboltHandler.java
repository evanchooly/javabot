package controllers;

import controllers.deadbolt.AbstractDeadboltHandler;
import models.JavabotRoleHolder;
import models.deadbolt.RoleHolder;

public class JavabotDeadboltHandler extends AbstractDeadboltHandler {
    public void beforeRoleCheck() {
        AdminController.oauth();
    }

    @Override
    public RoleHolder getRoleHolder() {
        AdminController.TwitterContext twitterContext = AdminController.getTwitterContext();
        return twitterContext != null
            && !models.Admin.find("userName", twitterContext.screenName).<models.Admin>fetch().isEmpty()
                ? new JavabotRoleHolder()
                : null; //super.getRoleHolder();
    }
}
