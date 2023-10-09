package payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tuananhdo.entity.DailyWeatherId;
import com.tuananhdo.entity.Location;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"day_of_month", "month", "min_temp", "max_temp", "precipitation", "status"})
public class DailyWeatherDTO {
    @JsonProperty("day_of_month")
    private int dayOfMonth;
    private int month;
    @JsonProperty("min_temp")
    private int minTemp;
    @JsonProperty("max_temp")
    private int maxTemp;
    private int precipitation;
    private String status;
}
