package com.mk.dts.api.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mk.dts.common.dao.WorkerMetadataDao;
import com.mk.dts.common.model.WorkerMetaData;

@Service
public class WorkerManager {
  private final WorkerMetadataDao metadataDao;
  private static final String LOG_DIR_PATH = "../worker/logs";

  public WorkerManager(WorkerMetadataDao metadataDao){
    this.metadataDao = metadataDao;
  }

  public synchronized String startWorker() throws IOException,SQLException{
    if(getActiveWorkerCount() >= 5){
      System.out.println("[API] Max Worker limit (5) reached. Cannot start new");
      throw new RuntimeException("Max Worker limit reached");
    }

    // dynamic logging file
    String uniqueId = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4);

    String logFileName = "worker-" + uniqueId + ".log";
    File logdir = new File(LOG_DIR_PATH);
    if(!logdir.exists()) logdir.mkdirs();

    File logFile = new File(logdir,logFileName);
    String workerId = "Worker-" + uniqueId;


    String mvnArgs = String.format("-Dexec.args=%s, %s",logFileName, workerId);

    //launching in detatched mode
    ProcessBuilder pb = new ProcessBuilder("setsid","mvn","exec:java","-Dexec.mainClass=com.mk.dts.worker.WorkerMain" , "-Dexec.args=" + mvnArgs);

    pb.directory(new File("../worker"));

    //outputing og logfile
    pb.redirectOutput(logFile);
    pb.redirectError(logFile);

     pb.start();

      System.out.println("[API] Scheduler process spawned in background. Logs: " + logFile.getAbsolutePath());

      return workerId;
}

private int getActiveWorkerCount() throws SQLException{
  List<WorkerMetaData> workers = metadataDao.getAllWorkers();
  int count = 0;
  for (WorkerMetaData node : workers){
    //checking if the status is ACTIVE and the heartbeat is fresh
    if("ACTIVE".equals(node.getStatus())){
      long timeDiff = Instant.now().getEpochSecond() - node.getLastHeartbeat().toInstant().getEpochSecond();
      if(timeDiff < 45){
        count ++;
      }
    }
  }
  return count;
}

public List<String> getAvailableLogFiles(){
  File folder = new File(LOG_DIR_PATH);
  if(!folder.exists() || !folder.isDirectory()){
    return List.of();
  }

  File[] files = folder.listFiles((dir, name) -> name.endsWith(".log"));
  if(files == null) return List.of();

  return Arrays.stream(files)
  .sorted((f1,f2) -> Long.compare(f2.lastModified(), f1.lastModified()))
  .map(File::getName)
  .collect(Collectors.toList());
}

public List<String> readLogFile(String fileName) throws IOException{
  if(fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")){
    throw new IllegalArgumentException("Invalid file name");
  }

  File file = new File(LOG_DIR_PATH, fileName);
  if(!file.exists()) throw new IOException("Log file not found: " + fileName);

List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        int start = Math.max(0, lines.size() - 500);
                return lines.subList(start, lines.size());
}

public void stopWorker(String workerId) throws  SQLException{
    // 1. Get the metadata from DB to find the PID
    // (Assuming you have access to the DAO here)
    WorkerMetaData meta  = metadataDao.findById(workerId);

    if (meta == null) {
        throw new RuntimeException("Scheduler not found with ID: " + workerId);
    }

    long pid = meta.getProcessId();

    // 2. Execute the OS kill command
    try {
        System.out.println("[API] Stopping Scheduler " + workerId + " (PID: " + pid + ")");

        // "kill <pid>" sends SIGTERM (polite shutdown)
        // "kill -9 <pid>" sends SIGKILL (force shutdown)
        Process killer = new ProcessBuilder("kill", String.valueOf(pid)).start();

        int exitCode = killer.waitFor();

        if (exitCode == 0) {
            // 3. Update DB status immediately so UI updates
            metadataDao.updateStatus(meta.getWorkerId(), "STOPPED");
        } else {
            throw new RuntimeException("Failed to kill process. Exit code: " + exitCode);
        }

    } catch (SQLException |IOException | InterruptedException e) {
        throw new RuntimeException("Error stopping scheduler", e);
    }
}


}
