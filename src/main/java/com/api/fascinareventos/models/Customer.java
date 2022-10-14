package com.api.fascinareventos.models;

import com.api.fascinareventos.models.views.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonView(View.Full.class)
    private Long id;
    @Column(name = "full_name", nullable = false)
    @JsonView(View.Base.class)
    private String fullName;
    @Column(name = "social_name", nullable = false)
    @JsonView(View.Base.class)
    private String socialName;
    @Column(name = "phone_number", nullable = false)
    @JsonView(View.Base.class)
    private Integer phoneNumber;
    @Column(name = "email")
    @JsonView(View.Base.class)
    private String email;

    @JsonView(View.Full.class)
    @OneToMany(mappedBy = "customer")
    private Set<EventModel> eventModels = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer that = (Customer) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
