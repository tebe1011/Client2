package org.vaadin.example;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsSeries;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.Stacking;
import com.vaadin.addon.charts.model.Title;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.server.ClientConnector;

public class Chart1 extends Chart {

	public Chart1() {
		super(ChartType.COLUMN);

		Configuration conf = this.getConfiguration();

        conf.setTitle(new Title("People with the strongest relationship to you"));

        XAxis xAxis = new XAxis();
        xAxis.setCategories(new String[] { "Apples", "Oranges", "Pears",
                "Grapes", "Bananas" });
        conf.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setAllowDecimals(false);
        yAxis.setMin(0);
        yAxis.setTitle(new Title("Number of relationships"));
        conf.addyAxis(yAxis);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("function() { return ''+ this.x +''"
                + "+this.series.name +': '+ this.y +''+'Total: '+ this.point.stackTotal; }");
        conf.setTooltip(tooltip);

        PlotOptionsColumn plotOptions = new PlotOptionsColumn();
        plotOptions.setStacking(Stacking.NORMAL);
        conf.setPlotOptions(plotOptions);

        ListSeries serie = new ListSeries("Document", new Number[] { 5, 3, 4, 7, 2 });
        serie.setStack("stack");
        conf.addSeries(serie);

        serie = new ListSeries("Email", new Number[] { 3, 4, 4, 2, 5 });
        serie.setStack("stack");
        conf.addSeries(serie);
        
        serie = new ListSeries("Appointment", new Number[] { 1, 3, 4, 2, 5 });
        serie.setStack("stack");
        conf.addSeries(serie);
        
        serie = new ListSeries("Opportunity", new Number[] { 5, 2, 1, 4, 7 });
        serie.setStack("stack");
        conf.addSeries(serie);
        
        serie = new ListSeries("Phone Call", new Number[] { 9, 10, 12, 8, 10 });
        serie.setStack("stack");
        conf.addSeries(serie);

		this.drawChart(conf);
	}
	
	public void refreshChart(String[] xValues, ArrayList<Number[]> series) {
		

		
		this.getConfiguration().setSeries();
		this.getConfiguration().removexAxes();
		this.getConfiguration().removeyAxes();
		
		XAxis xAxis = new XAxis();
        xAxis.setCategories(xValues);
        this.getConfiguration().addxAxis(xAxis);
        

        YAxis yAxis = new YAxis();
        yAxis.setAllowDecimals(false);
        yAxis.setMin(0);
        yAxis.setTitle(new Title("Number of relationships"));
        this.getConfiguration().addyAxis(yAxis);
        
        ArrayList<Series> test = new ArrayList<Series>();
        
        ListSeries serie1 = new ListSeries("Document", series.get(0));
        serie1.setStack("stack");
        test.add(serie1);
        
        ListSeries serie2 = new ListSeries("Email", series.get(1));
        serie2.setStack("stack");
        test.add(serie2);
        
        ListSeries serie3 = new ListSeries("Appointment", series.get(2));
        serie3.setStack("stack");
        test.add(serie3);
        
        ListSeries serie4 = new ListSeries("Opportunity", series.get(3));
        serie4.setStack("stack");
        test.add(serie4);

        ListSeries serie5 = new ListSeries("Phone Call", series.get(4));
        serie5.setStack("stack");
        test.add(serie5);
        
        this.getConfiguration().setSeries(test);

		
		this.drawChart(this.getConfiguration());
	}
}
