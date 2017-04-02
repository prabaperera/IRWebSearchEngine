# IRWebSearchEngine

This project created as an assignment which given by Information Retrival and Analysis course. Apache Lucene (https://lucene.apache.org/core/documentation.html) was used as the core of this text search engine.

Below technologies used while developing this project.

Client side technologies
	Html
	Javascript
	Ajax
	jQuery

Server side technologies
	Java
	Json
	Lucene

Third party Dependencies to the project.

Java (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
lucene-6.4.1 2 (http://archive.apache.org/dist/lucene/java/6.4.2/)
gson-2.3.1.jar(https://mvnrepository.com/artifact/com.google.code.gson/gson/2.3.1)
commons-io-2.5.jar (https://mvnrepository.com/artifact/commons-io/commons-io/2.5)


This project tested with Apache tomcat server and there is no any dependency with tomcat server. It support any web server.

Before start, Follow below steps
Step 1: Configure project and it’s locations

To start the Project, first need to configure below locations inside config.properties file (it is inside the class path).

DOC_LOCATION - This is the location of the source document which need to index. If it is a large file, then split file into multiple subfiles which contain less than or equal 10,000 records per each document (If there are lot of records in a file, then index process takes longer time).

INDEX_LOCATION_OF_MERGED_DOCUMENT - Lucene index is going to save inside this location
INDEX_LOCATION_OF_INDIVIDUAL_DOCUMENT-  Lucene index is going to save inside this location
STOP_WORD_LIST - List of stop words can be configure in this file, a single stop word per each line. While indexing and searching, lucent will consider words inside this file as stop words.

Step2: Build index.
Deploy project as a web application to a web server. (While testing Apache tomcat was used as a web server to run this project). Once deploy success fully as a web application, then use below URL access the application.

http://<ip address of the server>:<port of the server>/IRWebSearchEngine

ex:-( http://localhost:8080/IRWebSearchEngine/)

Step3:

Then go to setting tab in the Web UI, and use “build Index”,”build expert user index”, “build frequent word index” links to build separate indexes. Once index process completed, Search engine is ready to operation. 
