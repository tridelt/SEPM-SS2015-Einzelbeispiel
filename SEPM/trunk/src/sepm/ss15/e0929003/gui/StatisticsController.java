package sepm.ss15.e0929003.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import sepm.ss15.e0929003.entities.RaceResult;
import sepm.ss15.e0929003.service.Service;
import sepm.ss15.e0929003.service.ServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsController {

    private MainViewController mainViewController;
    private Service service;
    private BarChart chart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;


    public StatisticsController(MainViewController mainViewController, BarChart chart, CategoryAxis xAxis, NumberAxis yAxis) {
        this.mainViewController = mainViewController;
        this.service = mainViewController.getService();
        this.chart = chart;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public void onShowStatisticsClicked(TextField horseIdTextFieldInStatisticsTab,TextField jockeyIdTextFieldInStatisticsTab){
        try {
            Integer horseId = null;
            Integer jockeyId = null;
            if(!horseIdTextFieldInStatisticsTab.getText().isEmpty()){
                horseId = Integer.valueOf(horseIdTextFieldInStatisticsTab.getText());
            }
            if(!jockeyIdTextFieldInStatisticsTab.getText().isEmpty()){
                jockeyId = Integer.valueOf(jockeyIdTextFieldInStatisticsTab.getText());
            }

            HashMap<Integer,Integer> statistics = service.evaluateStatistics(new RaceResult(null,horseId,jockeyId,null,null,null,null,null,null,null));
            if(statistics.isEmpty()){
                throw new ServiceException("No races for the given IDs found.");
            }
            ArrayList<BarChart.Data> list = new ArrayList<BarChart.Data>();
            int maxValue = 0;
            ArrayList<String> categories = new ArrayList<String>();
            for(Map.Entry<Integer,Integer> entry : statistics.entrySet()) {
                if(entry.getValue()>maxValue){
                    maxValue = entry.getValue();
                }
                list.add(new BarChart.Data(entry.getKey()+"",entry.getValue()));
                categories.add(entry.getKey()+"");
            }
            xAxis.getCategories().clear();
            xAxis.setCategories(FXCollections.observableArrayList(categories));
            yAxis.setUpperBound(maxValue+2);
            ObservableList barChartData = FXCollections.observableArrayList(new BarChart.Series( FXCollections.observableArrayList(list)));
            chart.setData(barChartData);
        } catch (ServiceException e) {
            mainViewController.showAlertDialog("Statistics", "Couldn't show statistics.", e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}
