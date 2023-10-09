package payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class HourlyWeatherListDTO {
    private String location;
    @JsonProperty("hourly_forecast")
    private List<HourlyWeatherDTO> hourlyWeatherDTOS = new ArrayList<>();
    public void addWeatherHourlyDTO(HourlyWeatherDTO hourlyWeatherDTO) {
        this.hourlyWeatherDTOS.add(hourlyWeatherDTO);
    }
}
