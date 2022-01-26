package controllers;

import com.sun.org.apache.xpath.internal.operations.Bool;
import play.data.binding.As;
import play.data.validation.Valid;
import play.mvc.*;

import java.io.*;
import java.text.ParseException;
import java.util.*;

import models.*;

/**El controlador application es el controlador por defecto de Play y en el que mayor numero de funciones implementamos */
public class Application extends Controller {

    @Before
    static void addUser() {
        User user = connected();
        if (user != null) {
            renderArgs.put("user", user);
        }
    }

    /**
     * Se encarga de retornar el usuario conectado en el login
     */
    static User connected() {
        if (renderArgs.get("user") != null) {
            return renderArgs.get("user", User.class);
        }
        String username = session.get("user");
        if (username != null) {
            return User.find("byName", username).first();
        }
        return null;
    }

    /**
     * Manda el fichero index.html asociado al controlador Application al cliente web. Es el fichero inicial de la pagina web.
     */
    public static void index() {
        if (connected() != null) {
            MainMenu.index();
        }
        render();
    }

    /**
     * Manda el fichero login.html asociado al controlador Application al cliente web. Es el fichero asociado a la opcion login
     */
    public static void login() {
        render();
    }
    /**
     * Manda el fichero signup.html asociado al controlador Application al cliente web. Es el fichero asociado a la opcion signup
     */
    public static void signup() {
        render();
    }

    /**
     * Guarda una imagen en un directorio determiando. Los paramentos de entrada son los siguientes:
     * @param imageUrl La URL donde se encuentra la imagen
     * @param destinationFile Directorio donde se desea guardar la imagen
     */
    public static void saveImage(String imageUrl, String destinationFile) throws IOException {

    }

    /**
     * La funcion se encarga de guardar una relacion de la imagen del dia con el usuario conectado
     */
    public static void imageOfTheDay() {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        Picture picture = Picture.find("byDate",removeTime(date)).first();
        Boolean find = false;

        if(picture==null){
            Picture p = new Picture(removeTime(date));
            p.users.add( connected());
            p.save();
            ViewGallery.index();
        }
        for(User u :picture.users)
        {
            if(u==connected())
                find = true;
        }
        if(find==false){
            picture.users.add(connected());
            picture.save();
            ViewGallery.index();
        }
        else{
            MainMenu.index();
        }
        // save the image of the day for the current user
    }

    public static void dataApi1() {
        // save the data of api 1 for user1
    }

    /**
     * Es la funcion encargada de realizar el registrod e usuario en la pagina web. Recibe las siguientes entradas;
     * @param user Los datos del registro en formato usuario
     * @param verifyPassword La contraseña de confirmacion para verificar el registro
     * La funcion verifica que no existe ningun usuario con ese nombre en la base de datos y que la contraseña coincide con la contraseña
     * de verificacion. Si es asi manda el fichero index.html asociado al controlador Application al cliente web. Si no envia se error que
     * se visualizara en el fichero signup.html
     */
    public static void registered(@Valid User user, String verifyPassword) throws IOException {
        // validation.required(user.name);
        validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("Your password doesn't match");
        if (validation.hasErrors()) {
            render("@signup", user, verifyPassword);
        }
        user.create();
        session.put("user", user.name);
        flash.success("Welcome, " + user.name);
        changeProfilePic(user.profilePic);
        index();
    }

    /**
     * Es la funcion encargada de realizar el login de usuario en la pagina web. Recibe las siguientes entradas;
     * @param uname Nombre de usuario
     * @param psw Contraseña del usuario
     * La fubncion verifica si ecxiste un usuario con esa contraseña en la base de datos, si existe permite el login mandando el fochero index.html
     * asociado al controlador MainMenu. Si no envia error que se visualizara en el fichero login.html
     */
    public static void start(String uname, String psw) {
        User u = User.find("byName", uname).first();
        if (u != null) {
            if (u.password.equals(psw)) {
                session.put("user", u.name);
                try {
                    changeProfilePic(u.profilePic);
                }catch (Exception e){
                    System.out.println(e);
                }
                MainMenu.index();
            } else {
                flash.error("Incorrect password");
                login();
            }
        } else {
            flash.error("User does not exists");
            login();
        }
    }

    /**
     * La funcions encarga de guardar relaciones entre imagenes y usuarios
     * @param uname Nomre del usuario
     * @param pictureDate Fecha de la imagen
     * La funcion verifica que el usuario existe y que la imagen no ha sido guardada con anterioridad. Si es asi envia un mensaje
     * de confirmacion via renderText, si no envia un mensaje de error utilizando tambien la via renderText
     */
    public static void addPicture(String uname, @As("dd/MM/yyyy") Date pictureDate) throws ParseException {
        Picture pic = Picture.find("byDate", pictureDate).first();
        User user = User.find("byName", uname).first();
        if (user != null) {
            if (pic == null) {
                pic = new Picture(pictureDate);
            }
            if (!user.pictures.contains(pic)) {
                pic.users.add(user);
                pic.save();
                user.pictures.add(pic);
                user.save();
                renderText("added user");
            }
        } else {
            renderText("User does not exists");
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
     * Esta funcion tiene la finalidad de cambiar el formato de la fecha. La entrada es el siguiente parametro. Retorna la fecha
     * en el nuevo formato
     * @param date Fecha en fomrmato inicial
     */
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