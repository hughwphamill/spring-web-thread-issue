package org.springframework.hamill;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class TestController {

    public static final long LIMIT = 100;

    @RequestMapping("/responses")
    public ResponseEntity<List<Model>> responses() {
        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        long start = System.currentTimeMillis();
        List<String> names = Stream.iterate(1, i -> i++)
                .map(i -> "Response" + i)
                .limit(LIMIT)
                .collect(Collectors.toList());

        List<Model> models = names.stream()
                .map(n -> generateResponse(n))
                .collect(Collectors.toList());

        System.out.println("Elapsed: " + (System.currentTimeMillis() - start) + "ms." );
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    @RequestMapping("/responsesParallel")
    public ResponseEntity<List<Model>> responsesParallel() {
        long start = System.currentTimeMillis();
        List<String> names = Stream.iterate(1, i -> i++)
                .map(i -> "Response" + i)
                .limit(LIMIT)
                .collect(Collectors.toList());

        List<Model> models = names.parallelStream()
                .map(n -> generateResponse(n))
                .collect(Collectors.toList());

        System.out.println("Elapsed: " + (System.currentTimeMillis() - start) + "ms." );
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    @RequestMapping("/responses/{name}")
    public ResponseEntity<Model> response(@PathVariable("name") String name) {
        return new ResponseEntity<>(generateResponse(name), HttpStatus.OK);
    }

    private Model generateResponse(String name) {
        Model response = new Model(name);

        response.add(linkTo(methodOn(TestController.class).response(name)).withSelfRel());
        response.add(linkTo(methodOn(TestController.class).response("random")).withRel("random"));

        return response;
    }
}
