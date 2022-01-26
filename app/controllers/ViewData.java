package controllers;

import models.Donation;
import play.mvc.Before;
import java.util.List;
import java.util.Objects;
/**El controlador ViewData es el encargado de la visualizacion de los datos guardados*/
public class ViewData extends Application{

    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }

    public static void index(){
        List<Donation> donations = Objects.requireNonNull(connected()).donations;
        render(donations);
    }

}
