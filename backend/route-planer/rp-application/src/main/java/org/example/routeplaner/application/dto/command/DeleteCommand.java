package org.example.routeplaner.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeleteCommand<ID> {
    private ID id;
}
