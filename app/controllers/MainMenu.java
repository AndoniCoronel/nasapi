package controllers;

import play.mvc.Before;

public class MainMenu extends Application {

    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }

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
    public static void logout() {
        session.clear();
        Application.index();
    }

    //Add apis
}
