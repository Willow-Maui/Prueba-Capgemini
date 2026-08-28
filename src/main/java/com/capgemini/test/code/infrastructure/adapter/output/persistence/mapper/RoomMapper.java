package com.capgemini.test.code.infrastructure.adapter.output.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.capgemini.test.code.domain.room.model.Room;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.room.RoomEntity;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.room.RoomReadEntity;

/**
 * RoomMapper - Mapeador de conversión entre capas
 * Convierte entre: Room (dominio) ↔ RoomEntity (WriteDB) ↔ RoomReadEntity (ReadDB)
 */
@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    // Domain → Entity (WriteDB)
    default RoomEntity toEntity(Room room) {
        if (room == null) {
            return null;
        }

        return RoomEntity.builder()
            .id(room.getId())
            .name(room.getName())
            .build();
    }

    // Entity (WriteDB) → Domain
    default Room toDomain(RoomEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Room(entity.getId(), entity.getName());
    }

    // Entity (ReadDB) → Domain
    default Room readToDomain(RoomReadEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Room(entity.getId(), entity.getName());
    }

    // Entity (WriteDB) → Entity (ReadDB)
    default RoomReadEntity toReadEntity(RoomEntity entity) {
        if (entity == null) {
            return null;
        }

        return RoomReadEntity.builder()
            .id(entity.getId())
            .name(entity.getName())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}

