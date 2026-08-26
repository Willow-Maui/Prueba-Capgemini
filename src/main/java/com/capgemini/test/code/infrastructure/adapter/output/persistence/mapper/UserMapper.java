package com.capgemini.test.code.infrastructure.adapter.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.user.UserEntity;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user.UserReadEntity;

/**
 * UserMapper - Mapeador de conversión entre capas
 * Convierte entre: User (dominio) ↔ UserEntity (WriteDB) ↔ UserReadEntity (ReadDB) ↔ UserDTO (aplicación)
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Domain → Entity (WriteDB)
    default UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserEntity.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .dni(user.getDni())
            .phone(user.getPhone())
            .role(user.getRole().name())
            .roomId(user.getRoomId())
            .build();
    }

    // Entity (WriteDB) → Domain
    default User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
            .name(entity.getName())
            .email(entity.getEmail())
            .dni(entity.getDni())
            .phone(entity.getPhone())
            .role(entity.getRole())
            .roomId(entity.getRoomId())
            .build();
    }

    // Entity (ReadDB) → Domain
    default User readToDomain(UserReadEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
            .name(entity.getName())
            .email(entity.getEmail())
            .dni(entity.getDni())
            .phone(entity.getPhone())
            .role(entity.getRole())
            .roomId(entity.getRoomId())
            .build();
    }

    // Domain → DTO
    default UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .dni(user.getDni())
            .phone(user.getPhone())
            .rol(user.getRole().name())
            .roomId(user.getRoomId())
            .build();
    }

    // Entity (ReadDB) → DTO
    default UserDTO readToDTO(UserReadEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .email(entity.getEmail())
            .dni(entity.getDni())
            .phone(entity.getPhone())
            .rol(entity.getRole())
            .roomId(entity.getRoomId())
            .build();
    }

    // Entity (WriteDB) → Entity (ReadDB)
    default UserReadEntity toReadEntity(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserReadEntity.builder()
            .id(entity.getId())
            .name(entity.getName())
            .email(entity.getEmail())
            .dni(entity.getDni())
            .phone(entity.getPhone())
            .role(entity.getRole())
            .roomId(entity.getRoomId())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}




