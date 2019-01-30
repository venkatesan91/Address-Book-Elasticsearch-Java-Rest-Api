# Address-Book-Elasticsearch-Java-Rest-Api

This is a RESTful API built using Java Spark with Elasticsearch as the data store.

Pre-requisites:

Elasticsearch has to be installed locally and indexing has to be done using:

PUT /contact
{
  "mappings" : {
    "contact" : {
      "properties" : {
        "name" : {"type" : "text"},
				"phone": {"type" : "double"},
				"email": {"type" : "text"}
      } 
    }
  }
}

Once indexing is done, objects can be inserted either using Bulk api in elasticsearch or normal inserts. 

<b>Design Structure:</b>

1)<b>Contact</b> – The model class which has the skeleton of User/Contact.

2)<b>ContactController</b> – The controller class which has Java Spark API with all types of requests. 

3)<b>ContactService</b> – The service class which is called by the controller. This class has elasticsearch implementations like PreBuiltTransportClient, SearchHits, GetResponse, DeleteResponse etc. 

4)<b>ContactApiInitiator</b> – A singleton class which calls an instance of controller to invoke Java spark.

5)<b>TestRoutes</b> – Test class to test the routes established in controller and service classes. 
