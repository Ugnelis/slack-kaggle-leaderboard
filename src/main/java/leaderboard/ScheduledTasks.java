package leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final LeaderBoardService leaderBoardService;

    @Autowired
    public ScheduledTasks(LeaderBoardService leaderBoardService) {
        this.leaderBoardService = leaderBoardService;
    }

    @Scheduled(fixedDelayString = "${leaderboard.check-rate-ms}")
    public void reportCurrentTime() {
        leaderBoardService.checkChanges();
    }
}