package org.example.routeplaner.loader;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class Airport implements Serializable {
    private String icao;
    private String name;
    private String city;
    private String country;
}
