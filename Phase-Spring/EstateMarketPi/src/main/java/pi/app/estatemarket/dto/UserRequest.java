package pi.app.estatemarket.dto;

import lombok.*;
import pi.app.estatemarket.Entities.GenderType;

import javax.persistence.Column;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private long userID;
    private String username;
    private String lastName;
    private String firstName;
    private String emailAddress;
    private String phoneNumber;
    private String address;
    private String profilePicture;
    private Date dateCreated;
    private Date dateModified;
    private Date dateOfBirth;
    private String password;
    private GenderType gender;
    private String verificationCode;
    private boolean enabled;
    private String resetPasswordToken;
    private String secret;
    private long role_id;
    private String role_name;
}