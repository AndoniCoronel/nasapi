package models;

import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends Model {

    //properties
    @Required
    @MaxSize(15)
    @MinSize(4)
    @Match(value="^\\w*$", message="Not a valid username")
    public String name;

    @Required
    @MaxSize(15)
    @MinSize(5)
    public String password;
    public String profilePic;

    @Required
    @MaxSize(3)
    @MinSize(1)
    @Match(value = "^[1-9]{1}[0-9]*$", message = "invalid age, don't be a troll :) ")
    public int age;

    //profile level


    //relations
    @OneToMany (mappedBy= "user", cascade=CascadeType.ALL)
    public List<Donation> donations = new ArrayList<>();
    @ManyToMany (mappedBy = "users", cascade=CascadeType.ALL)
    public List<Picture> pictures = new ArrayList<>();
    @ManyToMany (mappedBy = "users", cascade=CascadeType.ALL)
    public List<Api> apis = new ArrayList<>();



    public User(String name, String password, String profilePic, int age) {
        this.name = name;
        this.password = password;
        this.profilePic = profilePic;
        this.age = age;
    }
}