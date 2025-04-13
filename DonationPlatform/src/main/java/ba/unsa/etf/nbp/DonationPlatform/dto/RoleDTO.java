package ba.unsa.etf.nbp.DonationPlatform.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleDTO {

    private String name;

    // Default constructor
    public RoleDTO() {}

    // Constructor
    public RoleDTO(Long id, String name) {
        this.name = name;
    }

    // Getters and Setters

}
