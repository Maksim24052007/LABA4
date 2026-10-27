import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sms {
    private String phone;
    private String message;
}
