package io.dabou.service;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/payments")
@Api(value = "/payments", description = "Operations about payments")
@Produces({"application/xml", "application/json"})
@CrossOriginResourceSharing(allowAllOrigins = true)
public interface PaymentService {

    @GET @Path("/payment")
    @ApiOperation(value = "Find payment", notes = "Returns an individual payment")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
                            @ApiResponse(code = 404, message = "Payment not found") })
    public String getIndividualPayment();

}
