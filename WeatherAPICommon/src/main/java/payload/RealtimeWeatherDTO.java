package payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
public class RealtimeWeatherDTO extends RepresentationModel<RealtimeWeatherDTO> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String location;
    @Range(min = -50, max = 50, message = "Temperature must be in the range of -50 to 50 Celsius degree ")
    private int temperature;
    @Range(min = 0, max = 100, message = "Humidity must be in the range of 0 to 100 percentage ")
    private int humidity;
    @Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage ")
    private int precipitation;
    @Range(min = 0, max = 200, message = "Wind speed must be in the range of 0 to 200 km/h ")
    private int windSpeed;
    @Column(length = 50)
    @NotBlank(message = "Status must not be empty")
    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealtimeWeatherDTO that = (RealtimeWeatherDTO) o;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }


    @Override
    public String toString() {
        return "RealtimeWeatherDTO{" +
                "location='" + location + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", precipitation=" + precipitation +
                ", windSpeed=" + windSpeed +
                ", status='" + status + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
