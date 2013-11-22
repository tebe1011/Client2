package org.vaadin.example;

import java.awt.MultipleGradientPaint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RootUI extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CssLayout menu = new CssLayout();
	private CssLayout content = new CssLayout();
	private VerticalLayout loginLayout;
	private VerticalLayout contentLayout;

	private CheckBox cb_isEmployee;
	private CheckBox cb_isFirm;
	private CheckBox cb_isFirmAndContact;
	private CheckBox cb_isContact;
	private CheckBox cb_IgnorPerson;
	private CheckBox cb_Town;
	private CheckBox cb_Country;
	private CheckBox cb_Group;

	private ComboBox comBox_isEmployee;
	private ComboBox comBox_Town;
	private ComboBox comBox_Country;

	private TextField tf_PersonIgnore;
	private TextField tf_Town;
	private TextField tf_Country;
	private IntegerField tf_IntervallStart;
	private IntegerField tf_IntervalLEnd;
	private TextField tf_MaxPerson;

	private PopupDateField df_start;
	private PopupDateField df_end;

	private Slider sl_document;
	private Slider sl_appointment;
	private Slider sl_phonecalls;
	private Slider sl_emails;
	private Slider sl_opportunitys;

	private OptionGroup og_Groups;
	
	private TextArea resultArea;
	private JerseyClient restClient;
	private String UserID;
	private CasAnalyticUI ui;
	private Chart1 chart;
	private Button search;
	private Label timeOut;
	private Label wrongUser;

	public RootUI(CasAnalyticUI c) {
		super();
		ui = c;
		buildLoginView(false);
		restClient = new JerseyClient();
	}

	private void buildLoginView(boolean exit) {
		if (exit) {
			this.removeAllComponents();
		}

		this.addStyleName("root");
		this.setSizeFull();

		loginLayout = new VerticalLayout();
		loginLayout.setWidth("450px");
		loginLayout.addStyleName("login");
		this.addComponent(loginLayout);
		this.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);

		final CssLayout loginPanel = new CssLayout();

		HorizontalLayout container = new HorizontalLayout();
		container.setMargin(true);
		loginLayout.addComponent(container);

		Embedded e = new Embedded(null, new ThemeResource("img/cas_logo.jpg"));
		e.setWidth("130px");
		e.setHeight("130px");
		e.setStyleName("login");
		container.addComponent(e);
		container.setComponentAlignment(e, Alignment.MIDDLE_CENTER);

		VerticalLayout container2 = new VerticalLayout();
		container2.setStyleName("loginUserfield");
		container2.setWidth("200px");
		container.addComponent(container2);

		HorizontalLayout labels = new HorizontalLayout();
		labels.setWidth("100%");
		labels.setMargin(true);
		container2.addComponent(labels);

		Label welcome = new Label("Welcome to Relation Analytics");
		welcome.setSizeUndefined();
		labels.addComponent(welcome);
		labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);
		
		wrongUser = new Label("");
		wrongUser.addStyleName("error");
		container2.addComponent(wrongUser);
		container2.setComponentAlignment(wrongUser, Alignment.MIDDLE_CENTER);
		
		container2.addComponent(wrongUser);
		
		// Label title = new Label("Relation Analytics");
		// title.setSizeUndefined();
		// labels.addComponent(title);
		// labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

		HorizontalLayout fields = new HorizontalLayout();
		fields.setSpacing(true);
		fields.setMargin(true);

		final TextField username = new TextField("Username");
		username.focus();
		fields.addComponent(username);
		fields.setComponentAlignment(username, Alignment.MIDDLE_CENTER);

		final Button signin = new Button("lets go");
		signin.addStyleName("login");
		fields.addComponent(signin);
		fields.setComponentAlignment(signin, Alignment.MIDDLE_CENTER);

		final ShortcutListener enter = new ShortcutListener("Sign In", KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				signin.click();
			}
		};

		signin.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				String queryResult = restClient.requestUserData(username.getValue());

				if (!queryResult.equals("null")) {
					UserID = queryResult;
					buildMainView();
					getRelationData();
				}

				// if (username.getValue() != null &&
				// username.getValue().equals("")
				// && password.getValue() != null &&
				// password.getValue().equals("")) {
				// signin.removeShortcutListener(enter);
				//
				// UserID = username.getValue();
				// buildMainView();
				else {
					
					wrongUser.setValue("No such user!");
					
//					if (loginPanel.getComponentCount() > 2) {
//						// Remove the previous error message
//						loginPanel.removeComponent(loginPanel.getComponent(2));
//					}
//					// Add new error message
//					Label error = new Label(
//							"Wrong username or password. <span>Hint: try empty values</span>",
//							ContentMode.HTML);
//					error.addStyleName("error");
//					error.setSizeUndefined();
//					error.addStyleName("light");
//					// Add animation
//					error.addStyleName("v-animate-reveal");
//					loginPanel.addComponent(error);
					username.focus();
				}
			}
		});

		signin.addShortcutListener(enter);

		container2.addComponent(fields);

		loginLayout.addComponent(loginPanel);
		loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
	}

	private void buildMainView() {

		this.removeComponent(loginLayout);

		chart = new Chart1();

		contentLayout = new VerticalLayout();
		contentLayout.addStyleName("mainUI");
		contentLayout.setWidth("1300px");
		contentLayout.setMargin(true);
		this.addComponent(contentLayout);
		this.setComponentAlignment(contentLayout, Alignment.MIDDLE_CENTER);

		HorizontalLayout barTop = new HorizontalLayout();
		VerticalLayout weighting = new VerticalLayout();
		VerticalLayout periodOfTime = new VerticalLayout();
		VerticalLayout filter = new VerticalLayout();
		VerticalLayout filterGroup = new VerticalLayout();

		contentLayout.addComponent(barTop);
		contentLayout.addComponent(chart);

		barTop.addComponent(weighting);
		barTop.addComponent(periodOfTime);
		barTop.addComponent(filter);
		barTop.addComponent(filterGroup);

		weighting.addComponent(createSharedDocuments());
		weighting.addComponent(createSharedAppointments());
		weighting.addComponent(createSharedPhoneCalls());
		weighting.addComponent(createSharedEmails());
		weighting.addComponent(createSharedOppotunitys());
		weighting.addComponent(createSearchButton());
		weighting.addComponent(createTimeOut());
		weighting.setComponentAlignment(search, Alignment.MIDDLE_CENTER);
		weighting.setComponentAlignment(timeOut, Alignment.MIDDLE_CENTER);

		periodOfTime.addComponent(createStartPeriod());
		periodOfTime.addComponent(createStartField());
		periodOfTime.addComponent(createEndPeriod());
		periodOfTime.addComponent(createEndField());

		filter.addComponent(createIsEmployee());
		filter.addComponent(createIsCompany());
		filter.addComponent(createIsCompanyAndContact());
		filter.addComponent(createIsContact());
		filter.addComponent(createPerson());
		filter.addComponent(createTown());
		filter.addComponent(createCountry());
		filter.addComponent(createMaxikmalNumbers());

		filterGroup.addComponent(createGroups());

		filter.addStyleName("filter");
	}

	private Button createSearchButton() {

		search = new Button("calculate");
		search.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				long startFinal = getDateDiff(df_start.getValue(), df_end.getValue(), TimeUnit.DAYS);
				int summe = Integer.parseInt(tf_IntervalLEnd.getValue()) + Integer.parseInt(tf_IntervallStart.getValue());
				
				if(summe < startFinal) {
					getRelationData();
				}
			}
		});
		return search;
	}

	private Label createTimeOut() {
		
		timeOut = new Label("response time: -");
		timeOut.setStyleName("timeOut");
		timeOut.setValue("response time: ~ 0 ms");
		return timeOut;
	}
	
	private HorizontalLayout createIsEmployee() {

		comBox_isEmployee = new ComboBox();
		comBox_isEmployee.addItem("only Employees");
		comBox_isEmployee.addItem("only External");
		comBox_isEmployee.setNullSelectionAllowed(false);
		comBox_isEmployee.setValue("only Employees");
		comBox_isEmployee.setEnabled(false);

		cb_isEmployee = new CheckBox("Mitarbeiter");
		cb_isEmployee.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				final String valueString = String.valueOf(event.getProperty().getValue());
				if (valueString.equals("true")) {
					comBox_isEmployee.setEnabled(true);
				} else {
					comBox_isEmployee.setEnabled(false);
				}
			}
		});

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(cb_isEmployee);
		container.addComponent(comBox_isEmployee);

		return container;
	}

	private CheckBox createIsCompany() {
		cb_isFirm = new CheckBox("only companys");
		return cb_isFirm;
	}

	private CheckBox createIsCompanyAndContact() {
		cb_isFirmAndContact = new CheckBox("only companys & their Contacts");
		return cb_isFirmAndContact;
	}

	private CheckBox createIsContact() {
		cb_isContact = new CheckBox("private contacts");
		return cb_isContact;
	}

	private HorizontalLayout createPerson() {

		tf_PersonIgnore = new TextField();
		tf_PersonIgnore.setEnabled(false);

		cb_IgnorPerson = new CheckBox("Ignore Person");
		cb_IgnorPerson.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				final String valueString = String.valueOf(event.getProperty().getValue());
				if (valueString.equals("true")) {
					tf_PersonIgnore.setEnabled(true);
				} else {
					tf_PersonIgnore.setEnabled(false);
				}
			}
		});

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(cb_IgnorPerson);
		container.addComponent(tf_PersonIgnore);

		return container;
	}

	private HorizontalLayout createTown() {

		comBox_Town = new ComboBox();
		comBox_Town.addItem("excludes");
		comBox_Town.addItem("contains");
		comBox_Town.setEnabled(false);
		comBox_Town.setNullSelectionAllowed(false);
		comBox_Town.setValue("contains");
		comBox_Town.setWidth("100px");

		tf_Town = new TextField();
		tf_Town.setEnabled(false);

		cb_Town = new CheckBox("City");
		cb_Town.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				final String valueString = String.valueOf(event.getProperty().getValue());
				if (valueString.equals("true")) {
					comBox_Town.setEnabled(true);
					tf_Town.setEnabled(true);
				} else {
					comBox_Town.setEnabled(false);
					tf_Town.setEnabled(false);
				}
			}
		});

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(cb_Town);
		container.addComponent(comBox_Town);
		container.addComponent(tf_Town);

		return container;
	}

	private HorizontalLayout createCountry() {

		comBox_Country = new ComboBox();
		comBox_Country.addItem("excludes");
		comBox_Country.addItem("contains");
		comBox_Country.setEnabled(false);
		comBox_Country.setNullSelectionAllowed(false);
		comBox_Country.setValue("contains");
		comBox_Country.setWidth("100px");

		tf_Country = new TextField();
		tf_Country.setEnabled(false);

		cb_Country = new CheckBox("Country");
		cb_Country.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				final String valueString = String.valueOf(event.getProperty().getValue());
				if (valueString.equals("true")) {
					comBox_Country.setEnabled(true);
					tf_Country.setEnabled(true);
				} else {
					comBox_Country.setEnabled(false);
					tf_Country.setEnabled(false);
				}
			}
		});

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(cb_Country);
		container.addComponent(comBox_Country);
		container.addComponent(tf_Country);

		return container;
	}

	private HorizontalLayout createGroups() {

		og_Groups = new OptionGroup();

		for (Entry<String, String> entry : CasAnalyticUI.sysGroup.entrySet()) {
			if(!entry.getValue().contains("ZZZ")){
				og_Groups.addItem(entry.getKey());
			}
		}

		og_Groups.setEnabled(false);
		og_Groups.setMultiSelect(true);

		VerticalLayout containerOptionGroup = new VerticalLayout();
		containerOptionGroup.addStyleName("optionGroup");
		containerOptionGroup.addComponent(og_Groups);
		containerOptionGroup.setHeight("169px");
		containerOptionGroup.setWidth("230px");

		cb_Group = new CheckBox("Group");
		cb_Group.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				final String valueString = String.valueOf(event.getProperty().getValue());
				if (valueString.equals("true")) {
					og_Groups.setEnabled(true);
				} else {
					og_Groups.setEnabled(false);
				}
			}
		});

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(cb_Group);
		container.addComponent(containerOptionGroup);

		return container;
	}

	private HorizontalLayout createStartPeriod() {

		df_start = new PopupDateField();
		df_start.setValue(new Date());
		df_start.setImmediate(true);
		// df_start.setTimeZone(TimeZone.getTimeZone("UTC"));
		df_start.setLocale(Locale.getDefault());
		df_start.setResolution(Resolution.MINUTE);
		df_start.setDateFormat("dd-MM-yyyy HH:mm");
		df_start.setValue(new Date(2010 - 1900, 0, 1, 12, 0));

		df_start.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				final String valueString = String.valueOf(event.getProperty().getValue());
				Notification.show("Value changed:", valueString, Type.TRAY_NOTIFICATION);
			}
		});

		TextField label = new TextField("from");
		label.setReadOnly(true);
		label.setStyleName("periodOfTime");

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(label);
		container.addComponent(df_start);

		return container;
	}

	private HorizontalLayout createStartField() {
		tf_IntervallStart = new IntegerField();
		tf_IntervallStart.setValue("0");
		tf_IntervallStart.setWidth("50px");

		TextField label = new TextField("day weighting");
		label.setReadOnly(true);
		label.setStyleName("periodOfTime");

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(label);
		container.addComponent(tf_IntervallStart);
		return container;
	}

	private HorizontalLayout createEndField() {
		tf_IntervalLEnd = new IntegerField();
		tf_IntervalLEnd.setValue("0");
		tf_IntervalLEnd.setWidth("50px");

		TextField label = new TextField("day weighting");
		label.setReadOnly(true);
		label.setStyleName("periodOfTime");

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(label);
		container.addComponent(tf_IntervalLEnd);

		return container;
	}

	private HorizontalLayout createEndPeriod() {

		df_end = new PopupDateField();
		df_end.setValue(new Date());
		df_end.setImmediate(true);
		// df_end.setTimeZone(TimeZone.getTimeZone("UTC"));
		df_end.setLocale(Locale.getDefault());
		df_end.setResolution(Resolution.MINUTE);
		df_end.setDateFormat("dd-MM-yyyy HH:mm");

		df_end.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				final String valueString = String.valueOf(event.getProperty().getValue());
				Notification.show("Value changed:", valueString, Type.TRAY_NOTIFICATION);
			}
		});

		TextField label = new TextField("to");
		label.setReadOnly(true);
		label.setStyleName("periodOfTime");

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(label);
		container.addComponent(df_end);

		return container;
	}

	private HorizontalLayout createSharedDocuments() {

		TextField textField = new TextField();
		textField.setValue("shared documents");
		textField.setWidth("150px");
		textField.setReadOnly(true);
		textField.setImmediate(true);
		textField.setMaxLength(10);
		textField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(final TextChangeEvent event) {

			}
		});

		sl_document = new Slider();
		sl_document.setWidth("180px");
		sl_document.setImmediate(true);
		sl_document.setMin(0.0);
		sl_document.setMax(100.0);
		sl_document.setValue(100.0);

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(textField);
		container.addComponent(sl_document);

		return container;
	}

	private HorizontalLayout createSharedAppointments() {

		TextField textField = new TextField();
		textField.setValue("shared appointments");
		textField.setWidth("150px");
		textField.setReadOnly(true);
		textField.setImmediate(true);
		textField.setMaxLength(10);
		textField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(final TextChangeEvent event) {

			}
		});

		sl_appointment = new Slider();
		sl_appointment.setWidth("180px");
		sl_appointment.setImmediate(true);
		sl_appointment.setMin(0.0);
		sl_appointment.setMax(100.0);
		sl_appointment.setValue(100.0);

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(textField);
		container.addComponent(sl_appointment);

		return container;
	}

	private HorizontalLayout createSharedPhoneCalls() {

		TextField textField = new TextField();
		textField.setValue("shared phone calls");
		textField.setWidth("150px");
		textField.setReadOnly(true);
		textField.setImmediate(true);
		textField.setMaxLength(10);
		textField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(final TextChangeEvent event) {

			}
		});

		sl_phonecalls = new Slider();
		sl_phonecalls.setWidth("180px");
		sl_phonecalls.setImmediate(true);
		sl_phonecalls.setMin(0.0);
		sl_phonecalls.setMax(100.0);
		sl_phonecalls.setValue(100.0);

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(textField);
		container.addComponent(sl_phonecalls);

		return container;
	}

	private HorizontalLayout createSharedEmails() {

		TextField textField = new TextField();
		textField.setValue("shared emails");
		textField.setWidth("150px");
		textField.setReadOnly(true);
		textField.setImmediate(true);
		textField.setMaxLength(10);
		textField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(final TextChangeEvent event) {

			}
		});

		sl_emails = new Slider();
		sl_emails.setWidth("180px");
		sl_emails.setImmediate(true);
		sl_emails.setMin(0.0);
		sl_emails.setMax(100.0);
		sl_emails.setValue(100.0);

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(textField);
		container.addComponent(sl_emails);

		return container;
	}

	private HorizontalLayout createSharedOppotunitys() {

		TextField textField = new TextField();
		textField.setValue("shared Opportunities");
		textField.setWidth("150px");
		textField.setReadOnly(true);
		textField.setImmediate(true);
		textField.setMaxLength(10);
		textField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(final TextChangeEvent event) {

			}
		});

		sl_opportunitys = new Slider();
		sl_opportunitys.setWidth("180px");
		sl_opportunitys.setImmediate(true);
		sl_opportunitys.setMin(0.0);
		sl_opportunitys.setMax(100.0);
		sl_opportunitys.setValue(100.0);

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(textField);
		container.addComponent(sl_opportunitys);

		return container;
	}

	private HorizontalLayout createMaxikmalNumbers() {

		TextField label = new TextField("limit result to");
		label.setReadOnly(true);
		label.setWidth("90px");

		tf_MaxPerson = new IntegerField();
		tf_MaxPerson.setValue("10");
		tf_MaxPerson.setWidth("50px");

		HorizontalLayout container = new HorizontalLayout();
		container.addComponent(label);
		container.addComponent(tf_MaxPerson);

		return container;
	}
	
	private void getRelationData() {
		
		int valueIsEmployee = 0;
		int valueTown = 0;
		int valueCountry = 0;

		if (comBox_isEmployee.getValue().equals("only Employees")) {
			valueIsEmployee = 1;
		}
		if (comBox_Town.getValue().equals("contains")) {
			valueTown = 1;
		}
		if (comBox_Country.getValue().equals("contains")) {
			valueCountry = 1;
		}

		JSONObject obj = new JSONObject();
		try {

			obj.put("UserID", UserID);

			obj.put("CheckBox_isEmployee", cb_isEmployee.getValue());
			obj.put("CheckBox_isCompany", cb_isFirm.getValue());
			obj.put("CheckBox_isCompanyAndContact", cb_isFirmAndContact.getValue());
			obj.put("CheckBox_isContact", cb_isContact.getValue());
			obj.put("CheckBox_IgnorePerson", cb_IgnorPerson.getValue());
			obj.put("CheckBox_Town", cb_Town.getValue());
			obj.put("CheckBox_Country", cb_Country.getValue());
			obj.put("CheckBox_Group", cb_Group.getValue());
			
			obj.put("comboBox_isEmployee", valueIsEmployee);
			obj.put("comboBox_Town", valueTown);
			obj.put("comboBox_Country", valueCountry);

			obj.put("TextField_PersonIgnore", tf_PersonIgnore.getValue());
			obj.put("TextField_Town", tf_Town.getValue());
			obj.put("TextField_Country", tf_Country.getValue());
			obj.put("TextField_IntervallStart", tf_IntervallStart.getValue());
			obj.put("TextField_IntervalLEnd", tf_IntervalLEnd.getValue());
			obj.put("TextField_MaxPerson", tf_MaxPerson.getValue());

			obj.put("DateField_Start", df_start.getValue());
			obj.put("DateField_End", df_end.getValue());

			obj.put("Slider_Document", sl_document.getValue());
			obj.put("Slider_Appointment", sl_appointment.getValue());
			obj.put("Slider_PhoneCall", sl_phonecalls.getValue());
			obj.put("Slider_Email", sl_emails.getValue());
			obj.put("Slider_Opprtunity", sl_opportunitys.getValue());

			obj.put("OptionGroup_Group", og_Groups.getValue());

			System.out.println(obj.getString("OptionGroup_Group"));
			
			JSONObject result = restClient.doGetRequestStep4(obj);
			Iterator<?> keys = result.keys();

			String[] names = new String[result.length()];
			// Number[] values = new Number[result.length()];

			ArrayList<Number[]> series = new ArrayList<Number[]>();

			Number[] doc = new Number[result.length()-1];
			Number[] eml = new Number[result.length()-1];
			Number[] app = new Number[result.length()-1];
			Number[] opp = new Number[result.length()-1];
			Number[] phc = new Number[result.length()-1];

			series.add(doc);
			series.add(eml);
			series.add(app);
			series.add(opp);
			series.add(phc);

			int index = 0;
			boolean first = true;
			
			timeOut.setValue("response time: ~"+result.getLong("respond") + " ms");
			timeOut.requestRepaint();
			
			while (keys.hasNext()) {
				if(first) {
					keys.next();
					first = false;
				}
				else {
					String key = (String) keys.next();
					names[index] = CasAnalyticUI.sysUser.get(key);
					JSONArray container = (JSONArray) result.get(key);

					doc[index] = container.getInt(1);
					eml[index] = container.getInt(2);
					app[index] = container.getInt(3);
					opp[index] = container.getInt(4);
					phc[index] = container.getInt(5);
					index += 1;
				}
			}

			chart.refreshChart(names, series);

			// frame.addComponent(chart);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}
}
