package com.mauntung.mauntung.adapter.http.controller;

import com.mauntung.mauntung.adapter.http.request.reward.CreateRewardRequest;
import com.mauntung.mauntung.adapter.http.response.reward.CreateRewardResponseBody;
import com.mauntung.mauntung.application.port.reward.CreateRewardCommand;
import com.mauntung.mauntung.application.port.reward.CreateRewardResponse;
import com.mauntung.mauntung.application.port.reward.CreateRewardUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequiredArgsConstructor
public class RewardController {
    private final CreateRewardUseCase createRewardUseCase;

    @PreAuthorize("hasRole('MERCHANT')")
    @PostMapping("merchant/rewards")
    public ResponseEntity<CreateRewardResponseBody> createReward(@RequestBody @Valid CreateRewardRequest request) {
        CreateRewardCommand command = buildCreateRewardCommand(request);
        CreateRewardResponse response = createRewardUseCase.apply(command);
        return new ResponseEntity<>(new CreateRewardResponseBody(response), HttpStatus.CREATED);
    }

    private CreateRewardCommand buildCreateRewardCommand(CreateRewardRequest request) {
        CreateRewardCommand.Builder commandBuilder = CreateRewardCommand.builder(
            request.getName(),
            request.getDescription(),
            request.getTermsCondition(),
            request.getCost()
        ).stock(request.getStock());

        boolean startPeriodIsProvided = request.getStartPeriod() != null && !request.getStartPeriod().equals("");
        boolean endPeriodIsProvided = request.getEndPeriod() != null && !request.getEndPeriod().equals("");

        if (startPeriodIsProvided && endPeriodIsProvided) {
            try {
                Date startPeriod = parseStringDateToDate(request.getStartPeriod());
                Date endPeriod = parseStringDateToDate(request.getEndPeriod());
                commandBuilder.periods(startPeriod, endPeriod);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid Date Format Provided (valid: yyyy-mm-dd; e.g. 2022-12-20)");
            }
        } else if (startPeriodIsProvided || endPeriodIsProvided) {
            throw new IllegalArgumentException("Both Start Period & End Period must be provided or must be blank");
        }

        return commandBuilder.build();
    }

    private Date parseStringDateToDate(String stringDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");
        return formatter.parse(stringDate);
    }
}
