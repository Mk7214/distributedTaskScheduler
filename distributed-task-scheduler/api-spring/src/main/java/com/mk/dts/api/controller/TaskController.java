package com.mk.dts.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.dts.api.dto.TaskCrateRequest;
import com.mk.dts.api.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  private final TaskService service;

  public TaskController(TaskService service ){
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<?> submit(@Valid   @RequestBody TaskCrateRequest req){

    // validation
    if(req.getType() == null || req.getType().isBlank()){
      return ResponseEntity.badRequest().body("Task type is required");
    }
    if(req.getPayload() == null ){
      return ResponseEntity.badRequest().body("Payload must not be empty");
    }

    try{
      ObjectMapper mapper = new ObjectMapper();
      String payload = mapper.writeValueAsString(req.getPayload());
      service.createTask(req.getType(),payload, req.getPriority());
      return ResponseEntity.ok("Task Created Successfully");
    }catch(Exception e){
      e.printStackTrace();
      return ResponseEntity.internalServerError().body("Failed to create task");
    }

  }
  @GetMapping
  public ResponseEntity<?> getTasks(@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "size",defaultValue = "10") int size){
      if(size > 100) size = 100;
    try{
      return ResponseEntity.ok(service.getCompletedTasks(page,size));
    }catch(Exception e){
      e.printStackTrace();
      return ResponseEntity.internalServerError().body("Failed to fetch completed tasks");
    }
  }

  //TODO: need to handle empty tasks;
  @GetMapping("/completed")
  public ResponseEntity<?> getCompletedTasks(@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "size",defaultValue = "10") int size){
      if(size > 100) size = 100;
    try{
      return ResponseEntity.ok(service.getCompletedTasks(page,size));
    }catch(Exception e){
      e.printStackTrace();
      return ResponseEntity.internalServerError().body("Failed to fetch completed tasks");
    }
  }

  //TODO: need to handle empty dead tasks;
  @GetMapping("/deadTasks")
  public ResponseEntity<?> getDeadTasks(@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "size",defaultValue = "10") int size){
      if(size > 100) size = 100;
    try{
    return ResponseEntity.ok(service.getDeadTasks(page, size));
    }catch(Exception e){
          e.printStackTrace();
          return ResponseEntity.internalServerError().body("Failed to fetch dead tasks");
    }

  }
}
