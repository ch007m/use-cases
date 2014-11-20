# CXF REST Swagger Example

* Compile the project using this maven command

    mvn install
    
* During this process, swagger-ui.tar.gz file is downloaded from GitRepo, decompress and dist content moved under src/main/resources/webapp directory
    
* To be able to use the REST Service defined locally, edit the following file webapp/index.html to change the URL location of the swagger-ui
  function
    
      <script type="text/javascript">
        $(function () {
          window.swaggerUi = new SwaggerUi({
          url: "/rest/api/api-docs",
          
* Launch the project locally

    mvn exec:java
    
* Open your browser ate the following address and explode the project
    
    http://localhost:8080/swagger