package ch.hslu.url_shortener.models;

public class AddOrUpdateResult<T> {
    public AddOrUpdateResult(T entity, boolean isAdded) {
        this.entity = entity;
        this.isAdded = isAdded;
    }

    private final T entity;

    private final boolean isAdded;

    public boolean isAdded() {
        return isAdded;
    }

    public T getEntity() {
        return entity;
    }
}
