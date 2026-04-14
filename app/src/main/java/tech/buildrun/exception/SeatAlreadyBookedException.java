package tech.buildrun.exception;

public class SeatAlreadyBookedException extends TicketMasterException{
    private final String seatName;

    public SeatAlreadyBookedException(String seatName) {
        this.seatName = seatName;
    }

    @Override
    protected ProblemDetails toProblemDetails() {
        var status = 422;
        return new ProblemDetails(
                new ExceptionResponse(
                        "about:blank",
                        "Seat already booked",
                        "This seat " + seatName + " is already booked",
                        status,
                        null
                ),
                status
        );
    }
}
