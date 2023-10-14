package com.tuananhdo.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
public class HourlyWeatherId implements Serializable {
    private int hourOfDay;
    @ManyToOne
    @JoinColumn(name = "location_code")
    private Location location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HourlyWeatherId weatherId = (HourlyWeatherId) o;
        return hourOfDay == weatherId.hourOfDay && Objects.equals(location, weatherId.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hourOfDay, location);
    }
}
