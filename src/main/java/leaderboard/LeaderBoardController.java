package leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class LeaderBoardController {

    private final LeaderBoardService leaderBoardService;

    @Autowired
    public LeaderBoardController(LeaderBoardService leaderBoardService) {
        this.leaderBoardService = leaderBoardService;
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public SlackJson getCurrentPlace() {
        return leaderBoardService.getCurrentPlace();
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public SlackJson startPlaceTracking() {
        return leaderBoardService.startPlaceTracking();
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public SlackJson stopPlaceTracking() {
        return leaderBoardService.stopPlaceTracking();
    }
}
