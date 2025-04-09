package ba.unsa.etf.nbp.DonationPlatform.dto;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String address;

    public UserDTO(Long id, String username, String email, String role, String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }


}