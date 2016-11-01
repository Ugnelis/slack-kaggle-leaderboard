package leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final LeaderBoardRepository leaderBoardRepository;
    private final LeaderBoardService leaderBoardService;

    @Autowired
    public ScheduledTasks(LeaderBoardRepository leaderboardRepository,
                          LeaderBoardService leaderBoardService) {

        this.leaderBoardRepository = leaderboardRepository;
        this.leaderBoardService = leaderBoardService;
    }

    @Scheduled(fixedDelayString = "${leaderboard.check-rate-ms}")
    public void checkChanges() {
        if (leaderBoardRepository.isEnablePlaceChecking()) {
            leaderBoardService.checkChanges();
        }
    }
}