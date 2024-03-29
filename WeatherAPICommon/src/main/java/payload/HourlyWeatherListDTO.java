package payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@JsonPropertyOrder({"location", "hourly_forecast"})
public class HourlyWeatherListDTO extends RepresentationModel<HourlyWeatherListDTO> {
    private String location;
    @JsonProperty("hourly_forecast")
    private List<HourlyWeatherDTO> hourlyWeatherDTOS = new ArrayList<>();
}
