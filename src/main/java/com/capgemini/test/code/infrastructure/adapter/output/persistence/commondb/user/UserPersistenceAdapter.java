package com.capgemini.test.code.infrastructure.adapter.output.persistence.commondb.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.domain.user.repository.UserRepository;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user.UserReadAdapter;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user.UserReadJpaRepository;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.user.UserWriteAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepository {
    private final UserReadAdapter userReadAdapter;
    private final UserWriteAdapter userWriteAdapter;

    @Override
    public UserDTO findById(Long id) {
        return userReadAdapter.findById(id);
    }

    @Override
    public UserDTO findByEmail(String email) {
        return userReadAdapter.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userReadAdapter.existsByEmail(email);
    }

    @Override
    public boolean existsByDni(String dni) {
        return userReadAdapter.existsByDni(dni);
    }

    @Override
    public UserDTO save(UserDTO user) {
        return userWriteAdapter.save(user);
    }

    @Override
    public UserDTO findByIdAndRoomId(Long userId, Long roomId) {
        return userReadAdapter.findByIdAndRoomId(userId, roomId);
    }
}
