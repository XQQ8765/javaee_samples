1. Create parent module
mvn archetype:generate -DgroupId=com.dell.rabbit.javaeesample -DartifactId=rabbit-javaeesample -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
2: Create child webapp module
mvn archetype:generate -DgroupId=com.dell.rabbit.javaeesample.jaxws -DartifactId=jaxws_hello -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

http://localhost:8988/jaxws_hello/HelloWs

mvn archetype:create -DgroupId=com.dell.rabbit.javaeesample.jaxws3 -DartifactId=jaxws_hello3 -DarchetypeArtifactId=maven-archetype-quickstart
mvn archetype:create -DgroupId=com.dell.rabbit.javaeesample.jaxws3 -DartifactId=jaxws_hello3 -DarchetypeArtifactId=maven-archetype-webapp
