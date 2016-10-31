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

    // TODO start when POST "/start", stop when POST "/stop"
    @Scheduled(fixedDelayString = "${leaderboard.check-rate-ms}")
    public void checkChanges() {
        leaderBoardService.checkChanges();
    }
}
