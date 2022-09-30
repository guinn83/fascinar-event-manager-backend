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

        UserModel u1 = new UserModel("guinn83", encoder.encode("123456"), UserRole.ADMIN, false, true);
        UserModel u2 = new UserModel("vaninha.85", encoder.encode("123456"), UserRole.PLANNER, false, true);
        UserModel u3 = new UserModel("moniky", encoder.encode("123456"), UserRole.ASSISTANT, false, true);
        UserModel u4 = new UserModel("vanessa", encoder.encode("123456"), UserRole.ASSISTANT, false, true);
        UserModel u5 = new UserModel("michele", encoder.encode("123456"), UserRole.CUSTOMER, false, true);
        userRepository.saveAll(Arrays.asList(u1, u2, u3, u4));

        TeamRoles tr1 = new TeamRoles("Cerimonialista");
        TeamRoles tr2 = new TeamRoles("Coordenador");
        TeamRoles tr3 = new TeamRoles("Planner");
        TeamRoles tr4 = new TeamRoles("Assistente");
        teamRolesRepository.saveAll(Arrays.asList(tr1, tr2, tr3, tr4));

        TeamModel t1 = new TeamModel(null, "Vânia Alves", tr1, u2);
        TeamModel t2 = new TeamModel(null, "Wagner Alves", tr2, u1);
        TeamModel t3 = new TeamModel(null, "Monik", tr3, u3);
        TeamModel t4 = new TeamModel(null, "Vanessa", tr4, u4);
        teamRepository.saveAll(Arrays.asList(t1, t2, t3, t4));

        Set<EventTeamModel> te1 = new HashSet<>();
        te1.add(new EventTeamModel(t1.getId(), t1.getName(), tr1.getRole()));
        te1.add(new EventTeamModel(t2.getId(), t2.getName(), tr2.getRole()));
        te1.add(new EventTeamModel(t3.getId(), t3.getName(), tr3.getRole()));
        te1.add(new EventTeamModel(t4.getId(), t4.getName(), tr3.getRole()));
        teamEventoRepository.saveAll(te1);

        Set<EventTeamModel> te2 = new HashSet<>();
        te2.add(new EventTeamModel(t1.getId(), t2.getName(), tr1.getRole()));
        te2.add(new EventTeamModel(t1.getId(), t3.getName(), tr3.getRole()));
        te2.add(new EventTeamModel(t1.getId(), t4.getName(), tr4.getRole()));
        te2.add(new EventTeamModel("Maria José", "Banheirista"));

        teamEventoRepository.saveAll(te2);

        EventModel e1 = new EventModel(null, "Chris e Daniel", LocalDateTime.now(), EventStatus.A_REALIZAR, te1);
        EventModel e2 = new EventModel(null, "Thais e Gustavo", LocalDateTime.now(), EventStatus.REALIZADO, te2);
        EventModel e3 = new EventModel(null, "Karol e Carlos", LocalDateTime.now(), EventStatus.CANCELADO);
        EventModel e4 = new EventModel(null, "Priscila e Biro", LocalDateTime.now(), EventStatus.REALIZADO);
        eventRepository.saveAll(Arrays.asList(e1, e2, e3, e4));

    }
}
