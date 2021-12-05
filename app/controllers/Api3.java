package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api3 extends Application{
    public static void index() {
        try {
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
        } catch (Exception e) {
            System.out.println(e);
        }
        render();
    }
}
