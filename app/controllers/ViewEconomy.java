package controllers;

import play.mvc.Before;

/**El controlador ViewEconomy es el encargado de gestionar la vision de las donaciones realizadas por el usuario conectado */
public class ViewEconomy extends Application{

    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }
}
