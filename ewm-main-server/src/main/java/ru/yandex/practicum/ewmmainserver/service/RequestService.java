package ru.yandex.practicum.ewmmainserver.service;

import ru.yandex.practicum.ewmmainserver.model.participationRequest.dto.RequestDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getUserRequests(long userId);

    RequestDto createRequest(long userId, long eventId);

    RequestDto cancelUserRequest(long userId, long requestId);
}
