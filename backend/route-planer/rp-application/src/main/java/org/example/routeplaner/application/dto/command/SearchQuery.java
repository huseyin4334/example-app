package org.example.routeplaner.application.dto.command;

import lombok.Getter;
import lombok.Setter;
import org.example.common.application.PageQuery;

@Getter @Setter
public class SearchQuery extends PageQuery {
    private String searchTerm;

    public SearchQuery(String searchTerm, int pageNo, int pageSize) {
        super(pageNo, pageSize);
        this.searchTerm = searchTerm;
    }
}
