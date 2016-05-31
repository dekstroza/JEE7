package io.dekstroza.github.jee7.swarmdemo.app.api;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "REST_DB_ACCESS")
@XmlRootElement
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 40)
    private String firstname;

    @Column(length = 40)
    private String lastname;

    @Column(length = 40)
    private String username;

    @Column(length = 40)
    private String password;

    /**
     * Default constructor
     */
    public ApplicationUser() {
    }

    /**
     * Full arg constructor
     * 
     * @param firstname
     *            Firstname, max length 40 characters
     * @param lastname
     *            Lastname, max length 40 characters
     * @param username
     *            Username, max length 40 characters
     * @param password
     *            Password, max length 40 characters
     */
    public ApplicationUser(final String firstname, final String lastname, final String username, final String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final ApplicationUser that = (ApplicationUser) o;

        if (id != that.id)
            return false;
        if (!firstname.equals(that.firstname))
            return false;
        if (!lastname.equals(that.lastname))
            return false;
        if (!username.equals(that.username))
            return false;
        return password.equals(that.password);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + firstname.hashCode();
        result = 31 * result + lastname.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
