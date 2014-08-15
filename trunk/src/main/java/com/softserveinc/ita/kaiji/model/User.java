package com.softserveinc.ita.kaiji.model;

import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.model.util.Identifiable;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents basic User that can be introduced to system
 *
 * @author Ievgen Sukhov
 * @author Paziy Evgeniy
 * @version 3.4
 * @since 30.03.14
 */
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Identifiable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Column(name = "nickname", nullable = false, unique = true, length = 30)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true, length = 40)
    private String email;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_date", nullable = true)
    private Date registrationDate = new Date();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")})
    @Column(name = "role", nullable = false,
            columnDefinition = "enum('USER_ROLE', 'ADMIN_ROLE')")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = getDefaultRoles();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "link_game_to_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "game_id")})
    private Set<GameInfoEntity> gameInfoEntities;


    public User() {}

    public User(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    /**
     * Returns real user name under which current user is registered in system
     *
     * @return {@link java.lang.String} user name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of user.
     *
     * @param name {@link java.lang.String} new user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns user's nickname (login)
     *
     * @return {@link java.lang.String} user's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets user's nickname (login)
     *
     * @param nickname {@link java.lang.String} new user's nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Returns encrypted user's password
     *
     * @return encrypted user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets new encrypted password for user
     *
     * @param password encrypted password for user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns date of user's registration
     *
     * @return {@link java.util.Date} date of user's registration
     */
    public Date getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets user registration date.
     *
     * @param registrationDate {@link java.util.Date} date of user's registration
     */
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Returns user's email
     *
     * @return {@link java.lang.String} user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets user's mail.
     *
     * @param email {@link java.lang.String} new user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }

    public static Set<UserRole> getDefaultRoles() {
        Set<UserRole> defaultRoles = new HashSet<>();
        defaultRoles.add(UserRole.USER_ROLE);
        return defaultRoles;
    }

    public Set<GameInfoEntity> getGameInfoEntities() {
        return gameInfoEntities;
    }

    public void setGameInfoEntities(Set<GameInfoEntity> gameInfoEntities) {
        this.gameInfoEntities = gameInfoEntities;
    }
}
