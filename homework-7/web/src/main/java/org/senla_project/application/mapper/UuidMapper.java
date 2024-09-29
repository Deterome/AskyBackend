package org.senla_project.application.mapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UuidMapper {

    public UUID toUuidFromString(String uuidString) {
        return UUID.fromString(uuidString);
    }

    public String toStringFromUuid(@NonNull UUID uuid) {
        return uuid.toString();
    }

}
