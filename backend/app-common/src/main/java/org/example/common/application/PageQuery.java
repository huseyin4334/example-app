package org.example.common.application;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class PageQuery implements BaseCommand {

    @Min(value = 0, message = "Page number must be at least 0")
    private int pageNo;

    @Min(value = 10, message = "Page size must be at least 10")
    private int pageSize;
}
