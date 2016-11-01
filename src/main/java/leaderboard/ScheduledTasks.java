package leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final LeaderboardRepository leaderboardRepository;
    private final LeaderBoardService leaderBoardService;

    @Autowired
    public ScheduledTasks(LeaderboardRepository leaderboardRepository,
                          LeaderBoardService leaderBoardService) {

        this.leaderboardRepository = leaderboardRepository;
        this.leaderBoardService = leaderBoardService;
    }

    @Scheduled(fixedDelayString = "${leaderboard.check-rate-ms}")
    public void checkChanges() {
        if (leaderboardRepository.isEnablePlaceChecking()) {
            leaderBoardService.checkChanges();
        }
    }
}