package org.springframework.hamill;

import org.springframework.hateoas.ResourceSupport;

public class Model extends ResourceSupport {

    private String name;

    public Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Model setName(String name) {
        this.name = name;
        return this;
    }
}
