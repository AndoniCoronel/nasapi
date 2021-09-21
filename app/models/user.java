package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class user extends Model {

    public String name;
    public String password;

    public user(String name, String password) {
        this.name = name;
        this.password = password;
    }
}