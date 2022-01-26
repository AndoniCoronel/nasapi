import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import play.jobs.*;
import models.*;

import com.google.gson.JsonElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;
import java.io.*;
import java.util.Date;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() throws IOException {
        // Load default data if the database is empty
        if (User.count() == 0) {
            User a = new User("Dexter", "1234", "dexter1", 6);
            a.save();
            User b = new User("a", "a", "dexter2", 1, 1);
            b.save();
            User c = new User("b", "b", "dexter2", 1, 2);
            c.save();
            new Donation(100, 2, a).save();
            new Donation(150, 3, a).save();
            new Donation(15, 1, b).save();
            new Donation(152, 2, b).save();
            new Donation(151, 3, b).save();
            long millis = System.currentTimeMillis();
            Date date = new Date(millis - 86400000);
            Picture one = new Picture(removeTime(date));
            millis = System.currentTimeMillis();
            date = new Date(millis - 86400000 * 2);
            Picture two = new Picture(removeTime(date));
            one.users.add(b);
            two.users.add(b);
            one.save();
            two.save();
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

        if (Objects.equals(media_type, "video")) {
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