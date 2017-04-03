package io.dekstroza.github.jee7.swarmdemo.app.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "registrations")
@XmlRootElement
public class RegistrationInfo implements Serializable {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private int userId;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, unique = false, length = 16)
    private String password;

    @Column(nullable = false, unique = false, length = 255)
    private String authToken;

    @Temporal(TemporalType.TIMESTAMP)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Date creationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }


}
