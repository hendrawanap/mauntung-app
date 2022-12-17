package com.mauntung.mauntung.application.port.membership;

public interface CreatePointMembershipUseCase {
    CreatePointMembershipResponse apply(CreatePointMembershipCommand command);
}
