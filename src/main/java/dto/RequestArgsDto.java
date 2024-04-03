package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestArgsDto {
    @JsonProperty("arch")
    private String arch;
}
