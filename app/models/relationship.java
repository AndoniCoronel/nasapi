package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class relationship extends Model {

    public String api;
    public int user;

    public relationship(String api, int user) {
        this.api = api;
        this.user = user;
    }
}