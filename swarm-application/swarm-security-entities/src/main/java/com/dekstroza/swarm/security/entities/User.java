package com.dekstroza.swarm.security.entities;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Table(schema = "security", name = "users")
@NamedQueries({ @NamedQuery(name = User.FIND_BY_USERNAME_QUERY, query = "SELECT u FROM User u WHERE u.username = :email") })
@XmlRootElement
@Entity
public class User implements Serializable {

    public static final String FIND_BY_USERNAME_QUERY = "FindByUsername";
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(schema = "security", catalog = "security", name = "seq_user_id", sequenceName = "seq_user_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_user_id", strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "email", length = 50, nullable = false)
    private String username;
    @Column(name = "firstname", length = 20, nullable = false)
    private String firstname;
    @Column(name = "lastname", length = 20, nullable = false)
    private String lastname;
    @Column(name = "passwd", length = 20, nullable = false)
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return username;
    }

    public void setEmail(final String email) {
        this.username = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final User user = (User) other;
        return id.equals(user.id);
    }
}
