package controllers;

import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

import java.util.*;

import models.*;

import javax.persistence.*;

public class Application extends Controller {

    public static void index() {render();}


    public static void login() {
        render();
    }

    public static void signup(){
        render();
    }

    public static void registered(String uname, String psw, int age, String dexter){
        User u = User.find("byName",uname).first();
        if (u != null) {
            flash.error("User already exists");
            signup();
        }
        else {
            new User(uname,psw, dexter,age).save();
            index();
        }
    }
    public static void start(String uname, String psw){
        User u = User.find("byName",uname).first();
        if (u != null) {
            if (u.password.equals(psw))
            {
                render();
            }
            else{
                flash.error("Incorrect password");
                login();
            }
        }
        else {
            flash.error("User does not exists");
            login();
        }
    }

}