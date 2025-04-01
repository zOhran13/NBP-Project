package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.model.VolunteerShift;
import ba.unsa.etf.nbp.DonationPlatform.service.VolunteerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping("/register")
    public ResponseEntity<VolunteerShift> registerVolunteer(@RequestBody VolunteerShift shift) {
        return ResponseEntity.ok(volunteerService.registerVolunteer(shift));
    }

    @GetMapping("/user/{userId}/shifts")
    public List<VolunteerShift> getUserShifts(@PathVariable Long userId) {
        return volunteerService.getUserShifts(userId);
    }
}