package com.blog.common.api;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponse<T> {

    private long total;
    private long page;
    private long size;
    private List<T> records;
}

