package org.senla_project.application.db.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public abstract class Entity {

    protected UUID id;

}
