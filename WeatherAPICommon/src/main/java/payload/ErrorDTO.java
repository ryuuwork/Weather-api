package payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorDTO {
    private LocalDateTime timestamp;
    private int status;
    private String path;
    private List<String> errors = new ArrayList<>();
    public void addError(String message) {
        this.errors.add(message);
    }
}
