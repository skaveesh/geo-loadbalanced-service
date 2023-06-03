package com.skaveesh.geo.geoloadbalanced.service.rest;

import com.amazonaws.regions.Regions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HomeController {

    @Value("${com.skaveesh.geo.geoloadbalanced.service.region.this}")
    private String thisRegion;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getHome() {

        String homeView = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
                "\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n" +
                "\t<head>\n" +
                "\t\t<title>" +
                Regions.fromName(thisRegion).getDescription()
                + "</title>\n" +
                "\t</head>\n" +
                "\n" +
                "\t<body>\n" +
                "\t\t<h1>"+
                Regions.fromName(thisRegion).getDescription()
                +"</h1>\n" +
                "</body>\n" +
                "</html>";

        return new ResponseEntity<> (homeView, HttpStatus.OK);
    }
}
