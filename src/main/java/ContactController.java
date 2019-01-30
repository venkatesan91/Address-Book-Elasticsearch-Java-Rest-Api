import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.*;

public class ContactController {

    public ContactController(final ContactService contactService) throws Exception {

        //establish connection
        contactService.establishElasticsearchConnection();

        get("/contacts", (req, res) -> contactService.getAllUsers(), JsonUtil.json());

        post("/contact", (req, res) -> contactService.createUser(
                req.queryParams("name"),
                req.queryParams("email")
        ), JsonUtil.json());

        put("/contact/:id", (req, res) -> contactService.updateUser(
                req.params(":id"),
                req.queryParams("name"),
                req.queryParams("email")
        ), JsonUtil.json());

        // DELETE - delete user
        ObjectMapper om = new ObjectMapper();
        delete("/user/:id", (request, response) -> {
            String id = request.params(":id");
            Contact user = contactService.findById(id);
            if (user != null) {
                contactService.delete(id);
                return om.writeValueAsString("user with id " + id + " is deleted!");
            } else {
                response.status(404);
                return om.writeValueAsString("user not found");
            }
        });

        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            //res.body(toJson(new ResponseError(e)));
        });
    }
}
