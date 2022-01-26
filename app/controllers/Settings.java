package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.util.Pair;
import models.Donation;
import models.Picture;
import models.User;
import play.mvc.Before;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**El controlador Settings es el controlador que gestina los ajustes del usuario */
public class Settings extends Application{

    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }

    /**
     * Se encarga de visualizar el fichero html de ajustes asociado a cada tipo de usuario (administrador, contable o usuario estandar)
     * esto de realiza mediante switch
     */
    public static void index() {
        User u = connected();
        assert u != null;
        switch (u.profileLevel) {
            case 0:
                render("@settingsDefault", u);
            case 1:
                render("@settingsAdmin", u);
            case 2:
                List<Donation> donations = Donation.findAll();
                //correct the cash table and make it decent
                render("@settingsCash", donations);
        }
    }

    /**
     * Elimina el usuario de la base de datos y nos envia al menu inical index.html del controlador application
     */
    public static void deleteUser(){
            Objects.requireNonNull(connected()).delete();
            MainMenu.logout();
            Application.index();
    }

    /**
     * Es la funcio que permite al usuario cambiar la contraseña. Tiene los siguientes parametos de entrada:
     * @param psw Contraseña antigua
     * @param newPsw Nueva contraseña
     * Se verifica que la contraseña antigua es correcta para poder combiar a la nueva. Se envia confirmacio o error mediante renderText
     */
    public static void editPassword( String psw, String newPsw){
        User u = connected();
        if (u != null) {
            if (u.password.equals(psw))
            {
                u.password=newPsw;
                u.save();
                index();

            }
            else{
                flash.error("not correct password");
                index();

            }
        }
        else {
            flash.error("User does not exists");
            index();


        }
    }

    /**
     * Es la funcio que permite al usuario cambiar la foto de perfil. Tiene los siguientes parametos de entrada:
     * @param newProfilePic El identificador de la nueva foto de perfil
     * Si el usuario existe se cambia la foto y se vuelve ha enviar el fichero index.html del controlador Settings. Si no existe se
     * envia el mismo fichero pero con una visualizacion de error
     */
    public static void editProfilePic( String newProfilePic) throws IOException {
        //http://localhost:9000/Application/editProfilePic?uname=a&newPorfilePic=dexter1
        User u = connected();
        if (u != null) {
            u.profilePic=newProfilePic;
            u.save();
            changeProfilePic(u.profilePic);
            index();
        }
        else {
            flash.error("User does not exists");
            index();
        }

    }

    /**
     * Es la funcion que permite cambiar de foto de perfil. Tiene como entrada los siguientes parametros:
     * @param Dexter Es el nombre de nueva imagen de perfil
     */
    private static void changeProfilePic(String Dexter) throws IOException {
        InputStream is = new FileInputStream("nasapi/public/images/"+Dexter+".jpg");
        OutputStream os = new FileOutputStream("nasapi/public/images/dexter.jpg");
        byte[] b = new byte[2048];
        int length;
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
    }

    /**
     * Permite al administrador conectarse como cuenta de un usuario corriente que escoja. De entrada tiene el siguiente parametro:
     * @param uname Nombre del usuario al que se quiera conectar
     * La funcion comprueba si el usuario existe, si no visualiza un mensaje de error
     */
    public static void changeToUser(String uname) throws IOException {
        User u = User.find("byName", uname).first();
        if(u!=null) {
            renderArgs.put("user", uname);
            session.put("user",uname);
            changeProfilePic(u.profilePic);
            flash.success("changed to the user");
            MainMenu.index();
        }else{
            flash.error("not a real user");
            index();
        }
    }




}
