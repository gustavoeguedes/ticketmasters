package tech.buildrun.exception;

public class SeatAlreadyBookedException extends TicketMasterException{
    private final String seatName;

    public SeatAlreadyBookedException(String seatName) {
        this.seatName = seatName;
    }

    @Override
    protected ProblemDetails toProblemDetails() {
        return new ProblemDetails(
                new ExceptionResponse("SeatAlreadyBookedException", "Seat already booked", "Seat with name " + seatName + " is already booked"),
                422
        );
    }
}
