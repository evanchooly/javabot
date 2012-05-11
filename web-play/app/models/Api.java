package models;

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
}
