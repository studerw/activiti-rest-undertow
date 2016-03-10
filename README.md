# activiti-rest-undertow

Activiti Rest with Undertow.

This is a simple example of adding the Activiti Rest functionality to your own application using
the Undertow Servlet container as the backend.

The Activiti configuration can be customized in the *src/main/resources/spring/app.properties*. By default,
it will use an embedded H2 Database.

To build the application:

`mvn clean install`

To run the application:

`mvn exec:java`

At this point, you will have access to the full [Activiti Rest API](http://www.activiti.org/userguide/#_rest_api):

To test the rest API, use Curl or a browser with a JSON formatting plugin (e.g. Chrome JSONView):

`curl http://localhost:8080/myapp/service/repository/process-definitions`

The result should be the single deployed definition:
```javascript
{

     "data":

 [

         {
             "id": "reviewSaledLead:1:4",
             "url": "http://localhost:8080/myapp/service/repository/process-definitions/reviewSaledLead:1:4",
             "key": "reviewSaledLead",
             "version": ​1,
             "name": "Review sales lead",
             "description": null,
             "tenantId": "",
             "deploymentId": "1",
             "deploymentUrl": "http://localhost:8080/myapp/service/repository/deployments/1",
             "resource": "http://localhost:8080/myapp/service/repository/deployments/1/resources/.../activiti-rest-undertow\\target\\classes\\bpmn\\reviewSalesLead.bpmn20.xml",
             "diagramResource": "http://localhost:8080/myapp/service/repository/deployments/1/resources/.../activiti-rest-undertow\\target\\classes\\bpmn\\reviewSalesLead.reviewSaledLead.png",
             "category": "Examples",
             "graphicalNotationDefined": true,
             "suspended": false,
             "startFormDefined": false
         }
     ],
     "total": ​1,
     "start": ​0,
     "sort": "name",
     "order": "asc",
     "size": ​1

 }
 ```