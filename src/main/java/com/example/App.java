package com.example;

import com.sun.istack.Nullable;

import java.time.LocalDate;
import java.util.Objects;

import static com.example.HibernateUtill.*;

public class App {
    public static void main(String[] args) {
        Person somePerson = new Person("Taras",
                "Shevchenko",
                "tarasShevchenko@gmail.com",
                "male",
                LocalDate.of(1890, 03, 9),
                "Ukraine");

        try {
            init("org.hibernate.tutorial.jpa");

            persistWithinPersistanceContext(somePerson);
            updateWithinPersistanceContext(101,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Ukraine");
            readById(5);
            readByEmail("%.com");
            System.out.println(removeAndGet(500).toString());
        } finally {
            close();
        }
    }

    private static Person removeAndGet(int id) {
        Person person = doAndReturnWithinPersistanceContext(entityManager -> entityManager.find(Person.class, id));
        doWithinPersistanceContext(entityManager -> {
            entityManager.merge(person);
            entityManager.remove(person);
        });
        return person;
    }

    private static void readByEmail(String email) {
        doWithinPersistanceContext(entityManager -> entityManager.createQuery(
                        "select a from Person a where a.email like :parameter", Person.class)
                .setParameter("parameter", email)
                .getResultStream()
                .forEach(System.out::println));
    }

    private static void readById(int id) {
        Person person = doAndReturnWithinPersistanceContext(entityManager -> entityManager.find(Person.class, id));
        System.out.println(person.toString());
    }


    private static void updateWithinPersistanceContext(int id,
                                                       @Nullable String fName,
                                                       @Nullable String lName,
                                                       @Nullable String email,
                                                       @Nullable String gender,
                                                       @Nullable LocalDate dateOfBirth,
                                                       @Nullable String country) {
        doWithinPersistanceContext(entityManager -> {
            Person person = entityManager.find(Person.class, id);
            if (!(Objects.equals(fName, null)))
                person.setfName(fName);

            if (!(Objects.equals(lName, null)))
                person.setlName(lName);

            if (!(Objects.equals(email, null)))
                person.setEmail(email);

            if (!(Objects.equals(gender, null)))
                person.setGender(gender);

            if (!(Objects.equals(dateOfBirth, null)))
                person.setDateOfBirth(dateOfBirth);

            if (!(Objects.equals(country, null)))
                person.setCountry(country);
        });
    }

    private static void persistWithinPersistanceContext(Person person) {
        HibernateUtill.doWithinPersistanceContext(entityManager -> entityManager.persist(person));
        System.out.println("person successfully persisted");
    }

}
