package com.capgemini.test.code.infrastructure.adapter.output.event;

import com.capgemini.test.code.domain.user.model.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Deserializador personalizado para la clase User.
 *
 * Permite deserializar JSON a User usando el patrón Builder de la clase,
 * ya que User tiene campos final y no posee un constructor sin argumentos.
 */
public class UserDeserializer extends JsonDeserializer<User> {

    @Override
    public User deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        Long id = null;
        if (node.has("id") && !node.get("id").isNull()) {
            id = node.get("id").asLong();
        }

        String name = node.has("name") ? node.get("name").asText() : null;
        String email = node.has("email") ? node.get("email").asText() : null;
        String dni = node.has("dni") ? node.get("dni").asText() : null;
        String phone = node.has("phone") && !node.get("phone").isNull() ? node.get("phone").asText() : null;
        String role = node.has("role") ? node.get("role").asText() : null;
        Long roomId = node.has("roomId") ? node.get("roomId").asLong() : null;

        // Usar el builder para construir el User con validaciones
        User.Builder builder = User.builder()
                .name(name)
                .email(email)
                .dni(dni)
                .phone(phone)
                .role(role)
                .roomId(roomId);

        // Si tiene ID, usar buildWithId, sino usar build
        if (id != null) {
            return builder.buildWithId(id);
        } else {
            return builder.build();
        }
    }
}

