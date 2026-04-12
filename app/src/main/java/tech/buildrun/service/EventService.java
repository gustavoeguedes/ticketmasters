package tech.buildrun.service;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import tech.buildrun.controller.dto.*;
import tech.buildrun.entity.EventEntity;
import tech.buildrun.entity.SeatEntity;
import tech.buildrun.entity.SeatStatus;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventService {

    public ApiListDto<EventDto> findAll(Integer page, Integer pageSize) {
        var query = EventEntity.findAll()
                .page(Page.of(page, pageSize));

        var count = query.count();
        var totalPages = query.pageCount();


        List<EventDto> events = query.list()
                .stream()
                .map(EventEntity.class::cast)
                .map(EventDto::fromEntity)
                .toList();

        return new ApiListDto<>(events, new PaginationDto(page, pageSize, (int) count, totalPages));
    }

    public EventDto createEvent(CreateEventDto dto) {
        var eventEntity = dto.toEntity();
        eventEntity.persist();

        SeatEntity entitySeat;
        for (int c = 0; c < dto.settings().numberOfSeats(); c++) {
            var seatName = "S" + c;
            entitySeat = new SeatEntity(eventEntity, seatName, SeatStatus.AVAILABLE);
            entitySeat.persist();
        }

        return EventDto.fromEntity(eventEntity);
    }

    public Optional<EventDto> findById(Long id) {
        Optional<EventDto> event = EventEntity.findByIdOptional(id)
                .map(EventEntity.class::cast)
                .map(EventDto::fromEntity);

        return event;
    }

    public ApiListDto<SeatDto> findAllSeats(Long id,
                                            Integer page,
                                            Integer pageSize) {

        var event = EventEntity.findByIdOptional(id).orElseThrow(() -> new RuntimeException("Event not found"));
        var query = SeatEntity.find("event", event)
                .page(Page.of(page, pageSize));

        var totalPages = query.pageCount();
        var totalElements = query.count();
        var events = query.stream()
                .map(SeatEntity.class::cast)
                .map(SeatDto::fromEntity)
                .toList();

        return new ApiListDto<>(events, new PaginationDto(page, pageSize, (int) totalElements, totalPages));
    }
}
