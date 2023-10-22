package payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class FullWeatherDTO {
    private String location;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = RealtimeWeatherFieldFilter.class)
    @Valid
    private RealtimeWeatherDTO realtimeWeather = new RealtimeWeatherDTO();
    @JsonProperty("hourly_forecast")
    @Valid
    private List<HourlyWeatherDTO> hourlyWeatherList = new ArrayList<>();
    @JsonProperty("daily_forecast")
    @Valid
    private List<DailyWeatherDTO> dailyWeatherList = new ArrayList<>();
}
