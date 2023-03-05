package pi.app.estatemarket.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity(name="UserApp")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserApp implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        private long userID;
        @Column(name = "username")
        private String username;
        @Column(name = "last_name")
        private String lastName;
        @Column(name = "first_name")
        private String firstName;
        @Column(name = "email_address")
        private String emailAddress;
        @Column(name = "phone_number")
        private String phoneNumber;
        @Column(name = "address")
        private String address;
        @Column(name = "profile_picture")
        private String profilePicture;
        @Column(name = "date_created")
        private Date dateCreated;
        @Column(name = "date_modified")
        private Date dateModified;
        @Column(name = "date_of_birth")
        private Date dateOfBirth;
        @Column(name = "password")
        private String password;
        @Enumerated(EnumType.STRING)
        @Column(name = "gender")
        private GenderType gender;
        @Column(name = "verification_code", length = 64)
        private String verificationCode;
        private boolean enabled;
        @Column(name = "reset_password_token")
        private String resetPasswordToken;
        private String secret;
        @ManyToOne
        @JoinColumn(name = "role_id")
        private Role role;
        @JsonIgnore
        @OneToMany (mappedBy = "userAppPub")
        private Set<Publication> publications;
        @JsonIgnore
        @OneToMany(mappedBy = "userAppMessage")
        private Set<Message> messagess;
        @JsonIgnore
        @OneToMany(mappedBy = "userAppComment")
        private Set<Comment> comments;
        @JsonIgnore
        @OneToMany(mappedBy = "userAppAgency")
        private Set<Agency> agencies;
        @JsonIgnore
        @OneToMany(mappedBy = "userAppContract")
        private Set<Contract> contracts;
        @JsonIgnore
        @OneToMany(mappedBy = "userAppAnnouncement")
        private Set<Announcement> announcements;
        @JsonIgnore
        @ManyToMany
        private Set<UserApp> appointments;




        @OneToMany(mappedBy = "userL")
        private List<Likee> likeList;



        @JsonBackReference
        public Role getRole() {
                return role;
        }
}

