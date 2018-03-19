package seedu.ptman.testutil;

import seedu.ptman.commons.core.index.Index;

/**
 * A utility class containing a list of {@code Index} objects to be used in tests.
 */
public class TypicalIndexes {
    public static final Index INDEX_FIRST_EMPLOYEE = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_EMPLOYEE = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_EMPLOYEE = Index.fromOneBased(3);
    public static final Index INDEX_FIRST_SHIFT = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_SHIFT = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_SHIFT = Index.fromOneBased(3);
    public static final Index INDEX_OUT_OF_BOUNDS_SHIFT = Index.fromOneBased(11);
}
