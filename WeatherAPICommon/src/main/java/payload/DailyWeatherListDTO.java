package payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DailyWeatherListDTO {
    private String location;
    @JsonProperty("daily_forecast")
    private List<DailyWeatherDTO> dailyForecast = new ArrayList<>();
}
