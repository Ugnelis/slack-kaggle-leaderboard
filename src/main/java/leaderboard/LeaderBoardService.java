package leaderboard;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LeaderBoardService {

    private static final Logger LOG = LoggerFactory.getLogger(LeaderBoardService.class);

    private final LeaderBoardRepository leaderBoardRepository;
    private final String channelName;
    private final String webhookURL;
    private final String contestid;
    private final String teamId;

    @Autowired
    public LeaderBoardService(LeaderBoardRepository leaderBoardRepository,
                              @Value("${leaderboard.channel}") String channelName,
                              @Value("${leaderboard.webhook}") String webhookURL,
                              @Value("${leaderboard.contest-id}") String contestId,
                              @Value("${leaderboard.team-id}") String teamId) {

        this.leaderBoardRepository = leaderBoardRepository;
        this.channelName = channelName;
        this.webhookURL = webhookURL;
        this.contestid = contestId;
        this.teamId = teamId;
    }

    public SlackJson getCurrentPlace() {
        int place = scrapPlace().orElse(0);

        if (place == 0) {
            getCurrentPlace();
        }

        return new SlackJson("Current place: *" + place + "*");
    }

    public SlackJson startPlaceTracking() {
        leaderBoardRepository.setEnablePlaceChecking(true);
        return new SlackJson("Started place tracking!");
    }

    public SlackJson stopPlaceTracking() {
        leaderBoardRepository.setEnablePlaceChecking(false);
        return new SlackJson("Stopped place tracking!");
    }

    public void checkChanges() {
        int place = leaderBoardRepository.getPlace();

        if (place == 0) {
            place = scrapPlace()
                    .orElse(0);

            sendMessage(buildMessage("Current place *" + place + "*"));
        }

        int newPlace = scrapPlace()
                .orElse(0);

        // If place is 0 than is the error
        if (newPlace == 0)
            return;

        int previousPlace = place;

        place = newPlace;

        int difference = previousPlace - place;

        if (previousPlace < place) {
            sendMessage(buildMessage("*" + difference + "* Current place *" + place + "*"));
        }

        if (previousPlace > place) {
            sendMessage(buildMessage("*+" + difference + "* Current place *" + place + "*"));
        }
        leaderBoardRepository.setPlace(place);
    }

    private Optional<Integer> scrapPlace() {
        try {
            return Optional.ofNullable(Jsoup
                    .connect("https://www.kaggle.com/c/" + contestid + "/leaderboard")
                    .get()
                    .getElementById(teamId)
                    .getElementsByClass("leader-number")
                    .first())
                    .map(e -> Integer.parseInt(e.text()));

        } catch (Exception e) {
            LOG.error("Could not scrap place", e);
            return Optional.empty();
        }
    }

    private JSONObject buildMessage(String text) {
        return new JSONObject()
                .put("channel", channelName)
                .put("username", "Kaggle Leaderboard")
                .put("text", text)
                .put("icon_emoji", ":robot_face:");
    }

    private void sendMessage(JSONObject json) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String message = json.toString();

            HttpPost request = new HttpPost(webhookURL);
            StringEntity params = new StringEntity(message);

            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            LOG.info("Sending message: {}", message);
            StatusLine statusLine = httpClient.execute(request)
                    .getStatusLine();

            LOG.info("Message sent successfully, status code: {}, response: {}",
                    statusLine.getStatusCode(), statusLine.getReasonPhrase());

        } catch (Exception e) {
            LOG.error("Could not send message", e);
        }
    }
}