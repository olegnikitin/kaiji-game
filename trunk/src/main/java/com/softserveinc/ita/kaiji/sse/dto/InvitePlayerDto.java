package com.softserveinc.ita.kaiji.sse.dto;

public class InvitePlayerDto {

    private Integer number;
    private String name;
    private boolean isPlaying;

    public InvitePlayerDto(Integer number, String name, boolean isPlaying) {
        this.number = number;
        this.name = name;
        this.isPlaying = isPlaying;
    }

}

