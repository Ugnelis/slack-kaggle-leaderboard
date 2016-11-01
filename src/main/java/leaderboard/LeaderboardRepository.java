package leaderboard;

import org.springframework.stereotype.Repository;

@Repository
public class LeaderboardRepository {

    private boolean enablePlaceChecking = true;
    private int place = 0;

    public boolean isEnablePlaceChecking() {
        return enablePlaceChecking;
    }

    public void setEnablePlaceChecking(boolean enablePlaceChecking) {
        this.enablePlaceChecking = enablePlaceChecking;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}