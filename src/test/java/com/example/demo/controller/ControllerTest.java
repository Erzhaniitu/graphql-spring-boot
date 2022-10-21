package com.example.demo.controller;

import com.example.demo.model.Level;
import com.example.demo.service.TestService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@GraphQlTest(TestController.class)
@Import(TestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @Autowired
    TestService testService;

    @Test
    @Order(1)
    void testFindAllShouldReturnAll() {
        String document = """
                query {
                    findAll {
                        id
                        name
                        level
                    }
                }            
                """;

        graphQlTester.document(document)
                .execute()
                .path("findAll")
                .entityList(com.example.demo.model.Test.class)
                .hasSize(3);
    }

    @Test
    @Order(2)
    void validShouldReturnTest() {
        String document = """
                query findOneTest($id: ID){
                    findOne(id: $id) {
                        id
                        name
                        level
                    }
                }            
                """;

        graphQlTester.document(document)
                .variable("id", 1)
                .execute()
                .path("findOne")
                .entity(com.example.demo.model.Test.class)
                .satisfies(test -> {
                    assertEquals("Test1", test.getName());
                    assertEquals(Level.MEDIUM, test.getLevel());
                });
    }

    @Test
    @Order(3)
    void invalidIdShouldReturnNull() {
        // language=GraphQL
        String document = """
        query findOneTest($id: ID){
            findOne(id: $id) {
                id
                name
                level
            }
        }            
        """;

        graphQlTester.document(document)
                .variable("id", 99)
                .execute()
                .path("findOne")
                .valueIsNull();


    }

    @Test
    @Order(4)
    void shouldCreateNewTest() {
        int currentTestCount = testService.findAll().size();

        // language=GraphQL
        String document = """
            mutation create($name: String, $level: Level) {
                create(name: $name, level: $level) {
                    id
                    name
                    level
                }
            }
        """;

        graphQlTester.document(document)
                .variable("name","Test4")
                .variable("size", Level.HIGH)
                .execute()
                .path("create")
                .entity(com.example.demo.model.Test.class)
                .satisfies(t -> {
                    assertNotNull(t.getId());
                    assertEquals("Tesstt",t.getName());
                    assertEquals(Level.MEDIUM,t.getLevel());
                });

        assertEquals(currentTestCount + 1,testService.findAll().size());
    }

    @Test
    @Order(5)
    void shouldUpdateExistingTest() {
        Test currentTest = (Test) testService.findOne(1).get();

        // language=GraphQL
        String document = """
            mutation update($id: ID, $name: String, $level: Level) {
                update(id: $id, name: $name, level: $level) {
                    id
                    name
                    level  
                }
            }
        """;

        graphQlTester.document(document)
                .variable("id", 1)
                .variable("name","UPDATED: Test3")
                .variable("level", Level.HIGH)
                .execute()
                .path("update")
                .entity(com.example.demo.model.Test.class);

        com.example.demo.model.Test updatedTest = testService.findOne(1).get();
        assertEquals("UPDATED: Test3",updatedTest.getName());
        assertEquals(Level.HIGH,updatedTest.getLevel());
    }

    @Test
    @Order(6)
    void shouldRemoveTestWithValidId() {
        int currentCount = testService.findAll().size();

        // language=GraphQL
        String document = """
            mutation delete($id: ID) {
                delete(id: $id) {
                    id
                    name
                    level
                }
            }
        """;

        graphQlTester.document(document)
                .variable("id", 1)
                .executeAndVerify();

        assertEquals(currentCount - 1, testService.findAll().size());
    }
}
