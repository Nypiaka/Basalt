package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ArchVersionDto {
    @JsonProperty("arch")
    String arch;
    @JsonProperty("count")
    Long count;
}
