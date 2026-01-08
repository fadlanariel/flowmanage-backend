package com.flowmanage.common;

import org.springframework.data.domain.*;

public class PageableUtil {

    public static Pageable sanitize(
            Pageable pageable,
            String defaultSortField,
            Sort.Direction defaultDirection,
            Iterable<String> allowedSortFields) {
        int page = Math.max(pageable.getPageNumber(), PaginationConstants.DEFAULT_PAGE);

        int size = pageable.getPageSize();
        if (size <= 0) {
            size = PaginationConstants.DEFAULT_SIZE;
        } else if (size > PaginationConstants.MAX_SIZE) {
            size = PaginationConstants.MAX_SIZE;
        }

        Sort sort = pageable.getSort().stream()
                .filter(order -> {
                    for (String allowed : allowedSortFields) {
                        if (allowed.equals(order.getProperty())) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst()
                .map(order -> Sort.by(order.getDirection(), order.getProperty()))
                .orElse(Sort.by(defaultDirection, defaultSortField));

        return PageRequest.of(page, size, sort);
    }
}
