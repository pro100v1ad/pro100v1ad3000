package main.java.com.pro100v1ad3000.ui.menus.playerSettings.utils;

import java.util.Objects;

public class CompositeKey {
    private final Integer first;
    private final Boolean second;

    public CompositeKey(Integer first, Boolean second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
        return first.equals(that.first) && second.equals(that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}

