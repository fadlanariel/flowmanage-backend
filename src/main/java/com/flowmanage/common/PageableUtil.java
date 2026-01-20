package com.flowmanage.common;

import org.springframework.data.domain.*;
import java.util.Set;

public class PageableUtil {

    public static Pageable sanitize(
            Pageable pageable,
            String defaultSortField,
            Sort.Direction defaultDirection,
            Set<String> allowedSortFields) {

        int page = Math.max(pageable.getPageNumber(), PaginationConstants.DEFAULT_PAGE);

        int size = pageable.getPageSize();
        if (size <= 0) {
            size = PaginationConstants.DEFAULT_SIZE;
        } else if (size > PaginationConstants.MAX_SIZE) {
            size = PaginationConstants.MAX_SIZE;
        }

        Sort sort = Sort.unsorted();

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                if (allowedSortFields.contains(order.getProperty())) {
                    sort = Sort.by(order.getDirection(), order.getProperty());
                    break;
                }
            }
        }

        if (sort.isUnsorted()) {
            sort = Sort.by(defaultDirection, defaultSortField);
        }

        return PageRequest.of(page, size, sort);
    }
}
