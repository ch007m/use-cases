package my.cool.demo;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/message")
@Consumes( { "application/json", "text/json" } )
@Produces( { "application/json", "text/json" } )
public interface Message {

    @GET @Path("/{param}") Response printMessage(@PathParam("param") String msg);
}