package com.tuananhdo.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWeatherId that = (DailyWeatherId) o;
        return dayOfMonth == that.dayOfMonth && month == that.month && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfMonth, month, location);
    }
}
