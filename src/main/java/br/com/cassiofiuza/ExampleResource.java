package br.com.cassiofiuza;

import static java.util.Objects.isNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Variant.VariantListBuilder;

import org.jboss.resteasy.annotations.Body;

import br.com.cassiofiuza.authentication.Role;
import br.com.cassiofiuza.dto.CreateUserDTO;

@Path("/auth")
public class ExampleResource {

    @POST
    @Path("/register")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response register(CreateUserDTO createUserDTO) {
        String username = createUserDTO.getUsername();
        String password = createUserDTO.getPassword();
        Set<Role> roles = createUserDTO.getRoles();

        if (isNull(username) || username.length() < 6) {
            Response.status(Status.BAD_REQUEST).
        }
    }

}