package hello;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RequestManager {

    private String webhookURL;
    private String channelName;
    private String teamId;
    private String competitionId;

    private int place;
    private JSONObject message;

    public RequestManager(String webhookURL,
                          String channelName,
                          String competitionId,
                          String teamId) {

        this.webhookURL = webhookURL;
        this.channelName = channelName;
        this.competitionId = competitionId;
        this.teamId = teamId;
    }

    RequestManager scrapPlace() {
        try {
            Document doc = Jsoup.connect("https://www.kaggle.com/c/" + competitionId + "/leaderboard").get();
            Element content = doc.getElementById(teamId);
            Elements rank = content.getElementsByClass("leader-number");
            place = Integer.parseInt(rank.first().text());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    RequestManager buildMessage(String text) {
        message = new JSONObject()
                .put("channel", channelName)
                .put("username", "Kaggle Leaderboard")
                .put("text", text)
                .put("icon_emoji", ":robot_face:");

        return this;
    }

    RequestManager sendMessage() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(webhookURL);
            StringEntity params = new StringEntity(message.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            httpClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    RequestManager checkChanges() {
        if (place == 0) {
            scrapPlace();
            return buildMessage("Current place *" + place + "*")
                    .sendMessage();
        }

        int previousPlace = place;
        int difference = previousPlace - place;
        scrapPlace();

        if (previousPlace < place) {
            return buildMessage("*-" + difference + "* Current place *" + place + "*")
                    .sendMessage();
        }

        if (previousPlace > place) {
            return buildMessage("*+" + difference + "* Current place *" + place + "*")
                    .sendMessage();
        }

        return this;
    }
}
