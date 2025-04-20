package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.dto.VolunteerShiftDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.VolunteerShift;
import ba.unsa.etf.nbp.DonationPlatform.service.VolunteerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> registerVolunteer(@RequestBody VolunteerShiftDTO shiftDTO) {
        try {
            VolunteerShift registeredShift = volunteerService.registerVolunteer(shiftDTO);
            return ResponseEntity.ok(shiftDTO);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("The registration of the volunteer has failed.");
        }
    }

    @GetMapping("/user/{userId}/shifts")
    public List<VolunteerShift> getUserShifts(@PathVariable Long userId) {
        return volunteerService.getUserShifts(userId);
    }

    @PutMapping("/addHoursWorked/{volunteerShiftId}/{additionalHours}")
    public ResponseEntity<?> addHoursWorked(@PathVariable Long volunteerShiftId, @PathVariable int additionalHours){
        try{
            return ResponseEntity.ok(volunteerService.addHoursWorked(volunteerShiftId, additionalHours));
        }
        catch(EntityNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        catch(IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the worked hours.");
        }
    }
}