package controllers;

import play.mvc.Before;
/**El controlador ViewData es el encargado de la visualizacion de los datos guardados*/
public class ViewData extends Application{

    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }

}
