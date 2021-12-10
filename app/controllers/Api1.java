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

public class Api1 extends Application {
    public static void index() {
        render();
    }

    public static void donation(float quantity) {
        Donation donation = new Donation(quantity, 3, connected());
        donation.save();
        index();

    }
}
