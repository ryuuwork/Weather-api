package payload;

public class RealtimeWeatherFieldFilter {
    public boolean equals(Object object) {
        if (object instanceof RealtimeWeatherDTO realtimeWeatherDTO){
            return realtimeWeatherDTO.getStatus() == null;
        }
        return false;
    }
}
