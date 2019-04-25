package hr.foi.diplomski.central.controllers.api.roles;

import hr.foi.diplomski.central.controllers.api.roles.data.RolesDto;
import hr.foi.diplomski.central.service.roles.RolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RolesController {

    private final RolesService rolesService;

    @GetMapping
    public ResponseEntity<List<RolesDto>> getAllRoles() {
        return ResponseEntity.ok(rolesService.findAllRoles());
    }

}
