package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PackageDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("epoch")
    private Integer epoch;
    @JsonProperty("version")
    private String version;
    @JsonProperty("release")
    private String release;
    @JsonProperty("arch")
    private String arch;
    @JsonProperty("disttag")
    private String disttag;
    @JsonProperty("buildtime")
    private Integer buildTime;
    @JsonProperty("source")
    private String source;
}
