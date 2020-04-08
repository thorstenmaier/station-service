package de.awr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/stations")
@CrossOrigin("http://localhost:4200")
public class StationController extends RESTController<Station, Long> {

    private StationRepository repo;

	@Autowired
    public StationController(StationRepository repo) {
        super(repo);
		this.repo = repo;
    }
    
    @GetMapping("/isValid")
    @ResponseBody
    public boolean isValid(@RequestParam("name") String name) {
    	return repo.countByName(name) == 0;
    }

}