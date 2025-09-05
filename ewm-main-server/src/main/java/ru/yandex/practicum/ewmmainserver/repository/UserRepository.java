package ru.yandex.practicum.ewmmainserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByIdIn(List<Long> ids);

    List<Long> id(Long id);
}
