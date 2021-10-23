package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Api extends Model {

    //properties
    public int api;
    public String data;

    //relations
    @OneToMany(cascade= CascadeType.ALL)
    public List<Donation> donations = new ArrayList<>();

    @ManyToMany(cascade= CascadeType.ALL)
    public List<User> users = new ArrayList<>();


    public Api(int api, String data) {
        this.api = api;
        this.data = data;
    }
}