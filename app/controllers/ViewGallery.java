package controllers;

import play.mvc.Before;

public class ViewGallery extends Application{
    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }

    public static void index(){
        render(connected().pictures);
    }
}
