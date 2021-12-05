import play.jobs.*;
import models.*;

import java.util.Date;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        // Load default data if the database is empty
        if(User.count() == 0) {
            User a = new User("Dexter","1234", "dexter1",6);
            a.save();
            User b = new User("a","a", "dexter2",1, 1);
            b.save();
            new Donation(100,2,a).save();
            new Donation(150,3,a).save();
            new Donation(15,1,b).save();
            new Api(1,"blabla").save();
            long millis = System.currentTimeMillis();
            Date date = new Date(millis);
            Picture one = new Picture(date);
            millis = System.currentTimeMillis();
            date = new Date(millis);
            Picture two = new Picture(date);
            one.users.add(a);
            two.users.add(a);
        }
    }

}