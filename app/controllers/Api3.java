package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import models.Donation;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
/**Se trata del controlador encargado de gestionar la API3*/
public class Api3 extends Application {
    /**
     * La funcion index se encarga de extraer la imagen de la API y guardarla en la carpeta de imagenes con el idnetificador earthApi.png
     * por ultimo manda el fichero HTML index.html asociado a la API3 al cliente web
     */
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
            if (data.size()>0) {
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
            }
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
        Donation donation = new Donation(quantity,3,connected());
        donation.save();
        index();

    }
}
