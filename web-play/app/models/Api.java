package models;

import play.db.jpa.Model;

import com.google.code.morphia.annotations.Entity;
import javax.persistence.Table;

@Entity
("apis")
public class Api extends Model {
    public String name;
    public String baseUrl;
    public String packages;
    public String zipLocations;
}
