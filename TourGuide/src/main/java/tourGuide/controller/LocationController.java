package tourGuide.controller;

import org.springframework.web.bind.annotation.RestController;
import tourGuide.service.TrackerService;

@RestController
public class LocationController {

    private final TrackerService trackerService;

    public LocationController(TrackerService trackerService){
        this.trackerService = trackerService;
    }

    //Temporary signatures


}
