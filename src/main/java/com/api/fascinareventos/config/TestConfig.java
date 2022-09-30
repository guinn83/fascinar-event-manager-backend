package com.api.fascinareventos.config;

import com.api.fascinareventos.models.*;
import com.api.fascinareventos.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
@Profile("test")
@AllArgsConstructor
public class TestConfig implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private JwtProperties jwtProperties;
    private TeamRolesRepository teamRolesRepository;
    private TeamRepository teamRepository;
    private EventRepository eventRepository;
    private EventTeamRepository teamEventoRepository;

    @Override
    public void run(String... args) throws Exception {

        User u1 = new User("guinn83", encoder.encode("123456"), UserRole.ADMIN, false, true);
        User u2 = new User("vaninha.85", encoder.encode("123456"), UserRole.PLANNER, false, true);
        User u3 = new User("moniky", encoder.encode("123456"), UserRole.ASSISTANT, false, true);
        User u4 = new User("vanessa", encoder.encode("123456"), UserRole.ASSISTANT, false, true);
        User u5 = new User("michele", encoder.encode("123456"), UserRole.CUSTOMER, false, true);
        userRepository.saveAll(Arrays.asList(u1, u2, u3, u4));

        TeamRoles tr1 = new TeamRoles("Cerimonialista");
        TeamRoles tr2 = new TeamRoles("Coordenador");
        TeamRoles tr3 = new TeamRoles("Planner");
        TeamRoles tr4 = new TeamRoles("Assistente");
        teamRolesRepository.saveAll(Arrays.asList(tr1, tr2, tr3, tr4));

        Team t1 = new Team(null, "Vânia Alves", tr1, u2);
        Team t2 = new Team(null, "Wagner Alves", tr2, u1);
        Team t3 = new Team(null, "Monik", tr3, u3);
        Team t4 = new Team(null, "Vanessa", tr4, u4);
        teamRepository.saveAll(Arrays.asList(t1, t2, t3, t4));

        Set<EventTeam> te1 = new HashSet<>();
        te1.add(new EventTeam(t1.getId(), t1.getName(), tr1.getRole()));
        te1.add(new EventTeam(t2.getId(), t2.getName(), tr2.getRole()));
        te1.add(new EventTeam(t3.getId(), t3.getName(), tr3.getRole()));
        te1.add(new EventTeam(t4.getId(), t4.getName(), tr3.getRole()));
        teamEventoRepository.saveAll(te1);

        Set<EventTeam> te2 = new HashSet<>();
        te2.add(new EventTeam(t1.getId(), t2.getName(), tr1.getRole()));
        te2.add(new EventTeam(t1.getId(), t3.getName(), tr3.getRole()));
        te2.add(new EventTeam(t1.getId(), t4.getName(), tr4.getRole()));
        te2.add(new EventTeam("Maria José", "Banheirista"));

        teamEventoRepository.saveAll(te2);

        EventModel e1 = new EventModel(null, "Chris e Daniel", LocalDateTime.now(), EventStatus.A_REALIZAR, te1);
        EventModel e2 = new EventModel(null, "Thais e Gustavo", LocalDateTime.now(), EventStatus.REALIZADO, te2);
        EventModel e3 = new EventModel(null, "Karol e Carlos", LocalDateTime.now(), EventStatus.CANCELADO);
        EventModel e4 = new EventModel(null, "Priscila e Biro", LocalDateTime.now(), EventStatus.REALIZADO);
        eventRepository.saveAll(Arrays.asList(e1, e2, e3, e4));

    }
}
