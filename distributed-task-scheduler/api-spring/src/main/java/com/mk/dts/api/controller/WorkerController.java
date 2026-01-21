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

import com.mk.dts.api.service.WorkerManager;

@RestController
@RequestMapping("/worker")
public class WorkerController {
  private final WorkerManager  workerManager;

  public WorkerController(WorkerManager workerManager){
    this.workerManager = workerManager;
  }

  @PostMapping("/start")
  public ResponseEntity<?> startWorker(){
    try{
      String workerId = workerManager.startWorker();
            Map<String,String> response = new HashMap<>();
            response.put("workerId", workerId);
            return ResponseEntity.ok(response);
    }catch(Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Failed to start scheduler");
            }
  }

  @GetMapping("/logs")
      public ResponseEntity<List<String>> listLogs() {
          return ResponseEntity.ok(workerManager.getAvailableLogFiles());
      }

      @GetMapping("/logs/{fileName}")
      public ResponseEntity<?> getLogContent(@PathVariable("fileName") String fileName) {
          try {
              List<String> logs = workerManager.readLogFile(fileName);
              return ResponseEntity.ok(logs);
          } catch (IllegalArgumentException e) {
              return ResponseEntity.badRequest().body("Security Warning: Invalid filename");
          } catch (IOException e) {
              return ResponseEntity.status(404).body("File not found");
          }
      }

      @PostMapping("/stop/{workerId}")
      public ResponseEntity<String> stopScheduler(@PathVariable("workerId") String workerId){
        try{
         workerManager.stopWorker(workerId);
         return ResponseEntity.ok("workerr " + workerId + " stopped successfully.");
        }catch(Exception e){
          e.printStackTrace();
          return ResponseEntity.status(500).body("Failed to start scheduler");
        }

}
}
