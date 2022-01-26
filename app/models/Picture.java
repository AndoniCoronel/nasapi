package models;

import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Una foto contiene una lista de usuarios que tienen guardada la foto y una fecha asignada a esa imagen
 */
@Entity
public class Picture extends Model {

    public Date date;

    @ManyToMany(cascade= CascadeType.ALL)
    public List<User> users = new ArrayList<>();

    public Picture(Date date) {
        this.date = date;
    }
}