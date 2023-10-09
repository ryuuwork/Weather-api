package com.tuananhdo.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
public class HourlyWeatherId implements Serializable {
    private int hourOfDay;
    @ManyToOne
    @JoinColumn(name = "location_code")
    private Location location;
}
