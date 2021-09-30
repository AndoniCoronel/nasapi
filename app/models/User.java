package models;

import java.util.*;
import java.util.prefs.AbstractPreferences;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class User extends Model {

    //properties
    public String name;
    public String password;
    public int profilePic;
    public int age;


    //relations
    @OneToMany (mappedBy= "user")
    public List<Donation> donations = new ArrayList<>();
    @ManyToMany (mappedBy = "users")
    public List<Picture> pictures = new ArrayList<>();
    @ManyToMany (mappedBy = "users")
    public List<Api> apis = new ArrayList<>();



    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}