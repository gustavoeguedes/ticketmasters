package tech.buildrun.controller.dto;

public record PaginationDto(
        Integer page,
        Integer pageSize,
        Integer totalItems,
        Integer totalPages
) {
}
