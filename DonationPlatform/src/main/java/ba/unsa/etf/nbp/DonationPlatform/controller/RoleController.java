package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.dto.RoleDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Role;
import ba.unsa.etf.nbp.DonationPlatform.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Get all roles
    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    // Get role by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        Optional<RoleDTO> role = roleService.getById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get role by name
    @GetMapping("/name/{name}")
    public ResponseEntity<RoleDTO> getRoleByName(@PathVariable String name) {
        Optional<RoleDTO> role = roleService.getRoleByName(name);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new role
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        // You can modify this to convert RoleDTO to Role if needed
        Role role = new Role(roleDTO.getName());
        Role savedRole = roleService.createRole(role);
        return new ResponseEntity<>(new RoleDTO(savedRole.getId(), savedRole.getName()), HttpStatus.CREATED);
    }

    // Delete role by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
