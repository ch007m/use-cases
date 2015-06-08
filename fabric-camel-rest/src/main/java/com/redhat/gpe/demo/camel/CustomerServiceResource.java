/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.redhat.gpe.demo.camel;

import com.redhat.gpe.demo.camel.model.Customer;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/customerservice/")
@Api(value = "/customerservice", description = "Operations about customerservice")
public class CustomerServiceResource {

    public CustomerServiceResource() {
    }

    @GET
    @Path("/customers/{id}/")
    @ApiOperation(value = "Find Customer by ID", notes = "More notes about this method", response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Invalid ID supplied"),
            @ApiResponse(code = 204, message = "Customer not found")
    })
    public Response getCustomer(@ApiParam(value = "ID of Customer to fetch", required = true) @PathParam("id") String id) {
        return null;
    }

    @PUT
    @Path("/customers/")
    @Consumes({"application/xml", "application/json"})
    @ApiOperation(value = "Update an existing Customer")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Invalid ID supplied"),
            @ApiResponse(code = 204, message = "Customer not found")
    })
    public Response updateCustomer(@ApiParam(value = "Customer object that needs to be updated", required = true) Customer customer) {
        return null;
    }

    @POST
    @Path("/customers/")
    @Consumes({"application/xml", "application/json"})
    @ApiOperation(value = "Add a new Customer")
    @ApiResponses(value = {@ApiResponse(code = 500, message = "Invalid ID supplied"),})
    public Response addCustomer(@ApiParam(value = "Customer object that needs to be updated", required = true) Customer customer) {
        return null;
    }

    @DELETE
    @Path("/customers/{id}/")
    @ApiOperation(value = "Delete Customer")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Invalid ID supplied"),
            @ApiResponse(code = 204, message = "Customer not found")
    })
    public Response deleteCustomer(@ApiParam(value = "ID of Customer to delete", required = true) @PathParam("id") String id) {
        return null;
    }
}