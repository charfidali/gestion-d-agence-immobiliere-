package pi.app.estatemarket.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;

import pi.app.estatemarket.Entities.Appointment;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AppointmentService implements IAppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    UserRepository userRepo;


    @Override
    public Appointment addAppointment(Appointment appointment) throws Exception {

        List<UserApp> listusers = userRepo.findAll();

        // Vérifier si la date de rendez-vous est bien renseignée
        if (appointment.getAppointmentDate() == null) {
            throw new Exception("La date de rendez-vous est obligatoire.");
        }
        if (appointment.getDate().before(new Date())) {
            throw new Exception("La date de rendez-vous doit être dans le futur.");
        }
        // Vérifier si la date de rendez-vous est dans le futur

        else {


            sendsms("+21629966022","votre appointement est le "+ appointment.getAppointmentDate());
            return appointmentRepository.save(appointment);
        }
    }


    public void sendsms(String str,String messagesms) {
        Twilio.init("AC8f7c763c17c52a3092a54ce299689286", "313e19a22bef5ab13af5bdb93273c739");
        try {
            com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message
                    .creator(new PhoneNumber(str), // To number
                            new PhoneNumber("+15674092533"), // From number
                            messagesms)
                    .create();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
    @Override
    public List<Appointment> getAppointment(){

        return appointmentRepository.findAll();
    }

    @Override
    public void deleteAppointment(Long id){

        appointmentRepository.deleteById(id);
    }

    @Override
    public Appointment updateAppointment(Appointment appointment,Long id) throws Exception {
        // Récupérer l'entité de rendez-vous existante
        Appointment a=appointmentRepository.findById(id)
                .orElseThrow(() -> new Exception("Rendez-vous introuvable."));

        // Vérifier si la date de rendez-vous est bien renseignée
        if (appointment.getAppointmentDate() == null) {
            throw new Exception("La date de rendez-vous est obligatoire.");
        }

        // Vérifier si la date de rendez-vous est dans le futur
        if (appointment.getAppointmentDate().before(new Date())) {
            throw new Exception("La date de rendez-vous doit être dans le futur.");
        }

        // Mettre à jour l'entité existante avec les nouvelles valeurs
        a.setAppointmentDate(appointment.getAppointmentDate());
        a.setAppointmentStatus(appointment.isAppointmentStatus());
        a.setAnnouncementApp(appointment.getAnnouncementApp());
        a.setUsers(appointment.getUsers());

        //     User u=shiftservice.findUserWithoutShiftOnDate(a.getAppointmentDate());
        a.getUsers().clear();
        //     a.getUsers().add(u);
        a.setAnnouncementApp(a.getAnnouncementApp());
        // Enregistrer l'entité mise à jour dans la base de données

        return appointmentRepository.save(a);



    }

    @Override
    public void affecterUserAppointment(Long IdAppointment, Long userID) {
        UserApp user= userRepo.findById(userID).orElse(null);
        Appointment appointment = appointmentRepository.findById(IdAppointment).orElse(null);
        //  user.getAppointments().add(appointment);
        appointment.getUsers().add(user);

        appointmentRepository.save(appointment);
    }

  /* @Override
    public List<Appointment> findAppointmentsByUsersAndDate(User user1, User user2, Date start, Date end) {
        return appointmentRepository.findAppointmentsByUsersAndDate(user1, user2, start, end);
    }*/

    // @Scheduled(fixedRate = 10000)
    public void RappelerSurRendezVous() {
        List<Appointment> listapp = appointmentRepository.findAll();
        Date currentSqlDate = new Date(System.currentTimeMillis());
        int a = currentSqlDate.getDate() -1;
        for (Appointment u : listapp) {

            if(u.getAppointmentDate().getYear()==currentSqlDate.getYear()&&
                    u.getAppointmentDate().getMonth()==currentSqlDate.getMonth()&&
                    u.getAppointmentDate().getDay()== u.getAppointmentDate().getDay()-1
            ) {
                sendsms("+21629966022", "il reste un jour pour rendez vous le "+u.getAppointmentDate());
            }
        }

    }//

    @Override
    public List<Date> getAvailableDatesFinal(Long userId,Long userId2) {
        UserApp user1 = userRepo.findById(userId).orElse(null);
        UserApp user2 = userRepo.findById(userId2).orElse(null);

        List<Appointment> listap1=new ArrayList<>() ;
        List<Appointment> listap2 =new ArrayList<>() ;

        for(Appointment ap1 : user1.getAppointments()) {
            if(ap1.isAppointmentStatus()) {
                listap1.add(ap1);
            }

        }

        for(Appointment ap2 : user2.getAppointments()) {
            if(ap2.isAppointmentStatus()) {
                listap2.add(ap2);
            }
        }

        List<Date> availableDates = new ArrayList<>();
        for (Appointment aa1 : listap1) {
            for (Appointment aa2 : listap2) {
                if(aa1.getAppointmentDate().getYear()==aa2.getAppointmentDate().getYear() &&aa1.getAppointmentDate().getMonth()==aa2.getAppointmentDate().getMonth()&&
                        aa1.getAppointmentDate().getDay()==aa2.getAppointmentDate().getDay())
                {
                    availableDates.add(aa1.getAppointmentDate());


                }
            }
        }


        return availableDates;

    }


    @Override
    public  List<Appointment> getAvailablesDates(long user1, long user2) {
        List<Appointment> user1Appointments = appointmentRepository.findByUsers_userID(user1);
        List<Appointment> user2Appointments = appointmentRepository.findByUsers_userID(user2);
        List<Appointment>  availableAppointments = new ArrayList<>();
        List<Appointment> user1AppointmentsAv = new ArrayList<>();
        List<Appointment> user2AppointmentsAv = new ArrayList<>();
        System.out.println(user2Appointments.size());
        for(Appointment app11 : user1Appointments) {
            if(!app11.isAppointmentStatus()){
                user1AppointmentsAv.add(app11);
            }
        }

        for(Appointment app22 : user2Appointments) {
            // System.out.println(app22.isAppointmentStatus());
            if(!app22.isAppointmentStatus()){
                user2AppointmentsAv.add(app22);
            }
        }

        for(Appointment app1 : user1AppointmentsAv){
            for(Appointment app2 : user2AppointmentsAv){

                if(app1.getAppointmentDate().compareTo(app2.getAppointmentDate()) == 0){
                    Appointment a1 = app1;
                    availableAppointments.add(a1);
                    // System.out.println(a1.getDate());
                }
            }
        }


        return availableAppointments.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public int getNombreRendezVousParuser(Long id) {
        return appointmentRepository.findByUsersUserID(id).size();
    }



    @Override
    public List<Object> getAppointmentStatistics() {
        List<Object> statistics = new ArrayList<>();

        // Nombre total de rendez-vous
        long totalAppointments = appointmentRepository.count();
        statistics.add(totalAppointments);

        // Nombre de rendez-vous pour chaque utilisateur
        List<Object[]> appointmentsPerUser = appointmentRepository.countAppointmentsPerUser();
        List<Object> appointmentsPerUserList = new ArrayList<>();
        for (Object[] result : appointmentsPerUser) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("user", result[0].toString());
            userMap.put("appointments", (Long) result[1]);
            appointmentsPerUserList.add(userMap);
        }
        statistics.add(appointmentsPerUserList);

        // Nombre de rendez-vous pour chaque annonce
        List<Object[]> appointmentsPerAnnouncement = appointmentRepository.countAppointmentsPerAnnouncement();
        List<Object> appointmentsPerAnnouncementList = new ArrayList<>();
        for (Object[] result : appointmentsPerAnnouncement) {
            Map<String, Object> announcementMap = new HashMap<>();
            announcementMap.put("announcement", result[0].toString());
            announcementMap.put("appointments", (Long) result[1]);
            appointmentsPerAnnouncementList.add(announcementMap);
        }
        statistics.add(appointmentsPerAnnouncementList);

        // Moyenne de rendez-vous par utilisateur
        double averageAppointmentsPerUser = appointmentRepository.averageAppointmentsPerUser();
        statistics.add(averageAppointmentsPerUser);

        return statistics;
    }


}