package my.cool.demo;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public class MessageRestService implements Message {

    @Override
    public Response printMessage(String msg) {
        String result = "Restful example : " + msg;
        return Response.status(200).entity(result).build();
    }

}