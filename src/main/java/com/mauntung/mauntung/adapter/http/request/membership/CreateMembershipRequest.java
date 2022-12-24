package com.mauntung.mauntung.adapter.http.request.membership;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class CreateMembershipRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Type must not be blank")
    private String type;

    @NotNull(message = "Rules must not be null")
    private Rules rules;

    @NotNull
    private Set<Long> rewardIds;

    private Set<Long> tierIds;

    private Long mediaId;
}
