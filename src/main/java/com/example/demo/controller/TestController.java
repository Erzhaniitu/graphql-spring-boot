package com.example.demo.controller;

import com.example.demo.model.Level;
import com.example.demo.model.Test;
import com.example.demo.service.TestService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService=testService;
    }

    @QueryMapping
    public List<Test> findAll() {
        return testService.findAll();
    }

    @QueryMapping
    public Optional<Test> findOne(@Argument Integer id) {
        return testService.findOne(id);
    }

    @MutationMapping
    public Test create(@Argument String name, @Argument Level level) {
        return testService.create(name,level);
    }

    @MutationMapping
    public Test update(@Argument Integer id, @Argument String name, @Argument Level level) {
        return testService.update(id,name,level);
    }

    @MutationMapping
    public Test delete(@Argument Integer id) {
        return testService.delete(id);
    }
}
