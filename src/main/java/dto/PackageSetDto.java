package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class PackageSetDto {
    @JsonProperty("length")
    Long length;
    @JsonProperty("archs")
    List<ArchVersionDto> archs;
}
