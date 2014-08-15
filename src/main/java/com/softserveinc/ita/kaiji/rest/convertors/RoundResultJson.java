package com.softserveinc.ita.kaiji.rest.convertors;

import java.util.ArrayList;
import java.util.List;

public class RoundResultJson {

    private Integer id;

    private List<RoundResultEntry> entries = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<RoundResultEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<RoundResultEntry> entries) {
        this.entries = entries;
    }
}
