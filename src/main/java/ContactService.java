import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.*;
public class ContactService {
    public Map<String, Contact> users = new HashMap<>();
    public Contact user;
    private Client client;
    private SearchResponse response;
    public Contact retrievedContact = new Contact("","");

    public void establishElasticsearchConnection() throws Exception {
        client = new PreBuiltTransportClient(Settings.builder().put("client.transport.sniff", true).put("cluster.name","elasticsearch").build()).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9200));
    }

    public List<Contact> getAllUsers() {
        response = client.prepareSearch().execute().actionGet();
        List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
        List<Contact> results = new ArrayList<>();
        for(SearchHit hit : searchHits) {
            Map<String, Object> source = hit.getSource();
            String name = (String) source.get("name");
            String email = (String) source.get("email");
            Contact retrievedContact = new Contact(name, email);
            results.add(retrievedContact);
        }
        return results;
    }

    public Contact getUser(String id) {
        response = client.prepareSearch().execute().actionGet();
        List<SearchHit> searchHit = Arrays.asList(response.getHits().getHits());
        for(SearchHit hit : searchHit) {
            Map<String, Object> source = hit.getSource();
            String nameOfContact = (String) source.get("name");
            String emailOfContact = (String) source.get("email");
            if(nameOfContact.equals(id)) {
                retrievedContact = new Contact(nameOfContact, emailOfContact);
            }
        }
        return retrievedContact;
    }

    public Contact createUser(String name, String email) {
        failIfInvalid(name, email);
        Contact user = new Contact(name, email);
        users.put(user.getId(), user);
        return user;
    }

    public Contact findById(String id) {
        return users.get(id);
    }

    public Contact updateUser(String id, String name, String email) {
        user = users.get(id);
        if (user == null) {
            throw new IllegalArgumentException("No user with id '" + id + "' found");
        }
        failIfInvalid(name, email);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    public void delete(String id) {
        GetResponse response = client.prepareGet("contact","name",id).get();
        String name = (String) response.getField("name").getValue();
        // Process other fields
        DeleteResponse delResponse = client.prepareDelete("contact", "name", id).get();
        users.remove(id);
    }

    private void failIfInvalid(String name, String email) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'email' cannot be empty");
        }
    }
}
