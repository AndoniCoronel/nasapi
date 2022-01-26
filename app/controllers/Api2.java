package controllers;

import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.*;
import models.Donation;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
/**Se trata del controlador encargado de gestinar la API2*/
public class Api2 extends Application {
    /**
     * La foncion index se encarga de extraer la imagen de la API y guardarla en la carpeta de imagenes con el idnetificador marsApi.png
     * por ultimo manda el fichero HTML index.html asociado a la API2 al cliente web
     */
    public static void index() {
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
    /**
     * Es la funcion encargada de gestionar las donaciones. Tiene como entrada el siguiente parametro:
     * @param quantity La cantidad que se desea donar
     * Se crea una donacion a cargo del usuario conectado y se guarda en la base de datos
     */
    public static void donation(float quantity){
        Donation donation = new Donation(quantity,2,connected());
        donation.save();
        index();

    }
}
