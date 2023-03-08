package pi.app.estatemarket.Services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pi.app.estatemarket.Entities.GenderType;

import java.util.Date;

@Service
public class SessionService {
     public static long userID;
     public static String username;
     public static String lastName;
     public static String password;


     public static String firstName;
     public static String emailAddress;
     public static long role_id;
     public static String role_name;

     public static long getUserID() {
          return userID;
     }

     public static void setUserID(long userID) {
          SessionService.userID = userID;
     }

     public static String getUsername() {
          return username;
     }

     public static void setUsername(String username) {
          SessionService.username = username;
     }

     public static String getLastName() {
          return lastName;
     }

     public static void setLastName(String lastName) {
          SessionService.lastName = lastName;
     }

     public static String getFirstName() {
          return firstName;
     }

     public static void setFirstName(String firstName) {
          SessionService.firstName = firstName;
     }

     public static String getEmailAddress() {
          return emailAddress;
     }

     public static void setEmailAddress(String emailAddress) {
          SessionService.emailAddress = emailAddress;
     }

     public static long getRole_id() {
          return role_id;
     }

     public static void setRole_id(long role_id) {
          SessionService.role_id = role_id;
     }

     public static String getRole_name() {
          return role_name;
     }

     public static void setRole_name(String role_name) {
          SessionService.role_name = role_name;
     }
     public static String getPassword() {
          return password;
     }

     public static void setPassword(String password) {
          SessionService.password = password;
     }

}