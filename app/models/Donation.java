package models;

import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Una donación es una clase que asigna a un usuario y una api una cantidad de dinero
 */
@Entity
public class Donation extends Model {

    //properties
    public float quantity;
    public int apiId;

    //relations
    @ManyToOne(cascade= CascadeType.ALL)
    public User user;

    public Donation(float quantity,int apiId, User user) {
        this.quantity = quantity;
        this.apiId = apiId;
        this.user = user;
    }
}