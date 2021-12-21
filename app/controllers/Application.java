package controllers;

import com.sun.org.apache.xpath.internal.operations.Bool;
import play.data.binding.As;
import play.data.validation.Valid;
import play.mvc.*;

import java.io.*;
import java.text.ParseException;
import java.util.*;

import models.*;

public class Application extends Controller {

    @Before
    static void addUser() {
        User user = connected();
        if (user != null) {
            renderArgs.put("user", user);
        }
    }

    static User connected() {
        if (renderArgs.get("user") != null) {
            return renderArgs.get("user", User.class);
        }
        String username = session.get("user");
        if (username != null) {
            return User.find("byName", username).first();
        }
        return null;
    }

    public static void index() {
        if (connected() != null) {
            MainMenu.index();
        }
        render();
    }

    public static void login() {
        render();
    }

    public static void signup() {
        render();
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {

    }

    public static void imageOfTheDay() {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        Picture picture = Picture.find("byDate",removeTime(date)).first();
        Boolean find = false;

        if(picture==null){
            Picture p = new Picture(removeTime(date));
            p.users.add( connected());
            p.save();
            ViewGallery.index();
        }
        for(User u :picture.users)
        {
            if(u==connected())
                find = true;
        }
        if(find==false){
            picture.users.add(connected());
            picture.save();
            ViewGallery.index();
        }
        else{
            MainMenu.index();
        }
        // save the image of the day for the current user
    }

    public static void dataApi1() {
        // save the data of api 1 for user1
    }

    public static void registered(@Valid User user, String verifyPassword) throws IOException {
        // validation.required(user.name);
        validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("Your password doesn't match");
        if (validation.hasErrors()) {
            render("@signup", user, verifyPassword);
        }
        user.create();
        session.put("user", user.name);
        flash.success("Welcome, " + user.name);
        changeProfilePic(user.profilePic);
        index();
    }

    public static void start(String uname, String psw) {
        User u = User.find("byName", uname).first();
        if (u != null) {
            if (u.password.equals(psw)) {
                session.put("user", u.name);
                try {
                    changeProfilePic(u.profilePic);
                }catch (Exception e){
                    System.out.println(e);
                }
                MainMenu.index();
            } else {
                flash.error("Incorrect password");
                login();
            }
        } else {
            flash.error("User does not exists");
            login();
        }
    }

    public static void addPicture(String uname, @As("dd/MM/yyyy") Date pictureDate) throws ParseException {
        // http://localhost:9000/Application/addPicture?uname=a&pictureDate=12/12/2000
        Picture pic = Picture.find("byDate", pictureDate).first();
        User user = User.find("byName", uname).first();
        if (user != null) {
            if (pic == null) {
                pic = new Picture(pictureDate);
            }
            if (!user.pictures.contains(pic)) {
                pic.users.add(user);
                pic.save();
                user.pictures.add(pic);
                user.save();
                renderText("added user");
            }
        } else {
            renderText("User does not exists");
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
    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}