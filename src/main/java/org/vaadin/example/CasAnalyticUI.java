package org.vaadin.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("casanalyticclient")
public class CasAnalyticUI extends UI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextArea textArea;
	private TextField textField;
	private RootUI root;
	
	private String extractPath = System.getProperty("catalina.base") + "/CasAnalyticsData/Extract/";
	private String transformPath = System.getProperty("catalina.base") + "/CasAnalyticsData/Transform/";
	
	static Map<String,String> sysUser = new HashMap<String,String>();
	static Map<String,String> sysGroup = new HashMap<String,String>();
	
//	private JerseyClient restClient = new JerseyClient();

	@Override
	protected void init(VaadinRequest request) {

		if(sysUser.isEmpty()) {
			CSVReader reader;
			try {
				reader = new CSVReader(new FileReader(extractPath + "ClientUser.csv"));
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					sysUser.put(nextLine[0], nextLine[1]);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
		if(sysGroup.isEmpty()) {
			CSVReader reader;
			try {
				reader = new CSVReader(new FileReader(extractPath + "SysGroup.csv"));
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					sysGroup.put(nextLine[0], nextLine[1]);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		root = new RootUI(this);
		setContent(root);
	}
}