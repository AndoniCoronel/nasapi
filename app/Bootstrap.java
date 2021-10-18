import play.jobs.*;
import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        // Load default data if the database is empty
        if(User.count() == 0) {
            User a = new User("Dexter","1234", "dexter1",6);
            a.save();
            User b = new User("a","a", "dexter2",1);
            b.save();
            new Donation(100,2,a).save();
            new Donation(150,3,a).save();
            new Donation(15,1,b).save();
            new Api(1,"blabla").save();
        }
    }

}