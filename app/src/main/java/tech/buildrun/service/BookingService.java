package tech.buildrun.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import tech.buildrun.controller.dto.CreateBookingDto;
import tech.buildrun.controller.dto.ReserveSeatDto;
import tech.buildrun.entity.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookingService {

    @Transactional
    public Long createBooking(CreateBookingDto dto) {

        Set<Long> seatsId = getAllSeatsId(dto);
        var seats = findSeats(seatsId);

        var bookingEntity = buildBookingEntity(dto);
        bookingEntity.persist();

        createTickets(seats, bookingEntity);

        updateSeats(seats);

        return bookingEntity.id;

    }

    private Set<Long> getAllSeatsId(CreateBookingDto dto) {
        return dto.seats().
                stream()
                .map(ReserveSeatDto::seatId)
                .collect(Collectors.toSet());
    }

    private BookingEntity buildBookingEntity(CreateBookingDto dto) {
        BookingEntity booking = new BookingEntity();
        booking.bookedAt = Instant.now();
        booking.status = BookingStatus.PENDING;
        booking.user = UserEntity.findById(dto.userId());

        return booking;

    }

    private Set<SeatEntity> findSeats(Set<Long> seatsId) {
        return SeatEntity.find("id in ?1", seatsId)
                .stream()
                .map(SeatEntity.class::cast)
                .peek(s -> {
                    if (s.status == SeatStatus.BOOKED) {
                        throw new RuntimeException("Seat " + s.id + " is already booked");
                    }
                })
                .collect(Collectors.toSet());
    }

    private void createTickets(Set<SeatEntity> seats, BookingEntity bookingEntity) {
        var ticketEntity = new TicketEntity();
        seats.forEach(s -> {
            ticketEntity.seat = s;
            ticketEntity.externalId = UUID.randomUUID();
            ticketEntity.booking = bookingEntity;
            ticketEntity.persist();
        });
    }

    private void updateSeats(Set<SeatEntity> seats) {
        seats.forEach(s -> {
            s.status = SeatStatus.BOOKED;
            s.persist();
        });
    }

}
