# slack-kaggle-leaderboard

## About

Slack Kaggle LeaderBoard is for checking Kaggle contest current place and changes in Slack.

## Usage

### Configuration

Change _src/main/resources/application.yml_ file configuration. 

### Endpoints

| Method        | Endpoint      | Description                  |
| ------------- | ------------- | ---------------------------- |
| `GET`         | */current*    | gets current position        |
| `POST`        | */start*      | starts place change tracking |
| `POST`        | */stop*       | stops place change tracking  |
