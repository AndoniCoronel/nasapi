package controllers;

import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import models.*;

import javax.persistence.*;

public class Application extends Controller {

    public static void index() {
        render();
        }

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
    public static void start(String uname, String psw, boolean unregister){
        User u = User.find("byName",uname).first();
        if (u != null) {
            if (u.password.equals(psw))
            {
                if(unregister){
                    u.delete();
                    index();
                }else {
                    render();
                }
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

    public static void editPassword(String uname, String psw, String newPsw){
        //http://localhost:9000/Application/editPassword?uname=a&psw=a&newPsw=aaa
        User u = User.find("byName",uname).first();
        if (u != null) {
            if (u.password.equals(psw))
            {
                u.password=newPsw;
                u.save();
                renderText("password changed");

            }
            else{
                renderText("Incorrect password");
            }
        }
        else {
            renderText("User does not exists");

        }
    }

    public static void editProfilePic(String uname, String newProfilePic){
        //http://localhost:9000/Application/editProfilePic?uname=a&newPorfilePic=dexter1
        User u = User.find("byName",uname).first();
        if (u != null) {
            u.profilePic=newProfilePic;
            u.save();
            renderText("profile pic changed");
        }
        else {
            renderText("User does not exists");
        }

    }

    public static void addPicture(String uname,String pictureDateString) throws ParseException {
        //http://localhost:9000/Application/addPicture?uname=a&pictureDateString=12/12/2000
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(pictureDateString);
        Picture pic = Picture.find("byDate",date).first();
        User user = User.find("byName",uname).first();
        if (user != null) {
            if(pic==null){
                pic = new Picture(date);
            }
            if(!user.pictures.contains(pic)) {
                pic.users.add(user);
                pic.save();
                user.pictures.add(pic);
                user.save();
                renderText("added user");
            }
        }
        else {
            renderText("User does not exists");
        }

    }


}