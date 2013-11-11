package org.vaadin.example;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        
        Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                layout.addComponent(new Label("Thank you for clicking"));
            }
        });
        layout.addComponent(button);
        
        Chart chart = new Chart();
        
        chart.getConfiguration().setSeries(new ListSeries(1,2,3,4,2,3,4));
        layout.addComponent(chart);
        // setContent(new DefectChartSizeSample());
    }
    
    
    public static class DefectChartSizeSample extends VerticalLayout {

        public DefectChartSizeSample() {
            addComponent(createChart());
            addComponent(new Button("Click Me"));
        }

        private Chart createChart(){
            Chart chart = new Chart(ChartType.COLUMN);
            chart.setWidth(225, Unit.PIXELS);
            chart.setHeight(150, Unit.PIXELS);

            Configuration config = chart.getConfiguration();

            XAxis x = new XAxis();
            x.setCategories("Group 1", "Group 2");
            config.addxAxis(x);

            Legend legend = new Legend();
            legend.setEnabled(false);
            config.setLegend(legend);

            DataSeries dataSeries = new DataSeries();
            dataSeries.add(new DataSeriesItem("Group 1", 50));
            dataSeries.add(new DataSeriesItem("Group 2", 100));
            config.addSeries(dataSeries);

            chart.drawChart(config);

            return chart;
        }
    }
}

