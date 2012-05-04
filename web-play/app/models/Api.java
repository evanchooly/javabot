package models;

import controllers.AdminController;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "apis")
public class Api extends Model {
    public String name;
    public String baseUrl;
    public String packages;
    public String zipLocations;

    @Override
    public Api save() {
        Api save = super.save();

        ApiEvent event = new ApiEvent(name, EventType.ADD, AdminController.getTwitterContext().screenName);
        event.save();
        return save;
    }
}
