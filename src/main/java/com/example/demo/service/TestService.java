package com.example.demo.service;

import com.example.demo.model.Level;
import com.example.demo.model.Test;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TestService {

    private List<Test> tests = new ArrayList<>();
    AtomicInteger id = new AtomicInteger(0);


    public List<Test> findAll(){
        return tests;
    }

    public Optional<Test> findOne(Integer id) {
        return tests.stream().filter(coffee -> coffee.getId() == id).findFirst();
    }

    public Test create(String name, Level level) {
        Test test = new Test(id.incrementAndGet(), name, level);
        tests.add(test);
        return test;
    }

    public Test update(Integer id, String name, Level level) {
        Test updatedCoffee = new Test(id, name, level);
        Optional<Test> optional = tests.stream().filter(c -> c.getId() == id).findFirst();
        if (optional.isPresent()) {
            Test coffee = optional.get();
            int index = tests.indexOf(coffee);
            tests.set(index, updatedCoffee);
        } else {
            throw new IllegalArgumentException("Invalid data");
        }
        return updatedCoffee;
    }

    public Test delete(Integer id) {
        Test coffee = tests.stream().filter(c -> c.getId() == id)
                .findFirst().orElseThrow(() -> new IllegalArgumentException());
        tests.remove(coffee);
        return coffee;
    }

    @PostConstruct
    private void init() {
        tests.add(new Test(id.incrementAndGet(), "Test1", Level.LOW));
        tests.add(new Test(id.incrementAndGet(), "Test2", Level.MEDIUM));
        tests.add(new Test(id.incrementAndGet(), "Test3", Level.HIGH));
    }
}
