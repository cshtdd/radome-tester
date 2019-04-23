package com.tddapps.rt;

import com.tddapps.rt.ioc.IocContainer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class Program {
    public static void main(String[] args){
        IocContainer.getInstance()
                .Resolve(ProgramStartup.class)
                .Run(args);
    }
}
