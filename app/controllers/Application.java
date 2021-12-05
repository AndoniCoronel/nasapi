package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import play.data.binding.As;
import play.data.validation.Valid;
import play.db.jpa.JPA;
import play.mvc.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    public static void signup() {
        render();
    }

    public static void api1() throws IOException {
        URL url = new URL("https://epic.gsfc.nasa.gov/api/natural");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        JsonArray data = new JsonParser().parse(content.toString()).getAsJsonArray();
        String imageID = data.get(0).getAsJsonObject().get("image").getAsString();
        System.out.println(imageID);
        render();
    }

    public static void api2() {
        render();
    }

    public static void api3() throws MalformedURLException, IOException {
        URL url = new URL("https://epic.gsfc.nasa.gov/api/natural");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        JsonArray data = new JsonParser().parse(content.toString()).getAsJsonArray();
        String imageID = data.get(0).getAsJsonObject().get("image").getAsString();
        // 2021-12-04 00:41:06
        String dateString = data.get(0).getAsJsonObject().get("date").getAsString();
        String date = (dateString.split(" "))[0];
        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String day = date.split("-")[2];
        // https://epic.gsfc.nasa.gov/archive/natural/2021/12/04/png/epic_1b_20211204004555.png
        String urlImage = "https://epic.gsfc.nasa.gov/archive/natural/" + year + "/" + month + "/" + day + "/png/";
        urlImage += imageID;
        urlImage += ".png";
        System.out.println(urlImage);
        URL url2 = new URL(urlImage);
        InputStream is = url2.openStream();
        OutputStream os = new FileOutputStream("nasapi/public/images/earthApi.png");
        byte[] b = new byte[2048];
        int length;
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();

        render();
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {

    }

    public static void imageOfTheDay() {
        // save the image of the day for the current user
    }

    public static void dataApi1() {
        // save the data of api 1 for user1
    }

    public static void registered(@Valid User user, String verifyPassword) {
        // validation.required(user.name);
        validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("Your password doesn't match");
        if (validation.hasErrors()) {
            render("@signup", user, verifyPassword);
        }
        user.create();
        session.put("user", user.name);
        flash.success("Welcome, " + user.name);
        index();
    }

    public static void start(String uname, String psw, boolean unregister) {
        User u = User.find("byName", uname).first();
        if (u != null) {
            if (u.password.equals(psw)) {
                session.put("user", u.name);
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
}