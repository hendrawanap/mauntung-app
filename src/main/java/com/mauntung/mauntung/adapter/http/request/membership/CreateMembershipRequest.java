package com.mauntung.mauntung.adapter.http.request.membership;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class CreateMembershipRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @NotNull
    private Rules rules;

    @NotNull
    private Set<Long> rewardIds;

    private Set<Long> tierIds;

    private Long mediaId;
}
