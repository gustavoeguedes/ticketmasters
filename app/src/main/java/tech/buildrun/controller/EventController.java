package tech.buildrun.controller;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import tech.buildrun.controller.dto.CreateEventDto;
import tech.buildrun.service.EventService;

import java.net.URI;

@Path("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GET
    public Response getEvents(@QueryParam("page") @DefaultValue("0") Integer page,
                                @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        var events = eventService.findAll(page, pageSize);

        return Response.ok(events).build();
    }

    @POST
    @Transactional
    public Response createEvent(CreateEventDto dto) {
        var eventCreated = eventService.createEvent(dto);
        var location = URI.create(String.format("/events/%d", eventCreated.id()));
        return Response.created(location).build();
    }

    @GET
    @Path("{id}")
    public Response getEvent(Long id) {
        var event = eventService.findById(id);

        return event.isPresent()
                ? Response.ok(event.get()).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}/seats")
    public Response getEventSeats(Long id,
                                  @QueryParam("page") @DefaultValue("0") Integer page,
                                  @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        var resp = eventService.findAllSeats(id, page, pageSize);

        return Response.ok(resp).build();
    }
}
