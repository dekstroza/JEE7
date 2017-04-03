package io.dekstroza.github.jee7.swarmdemo.app.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "users")
@XmlRootElement
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 16, nullable = false)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Date creationDate;

    public User() {
    }

    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final User user = (User) o;

        if (getId() != user.getId())
            return false;
        if (!getEmail().equals(user.getEmail()))
            return false;
        if (!getPassword().equals(user.getPassword()))
            return false;
        return getCreationDate().equals(user.getCreationDate());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + getCreationDate().hashCode();
        return result;
    }
}
