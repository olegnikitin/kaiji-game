package com.softserveinc.ita.kaiji.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class RoundResultRestDto {

    private Integer id;

    private List<RoundResultEntryDto> entries = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<RoundResultEntryDto> getEntries() {
        return entries;
    }

    public void setEntries(List<RoundResultEntryDto> entries) {
        this.entries = entries;
    }
}
