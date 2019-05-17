package com.tddapps.rt;

import com.tddapps.rt.ioc.IocContainer;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication()
public class Program {
    public static void main(String[] args) {
        log.info("Starting");
        try {
            IocContainer.getInstance()
                    .resolve(ProgramStartup.class)
                    .run(args);
        } catch (Exception e) {
            log.error("Unhandled Exception", e);
        }
    }
}
