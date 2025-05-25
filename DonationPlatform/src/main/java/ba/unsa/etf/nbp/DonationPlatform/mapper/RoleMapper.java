package ba.unsa.etf.nbp.DonationPlatform.mapper;

import ba.unsa.etf.nbp.DonationPlatform.dto.RoleDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleMapper() {}
    public RoleDTO mapToRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(role.getName());
        return roleDTO;
    }
}
