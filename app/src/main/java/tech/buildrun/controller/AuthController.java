package tech.buildrun.controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import tech.buildrun.controller.dto.AccessTokenResponseDto;
import tech.buildrun.controller.dto.LoginRequestDto;
import tech.buildrun.service.AccessTokenService;

@Path("/auth")
public class AuthController {

    private final AccessTokenService accessTokenService;

    public AuthController(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Path("/token")
    @POST
    public Response getToken(@Valid LoginRequestDto dto) {

        var resp = accessTokenService.getAccessToken(dto.username(), dto.password());

        return Response.ok(resp).build();
    }
}
