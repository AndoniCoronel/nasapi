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
/**Se trata del controlador encargado de gestionar la API1*/
public class Api1 extends Application {
    /**
     * La funcion index se encarga de enviar el archivo index.html asociado a la API1 al cliente web. La informacion de la API se obtiene
     * en el codigo HTML del fichero.
     */
    public static void index() {
        render();
    }

    /**
     * Es la funcion encargada de gestionar las donaciones. Tiene como entrada el siguiente parametro:
     * @param quantity La cantidad que se desea donar
     * Se crea una donacion a cargo del usuario conectado y se guarda en la base de datos
     */
    public static void donation(float quantity) {
        Donation donation = new Donation(quantity, 1, connected());
        donation.save();
        index();

    }
}
