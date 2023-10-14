package payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HourlyWeatherDTO {
    private int hourOfDay;
    private int temperature;
    private int precipitation;
    private String status;

    @Override
    public String toString() {
        return "HourlyWeatherDTO{" +
                "hourOfDay=" + hourOfDay +
                ", temperature=" + temperature +
                ", precipitation=" + precipitation +
                ", status='" + status + '\'' +
                '}';
    }
}
