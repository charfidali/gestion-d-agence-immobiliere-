package pi.app.estatemarket.Repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pi.app.estatemarket.Entities.Appointment;
import pi.app.estatemarket.Entities.UserApp;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    List<Appointment> findByUsers_userID(Long userId);

    List<Appointment> findByUsersInAndDateBetween(List<UserApp> users, Date start, Date end);


    List<Appointment> findByUsersUserID(Long id);

 //  @Query("SELECT u.firstName, count(a) FROM appointment a JOIN a.users u GROUP BY u")
  //  List<Object[]> countAppointmentsPerUser();

  // @Query("SELECT ann.title, count(a) FROM appointment a JOIN a.announcementApp ann GROUP BY ann")
  // List<Object[]> countAppointmentsPerAnnouncement();

    @Query("SELECT avg(a.users.size) FROM appointment a")
    double averageAppointmentsPerUser();

}