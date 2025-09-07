package ru.yandex.practicum.ewmmainserver.model.participationRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<RequestDto> confirmedRequests;

    private List<RequestDto> rejectedRequests;
}
