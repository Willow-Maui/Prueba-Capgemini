package com.capgemini.test.code.infrastructure.adapter.output.event;

import com.capgemini.test.code.domain.room.model.Room;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Deserializador personalizado para la clase Room.
 *
 * Permite deserializar JSON a Room, ya que Room tiene campos final
 * y no posee un constructor sin argumentos.
 */
public class RoomDeserializer extends JsonDeserializer<Room> {

    @Override
    public Room deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        Long id = node.has("id") ? node.get("id").asLong() : null;
        String name = node.has("name") ? node.get("name").asText() : null;

        return new Room(id, name);
    }
}

