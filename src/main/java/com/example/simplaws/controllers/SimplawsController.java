package com.example.simplaws.controllers;

import com.example.simplaws.entities.Human;
import com.example.simplaws.entities.HumanDTO;
import com.example.simplaws.repositories.HumanRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class SimplawsController {

    @Autowired
    ApplicationContext context;

    @Autowired
    HumanRepository humanRepository;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/humans")
    public ResponseEntity<List<Human>> getHumans() {
        return ResponseEntity.status(HttpStatus.OK).body(humanRepository.findAll());
    }

    @GetMapping("/humans/{id}")
    public ResponseEntity<Human> getHuman(@PathVariable("id") long id)  {
        Optional<Human> humanOptional = humanRepository.findById(id);
        if (humanOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(humanOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/humans")
    public ResponseEntity<Human> createHuman(@RequestBody HumanDTO humanDTO) {
        Human human = modelMapper.map(humanDTO, Human.class);
        System.out.println(human);
        Human humanCreated = humanRepository.save(human);
        return ResponseEntity.status(HttpStatus.OK).body(humanCreated);
    }

    @DeleteMapping("/humans/{id}")
    public ResponseEntity<String> deleteHuman(@PathVariable("id") long id) {
        humanRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }

    @GetMapping("/stop")
    public void stop() {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        configurableApplicationContext.close();
    }
}
