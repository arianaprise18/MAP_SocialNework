package domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Generic immutable pair of two elements, handling symmetric equality.
 *
 * @param <E1> type of first element
 * @param <E2> type of second element
 */
public class Tuple<E1, E2> implements Serializable {
    private final E1 left;
    private final E2 right;

    public Tuple(E1 left, E2 right) {
        this.left = left;
        this.right = right;
    }

    public E1 getLeft() {
        return left;
    }

    public E2 getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tuple)) {
            return false;
        }
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        boolean directMatch = Objects.equals(left, tuple.left) && Objects.equals(right, tuple.right);
        boolean reversedMatch = Objects.equals(left, tuple.right) && Objects.equals(right, tuple.left);
        return directMatch || reversedMatch;
    }

    @Override
    public int hashCode() {
        int hash1 = left != null ? left.hashCode() : 0;
        int hash2 = right != null ? right.hashCode() : 0;
        return hash1 ^ hash2;
    }

    @Override
    public String toString() {
        return "(" + left + ", " + right + ")";
    }
}