package xyz.mcutils.backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class Tuple<L, R> {

    /**
     * The left value of the tuple.
     */
    private final L left;

    /**
     * The right value of the tuple.
     */
    private final R right;
}
