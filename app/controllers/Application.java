package controllers;

import com.google.gson.JsonObject;
import play.*;
import play.data.binding.As;
import play.data.validation.Valid;
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

    public static void registered(@Valid User user, String verifyPassword){
        //validation.required(user.name);
        validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("Your password doesn't match");
        if(validation.hasErrors()) {
            render("@signup", user, verifyPassword);
        }
        user.create();
        session.put("user", user.name);
        flash.success("Welcome, " + user.name);
        index();
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
        List<User> users = new ArrayList<>();
        for (User user:users ) {

        }
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

    public static void addPicture(String uname,@As("dd/MM/yyyy") Date pictureDate) throws ParseException {
        //http://localhost:9000/Application/addPicture?uname=a&pictureDate=12/12/2000
        Picture pic = Picture.find("byDate",pictureDate).first();
        User user = User.find("byName",uname).first();
        if (user != null) {
            if(pic==null){
                pic = new Picture(pictureDate);
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

    //Android functions
    public void androidLogin(String uname, String psw){
        User u = User.find("byName",uname).first();
        JsonObject jsonObject = new JsonObject();
        if (u != null) {
            if (u.password.equals(psw))
            {
                jsonObject.addProperty("login",true);
                renderJSON(jsonObject);
            }
            else{
                jsonObject.addProperty("login",false);
                renderJSON(jsonObject);
            }
        }
        else {
            jsonObject.addProperty("login",false);
            renderJSON(jsonObject);
        }
    }

    //Android functions
    public void androidRegister(String name, String password,String verifyPassword, int age, String dexter ){
        JsonObject jsonObject = new JsonObject();
        User u = User.find("byName",name).first();
        //Check if user already exists
        if(u==null){
            //Check that the passwords match
            if(password.equals(verifyPassword)){
                u = new User(name, password, dexter, age);
                u.save();
                jsonObject.addProperty("register",true);
                renderJSON(jsonObject);
            } else{
                jsonObject.addProperty("register",false);
                jsonObject.addProperty("error", "Passwords don't match");
                renderJSON(jsonObject);
            }
        } else {
                jsonObject.addProperty("register",false);
                jsonObject.addProperty("error", "User already exists");
                renderJSON(jsonObject);
        }
    }



    //Test functions
    public void getSomething(){
        renderJSON("{\"phonetype\":\"N95\"}");
    }




}