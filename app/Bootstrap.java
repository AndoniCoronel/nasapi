import play.jobs.*;
import models.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.*;
import java.net.*;
import java.io.*;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() throws MalformedURLException, IOException {
        // Load default data if the database is empty
        if (User.count() == 0) {
            User a = new User("Dexter", "1234", "dexter1", 6);
            a.save();
            User b = new User("a", "a", "dexter2", 1);
            b.save();
            new Donation(100, 2, a).save();
            new Donation(150, 3, a).save();
            new Donation(15, 1, b).save();
            new Api(1, "blabla").save();
        }

        URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=DaFi4M1aSffvFg0EGzfCxWruc6FyhR7wStWMPtxf&");
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

        JsonObject data = new JsonParser().parse(content.toString()).getAsJsonObject();
        String urlImage = data.get("url").getAsString();
        String media_type = data.get("media_type").getAsString();

        if (media_type == "video") {
            urlImage = "https://www.nasa.gov/sites/default/files/thumbnails/image/pia01492-main.jpg";
        }

        URL url2 = new URL(urlImage);
        InputStream is = url2.openStream();
        OutputStream os = new FileOutputStream("nasapi/public/images/backgroundImage.png");
        byte[] b = new byte[2048];
        int length;
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
    }

}