package draganddrop.studybuddy.ui;

import java.util.List;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.model.statistics.Statistics;
import draganddrop.studybuddy.model.statistics.StatsUtil;
import draganddrop.studybuddy.model.task.Task;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

/**
 * Productivity page that contains usage statistics
 */
public class ProductivityPage extends UiPart<Region> {
    private static final String FXML = "ProductivityPage.fxml";
    private static Statistics statistics;

    private final Logger logger = LogsCenter.getLogger(ProductivityPage.class);
    private ObservableList<Task> taskList;

    @FXML
    private Label menuPointsLabel;

    @FXML
    private TabPane productivityTabPane;
    //daily
    @FXML
    private Label dailyStatsLabel;
    @FXML
    private Label streakLabel;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Circle progressCentre;
    @FXML
    private Image grayStarImage;
    @FXML
    private Image goldStarImage;
    @FXML
    private ImageView grayStarImageView;
    @FXML
    private ImageView goldStarImageView;

    //weekly
    @FXML
    private BarChart<String, Integer> weeklyBarChart;
    @FXML
    private Label weeklyStatsLabel;

    //score
    @FXML
    private Label ppLabel;
    @FXML
    private Label rankLabel;
    @FXML
    private Label nextRankLabel;
    @FXML
    private ImageView rankZeroImageView;
    @FXML
    private ImageView rankOneImageView;
    @FXML
    private ImageView rankTwoImageView;
    @FXML
    private ImageView rankThreeImageView;
    @FXML
    private ImageView rankFourImageView;
    @FXML
    private ImageView rankFiveImageView;
    @FXML
    private ImageView rankSixImageView;
    @FXML
    private AreaChart<String, Integer> ppAreaChart;
    @FXML
    private ProgressIndicator rankProgressIndicator;


