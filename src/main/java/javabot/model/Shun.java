package javabot.model;

import java.io.Serializable;
import java.util.Date;

import javabot.dao.ShunDao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table (name = "shun", uniqueConstraints = @UniqueConstraint (columnNames = { "nick" }))
@NamedQueries ( {
    @NamedQuery (name = ShunDao.BY_NAME, query = "select s from Shun s where s.nick = :nick"),
    @NamedQuery (name = ShunDao.CLEANUP, query = "delete from Shun s where s.expiry <= :now") })
public class Shun implements Serializable, Persistent {
  private Long   id;
  private String nick;
  private Date   expiry;

  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public String getNick () {
    return nick;
  }

  public void setNick (String nick) {
    this.nick = nick;
  }

  @Temporal (TemporalType.TIMESTAMP)
  public Date getExpiry () {
    return expiry;
  }

  public void setExpiry (Date updated) {
    this.expiry = updated;
  }
}
