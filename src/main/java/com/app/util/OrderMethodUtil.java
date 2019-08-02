package com.app.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Component;

@Component
public class OrderMethodUtil {

	public void ganeratePie(String path,List<Object[]> data)
	{

		// create dataset from data

		DefaultPieDataset dataset = new DefaultPieDataset();
		for(Object[] o : data)
		{
			dataset.setValue(o[0].toString(),new Double(o[1].toString()));  // (key:String,value:Number)
		}

		// dataset should be converted to  JFreeChart using ChartFactory 

		JFreeChart chart = ChartFactory.createPieChart3D("Order Mode Pie Chart", dataset,true,true,false); // (title,dataset,legend,tooltip,urls)

		// JFreeChart should be converted to image using ChartUtils

		try {

			ChartUtils.saveChartAsJPEG(new File(path+"/resources/images/orderMethodPie.jpg"),chart,450,400); //(outStrem , chart,width,height)

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void ganerateBar(String path,List<Object[]> data)
	{
		// creating dataset
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for(Object[] ob:data)
		{
			// setting value , since first argument is number value so ob[1] for numbers data
			dataset.setValue(new Double(ob[1].toString()), ob[0].toString(),"");   // (number value , row ,column)
		}
		
		// creating JFreeChart Object using ChartFactory 
		JFreeChart chart =ChartFactory.createBarChart("Order Mode Bar Chart", "Order Mode", "Count",dataset, PlotOrientation.VERTICAL, true, true, false);
		// arguments - (title, x-axis name, y-axis name,orientation , legend , tooltip, urls)
		
		try {
			ChartUtils.saveChartAsJPEG(new File(path+"/resources/images/orderMethodBar.jpg"), chart, 450, 400);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
	}
	


}
