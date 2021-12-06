package controllers;

import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api2 extends Application {
    public static void index() {
        // https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=2021-12-05&camera=fhaz&api_key=DEMO_KEY
        try {
            LocalDate today = LocalDate.now();
            String yesterdayDate = (today.minusDays(1)).format(DateTimeFormatter.ISO_DATE);

            String urlTemp = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date="
                    + yesterdayDate + "&camera=fhaz&api_key=DEMO_KEY";

            URL url = new URL(urlTemp);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            JsonObject data = new JsonParser().parse(content.toString()).getAsJsonObject().get("photos")
                    .getAsJsonArray()
                    .get(0).getAsJsonObject();

            String urlImage = data.get("img_src").getAsString();
            URL url2 = new URL(urlImage);
            InputStream is = url2.openStream();
            OutputStream os = new FileOutputStream("nasapi/public/images/marsApi.png");
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
