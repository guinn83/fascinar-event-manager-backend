package com.api.fascinareventos.models;

import com.api.fascinareventos.models.views.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tb_team_event")
public class EventTeam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView(View.Full.class)
    private Long id;
    @Transient
    @JsonView(View.Full.class)
    private Team team;
    @Column(name = "team_model_id")
    @JsonView(View.Full.class)
    private Long teamModelId;
    @Column(name = "team_model_name")
    @JsonView(View.Base.class)
    private String name;
    @Column(name = "event_role")
    @JsonView(View.Base.class)
    private String eventRole;

    @ManyToMany(mappedBy = "teamOfDay")
    @JsonIgnore //StackOverFlow error, without this...
    private Set<EventModel> eventModel;

    public EventTeam(String name) {
        this.name = name;
    }

    public EventTeam(String name, String eventRole) {
        this.name = name;
        this.eventRole = eventRole;
    }

    public EventTeam(Long teamModelId, String name, String eventRole) {
        this.teamModelId = teamModelId;
        this.name = name;
        this.eventRole = eventRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventTeam that = (EventTeam) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
