package com.mk.dts.api.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mk.dts.api.service.SchedulerManager;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

  private final SchedulerManager schedulerManager;

  public SchedulerController(SchedulerManager schedulerManager){
    this.schedulerManager = schedulerManager;
  }


  @PostMapping("/start")
  public ResponseEntity<?> startScheduler(){
    try{
      String schedulerId = schedulerManager.startScheduler();
      Map<String,String> response = new HashMap<>();
      response.put("schedulerId", schedulerId);
      return ResponseEntity.ok(response);
    }catch(Exception e){
      e.printStackTrace();
      return ResponseEntity.status(500).body("Failed to start scheduler");
    }
  }

  @GetMapping("/logs")
      public ResponseEntity<List<String>> listLogs() {
          return ResponseEntity.ok(schedulerManager.getAvailableLogFiles());
      }

      @GetMapping("/logs/{fileName}")
      public ResponseEntity<?> getLogContent(@PathVariable("fileName") String fileName) {
          try {
              List<String> logs = schedulerManager.readLogFile(fileName);
              return ResponseEntity.ok(logs);
          } catch (IllegalArgumentException e) {
              return ResponseEntity.badRequest().body("Security Warning: Invalid filename");
          } catch (IOException e) {
              return ResponseEntity.status(404).body("File not found");
          }
      }
    @PostMapping("/stop/{schedulerId}")
    public ResponseEntity<String> stopScheduler(@PathVariable("schedulerId") String schedulerId){
      try{
       schedulerManager.stopScheduler(schedulerId);
       return ResponseEntity.ok("Scheduler " + schedulerId + " stopped successfully.");
      }catch(Exception e){
        e.printStackTrace();
        return ResponseEntity.status(500).body("Failed to start scheduler");
      }

    }

}
