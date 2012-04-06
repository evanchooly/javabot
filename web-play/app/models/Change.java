package models;

import play.db.jpa.Model;

import java.util.Date;

public class Change extends Model {
    String message;
    Date changeDate;
}
