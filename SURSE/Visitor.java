package visitor;

import entities.Entity;

public interface Visitor <T extends Entity> {
    void visit(T entity);
}
