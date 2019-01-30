import org.junit.*;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;
public class TestRoutes {
    @Before
    public void setUp() throws Exception {
        Contact testContact = new Contact("venky","test@gmail.com");
        ContactService testContactService = new ContactService();
        ContactController newRoutes = new ContactController(testContactService);
        awaitInitialization();
    }
    @After
    public void tearDown() throws Exception {
        stop();
    }
    @Test
    public void testModelGET() {

        ContactService testContactService = new ContactService();
        testContactService.getUser("venky");
        Assert.assertFalse(testContactService.retrievedContact == null); //if not nul, contact exists
    }

    @Test
    public void testModelDelete() {
        ContactService testContactService = new ContactService();
        testContactService.getUser("venky");
        testContactService.delete("venky");
        assert testContactService.users.size() == 0; //if size is 0, contact is deleted.
    }

    @Test
    public void testModelPOST() {
        ContactService testContactService = new ContactService();
        testContactService.getUser("venky");
        testContactService.updateUser("venky","venky","test@gmail.com"); //id and name are same here - id is kept iin the design so that it can be changed in the future
        assert testContactService.user != null;
    }
}
