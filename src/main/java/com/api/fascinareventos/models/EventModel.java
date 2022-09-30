package com.api.fascinareventos.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_event")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private URL avatar;
    @Column(name="Name", nullable = false, length = 100)
    private String name;
    @Column(name="event_Date")
    private LocalDateTime eventDate;
    private EventStatus status;

    @OneToOne
    @JsonIgnore
    private Bill bill;

    @ManyToOne
    private Customer customer;

    @OneToMany
    @JoinTable(
            name = "tb_event_team_join",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = {@JoinColumn(name = "team_event_id", referencedColumnName = "id")})
    private Set<EventTeam> teamOfDay = new HashSet<>();

    public EventModel(URL avatar, String name, LocalDateTime eventDate, EventStatus status) {
        this.avatar = avatar;
        this.name = name;
        this.eventDate = eventDate;
        this.status = status;
    }

    public EventModel(URL avatar, String name, LocalDateTime eventDate, EventStatus status, Set<EventTeam> teamOfDay ) {
        this.avatar = avatar;
        this.name = name;
        this.eventDate = eventDate;
        this.status = status;
        this.teamOfDay = teamOfDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventModel that = (EventModel) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "avatar = " + avatar + ", " +
                "name = " + name + ", " +
                "eventDate = " + eventDate + ", " +
                "status = " + status + ")";
    }
}
