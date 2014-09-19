package com.softserveinc.ita.kaiji.sse.dto;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 26.08.14.
 */

public class InvitePlayerDto {

    private Integer number;
    private String name;
    private Boolean isPlaying;

    public InvitePlayerDto(Integer number, String name, Boolean isPlaying) {
        this.number = number;
        this.name = name;
        this.isPlaying = isPlaying;
    }

}

