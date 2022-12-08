package com.example.newclientservice.app.service;


import com.example.newclientservice.app.connector.FeignConnector;
import com.example.newclientservice.app.controller.NewClientController;
import com.example.newclientservice.app.model.Book;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class NewClientService {

    private final FeignConnector feignConnector;

    private final RestTemplate restTemplate;

    private final Logger LOG = Logger.getLogger(NewClientController.class.getName());

    public NewClientService(FeignConnector feignConnector, RestTemplate restTemplate) {
        this.feignConnector = feignConnector;
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "failed")
    public List<Book> getAllBooksList() {
        return feignConnector.getAllBooksList();
    }

    public List<Book> failed() {
        List<Book> error = new ArrayList<>();
        error.add(Book.builder().title("Service is not available now. Please try again later").build());
        return error;
    }
    @HystrixCommand
    public String data() {
        String response = restTemplate.getForObject("http://localhost:8081/data", String.class);
        LOG.log(Level.INFO, response);
        return response;
    }



}