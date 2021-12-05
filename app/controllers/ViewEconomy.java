package controllers;

import play.mvc.Before;

public class ViewEconomy extends Application{

    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }
}