    public ProductivityPage(ObservableList<Task> taskList, Label menuPointsLabel) {
        super(FXML);
        this.taskList = taskList;
        this.menuPointsLabel = menuPointsLabel;
        generateProductivityPage();

        productivityTabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            productivityTabPane.setTabMinWidth(productivityTabPane.getWidth() / 3 - 40);
            productivityTabPane.setTabMaxWidth(productivityTabPane.getWidth() / 3 - 40);
        });

        taskList.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> t) {
                generateProductivityPage();
            }
        });
    }

    public static void setStatistics(Statistics statistics) {
        ProductivityPage.statistics = statistics;
    }

    /**
     * generates the productivity page
     */
    public void generateProductivityPage() {
        renderDailyTab();
        renderWeeklyTab();
        renderScoreTab();
    }

    public void selectTab(int tabNumber) {
        productivityTabPane.getSelectionModel().select(tabNumber);
    }

    /**
     * renders the daily tab
     */
    public void renderDailyTab() {
        renderDailyStats();
        renderProgressIndicator();
        renderStreak();
    }

    /**
     * renders the weekly tab
     */
    public void renderWeeklyTab() {
        renderWeeklyStats();
        renderWeeklyBarChart();
    }


    /**
     * renders the score tab
     */
    public void renderScoreTab() {
        renderScore();
        renderRank();
        renderNextRank();
        renderRankIcon();
        renderAreaChart();
    }

    // daily

    /**
     * renders the progress indicator
     */
    public void renderProgressIndicator() {
        int taskCompletedCount = statistics.getCompleteCountToday();
        int goal = statistics.getGoal();

        double progress = (double) taskCompletedCount / (double) goal;
        if (taskCompletedCount >= goal) {
            goldStarImageView.setVisible(true);
            grayStarImageView.setVisible(false);
        }
        progressIndicator.setProgress(progress);
    }

    /**
     * renders daily stats
     */
    public void renderDailyStats() {
        int taskCompletedCount = statistics.getCompleteCountToday();
        int goal = statistics.getGoal();
        String completedString = taskCompletedCount + " / " + goal + " tasks\n";

        dailyStatsLabel.setText(completedString);
    }

    /**
     * renders user's streak
     */
    public void renderStreak() {
        int streak = statistics.getStreak();
        String streakString = "You've completed your goal " + streak + " days in a row";

        streakLabel.setText(streakString);
    }

    //weekly

    /**
     * renders weekly stats
     */
    public void renderWeeklyStats() {
        int weeklyTaskCompleted = statistics.getCompleteCountThisWeek();
        int weeklyTaskOverdue = statistics.getOverdueCountThisWeek();
        String weeklyStatsString = "You have completed " + weeklyTaskCompleted + " tasks this week\n"
            + weeklyTaskOverdue + " tasks were Overdue";
        weeklyStatsLabel.setText(weeklyStatsString);
    }

    /**
     * renders the weekly bar chart
     */
    public void renderWeeklyBarChart() {
        weeklyBarChart.setTitle("Task completed in last 7 days");
        List<String> dayList = StatsUtil.getDayList();
        List<Integer> weeklyCompleteCountList = statistics.getWeeklyCompleteCountList();
        if (!weeklyBarChart.getData().isEmpty()) {
            weeklyBarChart.getData().clear();
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (int i = 0; i < 7; i++) {
            XYChart.Data<String, Integer> data = new XYChart.Data<>(dayList.get(i), weeklyCompleteCountList.get(i));
            series.getData().add(data);
        }
        weeklyBarChart.getData().add(series);
        weeklyBarChart.setLegendVisible(false);
    }

    // score

    /**
     * renders the productivity points area chart
     */
    public void renderAreaChart() {
        ppAreaChart.setTitle("Productivity Points Progression");
        List<String> dayList = StatsUtil.getDayList();
        List<Integer> weeklyScores = statistics.getWeeklyScores();
        if (!ppAreaChart.getData().isEmpty()) {
            ppAreaChart.getData().clear();
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (int i = 0; i < 7; i++) {
            XYChart.Data<String, Integer> data = new XYChart.Data<>(dayList.get(i), weeklyScores.get(i));
            series.getData().add(data);
        }
        ppAreaChart.getData().add(series);
        ppAreaChart.setLegendVisible(false);
    }

    /**
     * renders score
     */
    public void renderScore() {
        int score = statistics.getScoreToday();
        String scoreString = score + " PP";
        ppLabel.setText(scoreString);
        // set points on the menu label
        menuPointsLabel.setText(scoreString);
    }

    /**
     * renders the user's rank
     */
    public void renderRank() {
        rankLabel.setText(statistics.getRank());

    }

    /**
     * renders the rank icon
     */
    public void renderRankIcon() {
        toggleRankImageView(getRankNumberFromRank(statistics.getRank()));
        int scoreToday = statistics.getScoreToday();
        int scoreToNextRank = statistics.getScoreToNextRank();
        double rankProgress = (double) scoreToday / (double) (scoreToday + scoreToNextRank);
        rankProgressIndicator.setProgress(rankProgress);
    }

    /**
     * returns a number corresponding to the rank
     * @param rank user's rank
     * @return a number corresponding to the rank
     */
    public int getRankNumberFromRank(String rank) {
        switch (rank) {
        case "Beginner":
            return 0;
        case "Novice":
            return 1;
        case "Apprentice":
            return 2;
        case "Duke":
            return 3;
        case "Expert":
            return 4;
        case "Master":
            return 5;
        default:
            return 6;
        }
    }

    /**
     * renders the user's next rank
     */
    public void renderNextRank() {
        nextRankLabel.setText(statistics.getNextRank());
    }

    /**
     * sets all rank ImageView to invisible except for one corresponding to rankNumber
     * @param rankNumber the rankNumber that should be set as visible
     */
    public void toggleRankImageView(int rankNumber) {
        hideAllRankImageView();
        switch (rankNumber) {
        case 0:
            rankZeroImageView.setVisible(true);
            break;
        case 1:
            rankOneImageView.setVisible(true);
            break;
        case 2:
            rankTwoImageView.setVisible(true);
            break;
        case 3:
            rankThreeImageView.setVisible(true);
            break;
        case 4:
            rankFourImageView.setVisible(true);
            break;
        case 5:
            rankFiveImageView.setVisible(true);
            break;
        case 6:
            rankSixImageView.setVisible(true);
            break;
        default:
            break;
        }
    }

    /**
     * hides all rank ImageViews
     */
    public void hideAllRankImageView() {
        rankZeroImageView.setVisible(false);
        rankOneImageView.setVisible(false);
        rankTwoImageView.setVisible(false);
        rankThreeImageView.setVisible(false);
        rankFourImageView.setVisible(false);
        rankFiveImageView.setVisible(false);
        rankSixImageView.setVisible(false);
    }
}
