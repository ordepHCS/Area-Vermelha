package inovatech24.areavermelha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Complaint {
    private Long id;

    private String occurrenceCode = UUID.randomUUID().toString();

    @NotBlank(message = "CEP cannot be empty")
    private String cep;

    @NotBlank(message = "Street cannot be empty")
    private String street;

    @NotBlank(message = "Neighborhood cannot be empty")
    private String neighborhood;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "State cannot be empty")
    private String state;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    private byte[] image;

    private User user;

    private List<String> hashtags;

    //for google maps api
    private Double latitude;
    private Double longitude;
}
