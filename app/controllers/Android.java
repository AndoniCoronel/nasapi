package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.User;

/**Se trata del controlador encargado de gestionar la comunicacion con el cliente android*/
public class Android extends Application{
    /**Es la funcion encargada de realizar el login, se le entran los siguientes parametros:
     * @param uname Es el nombre del usuario
     * @param psw Es la contraseña asociada al usuario
     * La funcion verifica si existe un usuario con esa contraseña en la base de datos, si existe permite el login, si no retorna
     * una respuesta negativa. La informacion se envia en formato JSON,
     */
    public void androidLogin(String uname, String psw){
        User u = User.find("byName",uname).first();
        JsonObject jsonObject = new JsonObject();
        if (u != null) {
            if (u.password.equals(psw))
            {
                jsonObject.addProperty("login",true);
                renderJSON(jsonObject);
            }
            else{
                jsonObject.addProperty("login",false);
                jsonObject.addProperty("error", "Incorrect password.");

                renderJSON(jsonObject);
            }
        }
        else {
            jsonObject.addProperty("login",false);
            jsonObject.addProperty("error", "Not a user, please register.");

            renderJSON(jsonObject);
        }
    }

    /**
     * Es la funcion encargada de realizar el registro, se le entran los siguientes parametros:
     * @param name Nombre de usuario que se quiere registrar
     * @param password Contraseña del usuario
     * @param verifyPassword Confirmacion de contraseña como modo de seguridad
     * @param age Edad del usuario
     * @param dexter Foto de perfil del usuario
     * La funcion verifica que no existe ningun usuario con ese nombre en la base de datos y que la contraseña coincide con la contraseña
     * de verificacion. Si es asi se guarda el usuario en la base de datos y se envia un mensaje de confirmacion, si no se envia un mensaje
     * de error. Los mensajes igual que en el login se envian en formato JSON.
     */
    //Android functions
    public void androidRegister(String name, String password,String verifyPassword, int age, String dexter ){
        JsonObject jsonObject = new JsonObject();
        User u = User.find("byName",name).first();
        //Check if user already exists
        if(u==null){
            //Check that the passwords match
            if(password.equals(verifyPassword)){
                u = new User(name, password, dexter, age);
                u.save();
                jsonObject.addProperty("register",true);
                renderJSON(jsonObject);
            } else{
                jsonObject.addProperty("register",false);
                jsonObject.addProperty("error", "Passwords don't match");
                renderJSON(jsonObject);
            }
        } else {
            jsonObject.addProperty("register",false);
            jsonObject.addProperty("error", "User already exists");
            renderJSON(jsonObject);
        }
    }
}
