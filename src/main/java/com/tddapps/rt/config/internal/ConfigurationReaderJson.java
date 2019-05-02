package com.tddapps.rt.config.internal;

import com.google.gson.GsonBuilder;
import com.tddapps.rt.config.Configuration;
import com.tddapps.rt.config.ConfigurationReader;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Log4j2
class ConfigurationReaderJson implements ConfigurationReader {
    @Override
    public Configuration Read() {
        var result = ReadInternal();
        log.info(result);
        return result;
    }

    private Configuration ReadInternal() {
        try(var reader = CreateResourceReader("/config.json")){
            return ParseJson(reader);
        } catch (IOException e) {
            log.warn("Error Reading Configuration", e);
            return new Configuration();
        }
    }

    private Configuration ParseJson(InputStreamReader reader) {
        return new GsonBuilder()
                .create()
                .fromJson(reader, Configuration.class);
    }

    private InputStreamReader CreateResourceReader(String resourceName) {
        var inputStream = this.getClass().getResourceAsStream(resourceName);
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }
}
