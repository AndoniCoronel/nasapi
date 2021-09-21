package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class api extends Model {

    public String api;
    public String data;

    public User(String api, String password, String data) {
        this.api = api;
        this.data = data;
    }
}