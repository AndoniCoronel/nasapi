package controllers;

import models.User;
import play.mvc.Before;

import java.io.*;
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

    public static void editProfilePic( String newProfilePic) throws IOException {
        //http://localhost:9000/Application/editProfilePic?uname=a&newPorfilePic=dexter1
        User u = connected();
        if (u != null) {
            u.profilePic=newProfilePic;
            u.save();
            changeProfilePic(u.profilePic);
            index();
        }
        else {
            flash.error("User does not exists");
            index();
        }

    }
    private static void changeProfilePic(String Dexter) throws IOException {
        InputStream is = new FileInputStream("nasapi/public/images/"+Dexter+".jpg");
        OutputStream os = new FileOutputStream("nasapi/public/images/dexter.jpg");
        byte[] b = new byte[2048];
        int length;
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
    }
    public static void changeToUser(String uname) throws IOException {
        User u = User.find("byName", uname).first();
        if(u!=null) {
            renderArgs.put("user", uname);
            session.put("user",uname);
            changeProfilePic(u.profilePic);
            MainMenu.index();
        }else{
            flash.error("not a real user");
            index();
        }
    }




}
