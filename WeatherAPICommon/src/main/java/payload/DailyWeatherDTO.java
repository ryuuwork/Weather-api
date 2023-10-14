package payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Setter
@Getter
@JsonPropertyOrder({"day_of_month", "month", "min_temp", "max_temp", "precipitation", "status"})
public class DailyWeatherDTO {
    @JsonProperty("day_of_month")
    @Range(min = 1, max = 31, message = "Day of month must be between 1-31")
    private int dayOfMonth;
    @Range(min = 1, max = 12, message = "Month must be between 1-12")
    private int month;
    @JsonProperty("min_temp")
    @Range(min = -60, max = 60, message = "Minimum temperature must be in range of -60 to 60 Celsius degree")
    private int minTemp;
    @JsonProperty("max_temp")
    @Range(min = -60, max = 60, message = "Maximum temperature must be in range of -60 to 60 Celsius degree")
    private int maxTemp;
    @Range(min = 0, max = 100, message = "Precipitation must be in range of 0 to 100 percentage")
    private int precipitation;
    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    private String status;

    @Override
    public String toString() {
        return "DailyWeatherDTO{" +
                "dayOfMonth=" + dayOfMonth +
                ", month=" + month +
                ", minTemp=" + minTemp +
                ", maxTemp=" + maxTemp +
                ", precipitation=" + precipitation +
                ", status='" + status + '\'' +
                '}';
    }
}
