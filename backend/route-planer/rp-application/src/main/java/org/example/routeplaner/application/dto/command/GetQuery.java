package org.example.routeplaner.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetQuery<ID> {
    private ID id;
}
