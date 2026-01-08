package com.flowmanage.common;

import java.util.Set;

public final class PaginationConstants {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 100;

    public static final String DEFAULT_SORT_FIELD = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    public static final Set<String> PROJECT_SORT_FIELDS = Set.of("createdAt", "name");

    public static final Set<String> TASK_SORT_FIELDS = Set.of("createdAt", "title", "status");

    private PaginationConstants() {
    }
}
