package pi.app.estatemarket.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.app.estatemarket.Entities.Appointment;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Repository.UserRepository;
import pi.app.estatemarket.Services.IAppointmentService;
import pi.app.estatemarket.Services.IUserService;
import pi.app.estatemarket.Services.UserServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class AppointmentController {

    @Autowired
    IAppointmentService iAppointmentService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/addAppointment")
    public Appointment addAppointment(@RequestBody Appointment appointment) throws Exception{

        return  iAppointmentService.addAppointment(appointment);

    }
    @GetMapping("/afficherAppointments")
    public List<Appointment> getAppointment() {
        return iAppointmentService.getAppointment();
    }
    @DeleteMapping("/supAppointment/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        iAppointmentService.deleteAppointment(id);
    }
    @PutMapping("/updateAppointment/{id}")
    public Appointment updateAppointment(@RequestBody Appointment appointment,@PathVariable Long id) throws Exception{
        return iAppointmentService.updateAppointment(appointment,id);
    }

    @PutMapping("/affecterUserAppointment/{IdAppointment}/{user_id}")
    public void affecterUserAppointment(@PathVariable Long IdAppointment, @PathVariable Long user_id){
        iAppointmentService.affecterUserAppointment(IdAppointment, user_id);
    }


    @GetMapping("/available-dates/{userId}/{userI2}")
    public ResponseEntity<?> getAvailableDatesFinally(@PathVariable Long userId,@PathVariable Long userId2) {
        List<Date> availableDates = iAppointmentService.getAvailableDatesFinal(userId,userId2);
        return ResponseEntity.ok(availableDates);
    }

  /* @GetMapping("/checkAvailability")
    public ResponseEntity<List<Appointment>> checkAvailability(@RequestParam User user1, @RequestParam User user2, @RequestParam Date start, @RequestParam Date end) {
       List<Appointment> appointments  = iAppointmentService.checkAvailability(user1, user2, start, end);
        return ResponseEntity.ok().body(appointments);
    }*/

    @GetMapping("/checkAvailability/{userId1}/{userId2}")
    public ResponseEntity<List<Appointment>> getAvailablesDates(@PathVariable ("userId1") long user1, @PathVariable("userId2")  long user2) {
        List<Appointment> appointments  = iAppointmentService.getAvailablesDates(user1, user2);
        return ResponseEntity.ok().body(appointments);
    }

    @GetMapping("/RDV/{userId1}/{userId2}")
    public ResponseEntity<Date> rdv(@PathVariable ("userId1") long user1, @PathVariable("userId2")  long user2) {
        List<Appointment> appointments  = iAppointmentService.getAvailablesDates(user1, user2);
        return ResponseEntity.ok().body(appointments.get(0).getAppointmentDate());
    }

/*    @PostMapping("/addAppointmentEtAffecterAUser/{userID}")
    public Appointment addAppointmentEtAffecterAUser(@RequestBody Appointment appointment, @PathVariable long userID) throws Exception{
       return iAppointmentService.addAppointmentEtAffecterAUser(appointment,userID);
    }*/



    @GetMapping("/nombrederendezvous/{id}")
    public int getTrendsByDayOfWeek(@PathVariable Long id) {
        return iAppointmentService.getNombreRendezVousParuser(id);
    }

    @GetMapping("/appointmentStatistics")
    public List<Object> getAppointmentStatistics() {
        return iAppointmentService.getAppointmentStatistics();
    }

}