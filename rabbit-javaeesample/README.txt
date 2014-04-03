1. Create parent module
mvn archetype:generate -DgroupId=com.dell.rabbit.javaeesample -DartifactId=rabbit-javaeesample -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
2: Create child webapp module
mvn archetype:generate -DgroupId=com.dell.rabbit.javaeesample.service -DartifactId=jaxws_hello -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

Generate your java files using the jaxws-plugin: mvn jaxws:wsimport

http://localhost:8988/jaxws_hello/action/accessMySQL
http://localhost:8988/jaxws_hello/action/sayHello?content=Rabbit
http://localhost:8988/jaxws_hello/service/HelloWs
http://localhost:8988/jaxws_hello/service/HelloWs?wsdl

3. Create JMS webapp module
mvn archetype:generate -DgroupId=com.dell.rabbit.javaeesample.jms -DartifactId=jms_sample -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

http://localhost:8988/jms_sample/sendjms?messagesCount=2&content=Welcome
