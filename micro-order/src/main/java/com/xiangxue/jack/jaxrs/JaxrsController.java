package com.xiangxue.jack.jaxrs;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/jaxrs")
public class JaxrsController {

    @GET
    @Path("/queryUser")
    @Produces(MediaType.APPLICATION_JSON)
    public String queryUser() {
        return "xx";
    }
}
