package com.mckinleyit.microapp.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(path = "/microapp-api")
public class MicroAppController {

    @RequestMapping(value = "/log/{level}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity produceLog(@PathVariable String level) {

        String otherValue = RandomStringUtils.random(3, 'a', 'b', 'c', 'x', 'y', 'z');

        switch (level.toLowerCase()) {
            case "trace":
                log.trace("Produced a trace. log={}, someText={}", level, otherValue);
                break;
            case "debug":
                log.debug("Produced a debug. log={}, someText={}", level, otherValue);
                break;
            case "info":
                log.info("Produced a info. log={}, someText={}", level, otherValue);
                break;
            case "warn":
                log.warn("Produced a warn. log={}, someText={}", level, otherValue);
                break;
            default:
                log.error("Produced a error. log={}, someText={}", level, otherValue);
                break;
        }
        String responseText = String.format("{  \"response\" : \"Log generated at:%s \" }", LocalDateTime.now());
        return new ResponseEntity<>(responseText, HttpStatus.OK);
    }
}
