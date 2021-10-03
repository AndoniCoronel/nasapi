package models;

import play.db.jpa.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends Model {

    //properties
    public String name;
    public String password;
    public String profilePic;
    public int age;


    //relations
    @OneToMany (mappedBy= "user")
    public List<Donation> donations = new ArrayList<>();
    @ManyToMany (mappedBy = "users")
    public List<Picture> pictures = new ArrayList<>();
    @ManyToMany (mappedBy = "users")
    public List<Api> apis = new ArrayList<>();



    public User(String name, String password, String profilePic, int age) {
        this.name = name;
        this.password = password;
        this.profilePic = profilePic;
        this.age = age;
    }
}