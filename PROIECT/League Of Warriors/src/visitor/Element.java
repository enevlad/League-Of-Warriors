package visitor;

import entities.Entity;

public interface Element <T extends Entity> {
    void accept(Visitor<T> visitor);
}
