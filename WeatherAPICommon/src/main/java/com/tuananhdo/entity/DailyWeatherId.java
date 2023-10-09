package com.tuananhdo.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeatherId implements Serializable {
    private int dayOfMonth;
    private int month;
    @ManyToOne
    @JoinColumn(name = "location_code")
    private Location location;
}
