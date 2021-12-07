package com.example.countdown;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {
    @FXML
    public Button startBtn;
    @FXML
    public TextField ido;
    @FXML
    public Label idoVisszaLbl;

    private Timer t;
    public LocalDateTime lejarat;

    public void startBtnClick() {
        String timeText = ido.getText();
        LocalDateTime timeUntil = textToTime(timeText);
        if (timeUntil.isAfter(LocalDateTime.now())) {
            start(timeUntil);
        } else {
            new Alert(Alert.AlertType.WARNING, "nem adhatsz meg kisebb szamot mint a mostani datum", ButtonType.OK).show();
        }
    }

    private LocalDateTime textToTime(String timeText) {
        LocalDateTime timeUntil = LocalDateTime.now();
        String[] timeSplit;
        String[] date = new String[3];
        String[] time = new String[3];
        Integer[] napok = new Integer[]{31,30,31,30,31,30,31,31,30,31,30,31};

        if (timeText.length() == 0) {
            timeUntil = LocalDateTime.now().plusMinutes(1);
        } else {
            timeSplit = timeText.split(" ");
            if (timeSplit.length == 1) {
                if (timeSplit[0].contains(".")) {
                    date = mySplit(timeSplit[0], ".");
                } else if (timeSplit[0].contains(":")) {
                    time = mySplit(timeSplit[0], ":");
                }
            } else if (timeSplit.length == 2) {
                date = mySplit(timeSplit[0], ".");
                time = mySplit(timeSplit[1], ":");
            }
            for (String s : date) {
                if (!s.matches("\\d+")) s = "0";
            }for (String s : time) {
                if (!s.matches("\\d+")) s = "0";
            }
            if (Integer.parseInt(date[2]) > napok[Integer.parseInt(date[1])]) date[2] = (Integer.parseInt(date[2]) - 1) + "";
            timeUntil = LocalDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
        }
        return timeUntil;
    }

    private String[] mySplit(String text, String regex) {
        String[] helper = text.split(regex);
        String[] results = new String[3];
        for (int i = 0; i < 3; i++) {
            if (i >= helper.length) {
                results[i] = "0";
            } else {
                results[i] = helper[i];
            }
        }
        return results;
    }

    private void start(LocalDateTime timeUntil) {
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> { setTime(timeUntil); });
            }
        }, 0, 1000);

    }

    private void setTime(LocalDateTime timeUntil) {
        if (timeUntil.isBefore(LocalDateTime.now().plusSeconds(1))) {
            t.cancel();
            idoVisszaLbl.setText("Lejart");
        } else {
            Period p = Period.between(LocalDateTime.now().toLocalDate(), timeUntil.toLocalDate());
            Duration d = Duration.between(LocalDateTime.now(), timeUntil);

            int ev = p.getYears();
            int honap = p.getMonths();
            int nap = p.getDays();

            int ora = d.toHoursPart();
            int perc = d.toMinutesPart();
            int mp = d.toSecondsPart();

            String text = String.format("%d év %d hónap %d nap %02d:%02d:%02d", ev, honap, nap, ora, perc, mp);

            idoVisszaLbl.setText(text);
        }
    }
}