package com.test.postgre;

import antlr.collections.impl.IntRange;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.test.postgre.entity.Opportunity;
import com.test.postgre.entity.User;
import com.test.postgre.repository.OpportunityRepository;
import com.test.postgre.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("performance")
public class PerformanceController {

    private final OpportunityRepository opportunityRepository;
    private final UserRepository userRepository;

    @Autowired
    public PerformanceController(final OpportunityRepository opportunityRepository,
                                 final UserRepository userRepository) {
        this.opportunityRepository = opportunityRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("load")
    public void loadDatabase() {
        final List<Opportunity> opportunities = IntStream.rangeClosed(1, 1000)
                .mapToObj(i -> new Opportunity("code_" + i))
                .collect(Collectors.toList());
        opportunities.forEach(o -> o.setUsers(IntStream.rangeClosed(1, 100).mapToObj(i -> new User("username_"+i)).collect(Collectors.toList())));

        // insert every user
        final Set<User> allUsers = opportunities
                .stream()
                .map(Opportunity::getUsers)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        userRepository.saveAll(allUsers);

        opportunityRepository.saveAll(opportunities);


    }

}
