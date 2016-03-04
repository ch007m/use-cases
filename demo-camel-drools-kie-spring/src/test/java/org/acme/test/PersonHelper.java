package org.acme.test;

import org.acme.model.Person;
import org.apache.camel.Body;

import java.util.concurrent.ThreadLocalRandom;

public class PersonHelper {

    int Low = 15;
    int High = 75;

    public Person generate(@Body String name) {
        Person p = new Person();
        p.setName(name);
        p.setAge(ThreadLocalRandom.current().nextInt(High - Low) + Low);

        return p;
    }
}
