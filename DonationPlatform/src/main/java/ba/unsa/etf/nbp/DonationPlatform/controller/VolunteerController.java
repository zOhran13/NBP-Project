package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.dto.VolunteerShiftDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.VolunteerShift;
import ba.unsa.etf.nbp.DonationPlatform.security.JwtTokenHelper;
import ba.unsa.etf.nbp.DonationPlatform.service.VolunteerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {
    @Autowired
    private JwtTokenHelper tokenHelper;
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerVolunteer(@RequestHeader("Authorization") String authHeader, @RequestBody VolunteerShiftDTO shiftDTO) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String userEmail = tokenHelper.getClaimsFromToken(token).get("email", String.class);
            volunteerService.registerVolunteer(shiftDTO, userEmail);
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

    @GetMapping("/user/shifts")
    public ResponseEntity<?> getUserShifts(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String userEmail = tokenHelper.getClaimsFromToken(token).get("email", String.class);
            List<VolunteerShift> shifts = volunteerService.getUserShifts(userEmail);
            return ResponseEntity.ok(shifts);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching user shifts.");
        }
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

    @GetMapping("/count/{eventId}")
    public ResponseEntity<Long> getVolunteerCountForEvent(@PathVariable Long eventId) {
        try {
            long count = volunteerService.countVolunteersByEventId(eventId);
            return ResponseEntity.ok(count);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(0L);
        }
    }

    @PutMapping("/shiftTime/{volunteerShiftId}/{shiftStart}/{shiftEnd}")
    public ResponseEntity<?> editShiftTime(
            @RequestParam Long volunteerShiftId,
            @RequestParam String shiftStart,
            @RequestParam String shiftEnd){
        try{
            return ResponseEntity.ok(volunteerService.editShiftTime(volunteerShiftId, LocalTime.parse(shiftStart), LocalTime.parse(shiftEnd)));
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