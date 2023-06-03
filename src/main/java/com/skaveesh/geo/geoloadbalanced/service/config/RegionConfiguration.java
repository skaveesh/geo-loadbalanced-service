package com.skaveesh.geo.geoloadbalanced.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class RegionConfiguration {

    @Value("${com.skaveesh.geo.geoloadbalanced.service.region.all}")
    private String allRegions;

    @Value("${com.skaveesh.geo.geoloadbalanced.service.region.this}")
    private String thisRegion;

    private final Environment environment;

    public RegionConfiguration(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public List<String> getOtherRegionsUrls() {

        List<String> allRegionsList = new ArrayList<>(Arrays.asList(allRegions.split(",")));
        allRegionsList.remove(thisRegion);

        List<String> otherRegionUrlList = new ArrayList<>();

        for (String otherRegion : allRegionsList) {
            otherRegionUrlList.add(
                    environment.getProperty("com.skaveesh.geo.geoloadbalanced.service." + otherRegion + ".url"));
        }

        return otherRegionUrlList;
    }
}
