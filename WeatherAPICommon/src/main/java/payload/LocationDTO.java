package payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
public class LocationDTO {
    @NotBlank(message = "Location code cannot be left blank")
    @Length(min = 2, max = 12, message = "Location code must have 2-12 characters")
    private String code;
    @NotBlank(message = "City name cannot be left blank")
    @Length(min = 2, max = 128, message = "City name must have 2-128 characters")
    private String cityName;
    @NotBlank(message = "Region name cannot be left blank")
    @Length(min = 2, max = 128, message = "Region name must have 2-128 characters")
    private String regionName;
    @NotBlank(message = "Country name cannot be left blank")
    @Length(min = 2, max = 64, message = "Country name must have 2-64 characters")
    private String countryName;
    @Length(min = 2, max = 64, message = "Country code must have 2-64 characters")
    @NotBlank(message = "Country code cannot be left blank")
    private String countryCode;
    private boolean enabled;
    @JsonIgnore
    private boolean trashed;
    public LocationDTO(String cityName, String regionName, String countryName, String countryCode) {
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationDTO that = (LocationDTO) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public String toString() {
        return cityName + "," + (regionName != null ? regionName : "") + "," + countryName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public LocationDTO code(String code) {
        setCode(code);
        return this;
    }
}
