package com.example.simplaws.controllers;

import com.example.simplaws.dao.HumanDao;
import com.example.simplaws.entities.Human;
import com.example.simplaws.entities.HumanDTO;
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
    private ApplicationContext context;

//    @Autowired
//    private HumanRepository humanRepository;

    @Autowired
    private HumanDao humanDao;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/humans")
    public ResponseEntity<List<Human>> getHumans() {
        return ResponseEntity.status(HttpStatus.OK).body(humanDao.findAll());
    }

    @GetMapping("/humans/{id}")
    public ResponseEntity<?> getHuman(@PathVariable("id") long id) {
        Optional<Human> humanOptional = humanDao.findById(id);
        if (humanOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(humanOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Human with id=" + id + "not found");
    }

    @PostMapping("/humans")
    public ResponseEntity<Human> saveHuman(@RequestBody HumanDTO humanDTO) {
        Human human = modelMapper.map(humanDTO, Human.class);
        Human humanCreated = humanDao.save(human);
        return ResponseEntity.status(HttpStatus.OK).body(humanCreated);
    }

    @DeleteMapping("/humans/{id}")
    public ResponseEntity<String> deleteHuman(@PathVariable("id") long id) {
        humanDao.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }

    @GetMapping("/stop")
    public void stop() {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        configurableApplicationContext.close();
        System.exit(0);
    }
}
