package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.util.Pair;
import models.Picture;
import netscape.javascript.JSObject;
import play.data.binding.As;
import play.mvc.Before;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewGallery extends Application{
    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }

    public static void index(){
        List<Picture> pictures = connected().pictures;
        List<Pair<String, String>> picURL = new ArrayList<>();
        JsonParser parser = new JsonParser();
        boolean neptune = false;
        for (Picture pic:pictures) {
            System.out.println();
            try {
                String date =pic.date.toString().substring(0,11);
                URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=DaFi4M1aSffvFg0EGzfCxWruc6FyhR7wStWMPtxf&date="+date);
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
                JsonObject js = parser.parse(content.toString()).getAsJsonObject();
                if(!js.get("media_type").getAsString().equals("video")){
                    picURL.add(new Pair<>( date, js.get("url").getAsString()));
                }
                else if(!neptune){
                    neptune=true;
                    picURL.add(new Pair<>(date, "https://www.nasa.gov/sites/default/files/thumbnails/image/pia01492-main.jpg"));
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }


        render(picURL);
    }

    public static void delete( @As("yyyy-MM-dd") Date date){
        Picture picture = Picture.find("byDate",date).first();
        picture.users.remove(connected());
        picture.save();
        index();

    }
}
