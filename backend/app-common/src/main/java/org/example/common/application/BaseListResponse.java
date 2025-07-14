package org.example.common.application;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
public class BaseListResponse<T extends BaseResponse> implements BaseResponse {
    @Setter
    private List<T> items = Collections.emptyList();
    @Setter
    private int page;
    @Setter
    private int pageSize;
    private long total;
    private long totalPages;

    public void setTotal(long total) {
        this.total = total;
        if (pageSize > 0) {
            this.totalPages = (total + pageSize - 1) / pageSize; // Calculate total pages
        } else {
            this.totalPages = 0; // Avoid division by zero
        }
    }
}
