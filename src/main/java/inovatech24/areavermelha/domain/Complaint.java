package inovatech24.areavermelha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Complaint {
    private Long id;
    private String occurrenceCode;

    @NotBlank(message = "CEP cannot be empty")
    private String zipCode;

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
}
