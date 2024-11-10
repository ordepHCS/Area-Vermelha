package inovatech24.areavermelha.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Campaign {
    private Long id;

    @NotBlank(message = "Campaign name cannot be empty")
    private String campaignName;

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