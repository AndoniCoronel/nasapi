package controllers;

import models.User;
import play.mvc.Before;

import java.util.Objects;

public class Settings extends Application{

    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }

    public static void index(){
        User u = connected();
        assert u != null;
        switch (u.profileLevel){
            case 0:
                render("@settingsDefault",u);
            case 1:
                render("@settingsAdmin",u);
            case 2:
                render("@settingsCash",u);
        }
    }

    public static void deleteUser(){
            Objects.requireNonNull(connected()).delete();
            MainMenu.logout();
            Application.index();
    }

    public static void editPassword( String psw, String newPsw){
        User u = connected();
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
    public static void changeToUser(String uname){
        User u = User.find("byName", uname).first();
        if(u!=null) {
            renderArgs.put("user", uname);
            session.put("user",uname);
            MainMenu.index();
        }else{
            flash.error("not a real user");
            index();
        }
    }




}
