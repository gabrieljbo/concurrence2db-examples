package dev.gabrieljbo.concurrence2db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupCommandLineRunner implements CommandLineRunner {

    @Autowired
    private Action action;

    public void run(String... args) throws Exception {
	long executionInterval = Long.parseLong(args[0]);  // Milliseconds
	this.action.start(executionInterval);
    }

}