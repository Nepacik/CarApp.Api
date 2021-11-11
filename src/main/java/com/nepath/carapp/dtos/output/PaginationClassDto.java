package com.nepath.carapp.dtos.output;

import lombok.Data;

import java.util.List;

@Data
public class PaginationClassDto<T> {
    private List<T> elements;
    private int elementsPerPage = 20;
    private int page;

    public PaginationClassDto(List<T> elements, int page) {
        this.elements = elements;
        this.page = page;
    }
}
