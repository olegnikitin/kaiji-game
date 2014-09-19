package com.softserveinc.ita.kaiji.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Shevchuk
 * @version 1.3
 * @since 15.08.14.
 */

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
