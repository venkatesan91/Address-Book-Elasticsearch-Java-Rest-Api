public class ContactApiInitiator {
    private static ContactApiInitiator init = null;
    private ContactApiInitiator() throws Exception {
        new ContactController(new ContactService());
    }
    public static ContactApiInitiator getInstance() throws Exception {
        if(init == null)
            init = new ContactApiInitiator();
        return init;
    }
    public static void main(String[] args) throws Exception {
        ContactApiInitiator.getInstance();
    }
}
