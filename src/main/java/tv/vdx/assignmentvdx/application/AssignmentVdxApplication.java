package tv.vdx.assignmentvdx.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tv.vdx.assignmentvdx.cache.impl.TimedCache;

@SpringBootApplication
public class AssignmentVdxApplication {

	public static void main(String[] args) {

		SpringApplication.run(AssignmentVdxApplication.class, args);
	}

}
