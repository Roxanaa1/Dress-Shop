package com.example.service;

import com.example.model.User;
import com.example.model.dtos.MonthlyUserCountDTO;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserChartsService {
    @Autowired
    private UserRepository userRepository;
    public List<Map<String, Object>> getUserRegistrationsByMonth(int year) {
        List<Object[]> results = userRepository.countUsersByMonth(year);

        Map<Integer, Integer> monthToCount = new HashMap<>();
        for (Object[] row : results) {
            int month = ((Number) row[0]).intValue();
            int count = ((Number) row[1]).intValue();
            monthToCount.put(month, count);
        }

        List<Map<String, Object>> completeData = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("month", i);
            map.put("count", monthToCount.getOrDefault(i, 0));
            completeData.add(map);
        }

        return completeData;
    }


    public Map<String, Long> getAgeDistribution() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(u -> u.getBirthDate() != null)
                .map(u -> {
                    int age = Period.between(u.getBirthDate(), LocalDate.now()).getYears();
                    if (age < 18) return "<18";
                    if (age < 25) return "18-24";
                    if (age < 35) return "25-34";
                    if (age < 45) return "35-44";
                    if (age < 60) return "45-59";
                    return "60+";
                })
                .collect(Collectors.groupingBy(group -> group, Collectors.counting()));
    }

    public Map<String, Long> getVerifiedStatusCount() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .collect(Collectors.groupingBy(
                        u -> u.getVerifiedAccount() != null && u.getVerifiedAccount() ? "Verified" : "Unverified",
                        Collectors.counting()
                ));
    }

    public Map<String, Long> getUsersByCounty() {
        return userRepository.findAll().stream()
                .filter(u -> u.getAddresses() != null && !u.getAddresses().isEmpty())
                .map(u -> u.getAddresses().get(0).getCounty())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
    }
}
