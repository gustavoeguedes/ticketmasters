package tech.buildrun.controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import tech.buildrun.controller.dto.BookingResponseDto;
import tech.buildrun.controller.dto.ConfirmBookingDto;
import tech.buildrun.controller.dto.CreateBookingDto;
import tech.buildrun.service.BookingService;
import tech.buildrun.service.ConfirmBookingService;

@Path("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ConfirmBookingService confirmBookingService;

    public BookingController(BookingService bookingService,
                             ConfirmBookingService confirmBookingService) {
        this.bookingService = bookingService;
        this.confirmBookingService = confirmBookingService;
    }

    @POST
    public Response createBooking(@Valid CreateBookingDto dto) {
        var bookingId = bookingService.createBooking(dto);

        return Response.ok(new BookingResponseDto(bookingId)).build();

    }

    @POST
    @Path("/confirm")
    public Response confirmBooking(@Valid ConfirmBookingDto dto) {

        confirmBookingService.confirmBooking(dto.bookingId());

        return Response.noContent().build();
    }
}
