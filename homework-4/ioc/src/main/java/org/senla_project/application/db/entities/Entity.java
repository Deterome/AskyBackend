package org.senla_project.application.db.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter @Getter
public abstract class Entity implements Cloneable {

    protected UUID id = UUID.randomUUID();

    @Override
    public Entity clone() {
        try {
            Entity clone = (Entity) super.clone();
            clone.id = id;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
