package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Api extends Model {

    //properties
    public String api;
    public String data;

    //relations
    @OneToMany
    public List<Donation> donations = new ArrayList<>();

    @ManyToMany
    public List<User> users = new ArrayList<>();


    public Api(String api, String data) {
        this.api = api;
        this.data = data;
    }
}