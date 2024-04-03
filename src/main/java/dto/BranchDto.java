package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class BranchDto {
    @JsonProperty("request_args")
    private RequestArgsDto requestArgs;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("packages")
    private List<PackageDto> packages;
}
