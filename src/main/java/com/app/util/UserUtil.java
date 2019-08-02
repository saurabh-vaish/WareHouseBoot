package com.app.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

	public void generatePieChart(String path,List<Object[]> users) {
		
		//1.create dataset
		DefaultPieDataset dataset=new DefaultPieDataset();
		for(Object[] d:users) {
			dataset.setValue(d[0].toString(), new Double(d[1].toString()));
		}
		
		//2.convert to jfree chart obj
		JFreeChart jFreeChart = ChartFactory.createPieChart3D("Users Details", dataset);
		
		//3.save as jpeg
		try {
			ChartUtils.saveChartAsJPEG(new File(path+"/resources/images/userPie.jpg"), jFreeChart, 250, 250);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void generateBarChart(String path,List<Object[]> users) {
		
		DefaultCategoryDataset dataset=new DefaultCategoryDataset();
		for(Object[] d:users) {
			dataset.setValue(new Double(d[1].toString()), d[0].toString(), "");
		}
		
		JFreeChart jFreeChart  = ChartFactory.createBarChart("Users Details", "Users Gender", "Count", dataset);
		
		try {
			ChartUtils.saveChartAsJPEG(new File(path+"/resources/images/userBar.jpg"), jFreeChart, 250, 250);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}














