package controllers;

import play.mvc.Before;

/**El controlador Main menu es el encargado de gestionar el menu una vez iniciada sesion */
public class MainMenu extends Application {

    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }
    /**
     * Manda el fichero index.html asociado al controlador MainMenu al cliente web
     */
    public static void index(){
        render(connected());
    }

    public static void onClickSettings(){
        Settings.index();
    }
    public static void onClickViewGallery(){
        ViewGallery.index();
    }
    public static void onclickViewData(){
        ViewData.index();
    }
    /**
     * Cierra la sesion y nos retorna a la pagina de inicio (index.html del controlador Application) para el login o registro
     */
    public static void logout() {
        session.clear();
        Application.index();
    }

    //Add apis
}
