package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.User;

public class Android extends Application{

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
                jsonObject.addProperty("error", "Incorrect password.");

                renderJSON(jsonObject);
            }
        }
        else {
            jsonObject.addProperty("login",false);
            jsonObject.addProperty("error", "Not a user, please register.");

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
}
