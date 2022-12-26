package com.mauntung.mauntung.adapter.http.request.membership;

import com.mauntung.mauntung.common.validator.ValueOfEnum;
import com.mauntung.mauntung.domain.model.membership.Membership;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class CreateMembershipRequest {
    @NotBlank()
    private String name;

    @NotBlank()
    @ValueOfEnum(enumClass = Membership.Type.class, message = "with value '${validatedValue}' is not valid enum")
    private String type;

    @Valid
    @NotNull()
    private Rules rules;

    @NotNull
    private Set<Long> rewardIds;

    private Set<Long> tierIds;

    private Long mediaId;
}
