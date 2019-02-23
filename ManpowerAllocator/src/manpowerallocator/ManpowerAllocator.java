package manpowerallocator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class ManpowerAllocator extends JFrame
{
    private static JPanel[] welcomeScreenPanels;
    
    private static JTabbedPane attendanceTabPane;
    private static JTabbedPane employeeTabPane;
    private static JPanel editEmployeeScreen;
    private static JPanel newEmployeeInfoTab;
    private static JPanel newEmployeeJobTab;
    private static JPanel newEmployeeNotesTab;
    private static JPanel newEmployeeSaveTab;
    private static JPanel newEmployeeOptionTab;
    
    private static JTabbedPane existingEmployeeTabPane;
    private static JPanel existingEmployeeConfirmationScreen;
    private static JPanel optionsScreen;
    
    private static JPanel existingEmployeeScreen;
    private static JPanel newEmployeeConfirmationScreen;
    private static JPanel newAttendanceTab;
    private static JPanel newAttendanceNotesTab;
    private static JPanel newAttendanceSaveTab;
    private static JPanel newAttendanceOptionsTab;
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final double screenRatio = screenSize.getWidth() / screenSize.getHeight();
    private static String[] DEPARTMENTS;
    private static final String[] WORK_AREA_MATERIALS = {" ", "Docks", "PrePaint Chassis"};
    private static final String[] WORK_AREA_PPCHASSIS = {" ", "Engine Line"};
    private static final ArrayList<String> DOCK_JOBS = new ArrayList<>();
    private static final ArrayList<String> PPCHASSIS_JOBS = new ArrayList<>();
    private static final ArrayList<String> ENGINE_LINE_JOBS = new ArrayList<>();
    private static final String[] JOB_TYPES = {"Training", "Primary", "Secondary"};
    private static ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private static ArrayList<JCheckBox> jobSelectedToAdd = new ArrayList<>();
    private static ArrayList<JCheckBox> jobSelectedToRemove = new ArrayList<>();
    private static Employee newEmployee;
    private static ExistingEmployee existingEmp;
    private static Options option;
    private static JPanel newEmployeeConfirmationPanel;
    private static ArrayList<String> employeeInfo = new ArrayList<>();
    private static ArrayList<String> employeeJobsList = new ArrayList<>();
    private static String employeeNotes;
    static Attendance attendance = new Attendance();
    
    private static InputValidator validator;
    private static boolean errorMessagePresent = false;
    
            
    public ManpowerAllocator()
    {
        //Configure JFrame for displaying ManPowerAllocator GUI
        super("Manpower Allocator");        
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension((int)screenSize.getWidth(), (int)screenSize.getHeight()));
        
        addAndSortJobs();
        
        validator = new InputValidator();
        
        //Create Tab Pane and add all tabs for Attendance Selection 
        
        attendanceTabPane = attendance.getAttendanceTab();
        
        add(attendanceTabPane);
        
        //Create Tab Pane and add all tabs for adding New Employee Selection 
        employeeTabPane = new JTabbedPane();
        
        newEmployeeInfoTab = createNewEmployeeInfoTab();
        employeeTabPane.setFont(new Font( "Times New Roman", Font.PLAIN, 40));
        employeeTabPane.addTab("INFO", newEmployeeInfoTab);
        
        newEmployeeJobTab = createNewEmployeeJobTab();
        employeeTabPane.addTab("JOBS", newEmployeeJobTab);
        employeeTabPane.setEnabledAt(1, false);
        
        newEmployeeNotesTab = createNewEmployeeNotesTab();
        employeeTabPane.addTab("NOTES", newEmployeeNotesTab);
        employeeTabPane.setEnabledAt(2, false);
        
        newEmployeeSaveTab = createNewEmployeeSaveTab();
        employeeTabPane.addTab("SAVE", newEmployeeSaveTab);
        employeeTabPane.setEnabledAt(3, false);
        
        newEmployeeOptionTab = createNewEmployeeOptionTab();
        employeeTabPane.addTab("OPTIONS", newEmployeeOptionTab);
        
        add(employeeTabPane);
        employeeTabPane.setVisible(false);
        
        //Create and add confirmation screen for adding a new employee
        newEmployeeConfirmationScreen = createNewEmployeeConfirmationScreen();
        add(newEmployeeConfirmationScreen);
        newEmployeeConfirmationScreen.setVisible(false);
        
        //Create Screen for editing an existing employee
        editEmployeeScreen = createEditEmployeeScreen();
        add(editEmployeeScreen);
        editEmployeeScreen.setVisible(false);
        
        existingEmp = new ExistingEmployee();
        existingEmp.getJFrame(this);
        existingEmployeeConfirmationScreen = existingEmp.createExistingEmployeeConfirmationScreen("update");
        add(existingEmployeeConfirmationScreen);
        existingEmployeeConfirmationScreen.setVisible(false);
        
        existingEmployeeScreen = existingEmp.createExistingEmployeeSearchScreen();
        add(existingEmployeeScreen);
        existingEmployeeScreen.setVisible(false);
        
        existingEmployeeTabPane = existingEmp.getExistingEmployeeTab(existingEmployeeScreen);
        add(existingEmployeeTabPane);
        existingEmployeeTabPane.setVisible(false);
        
        option  = new Options();
        option.getJFrame(this);
        optionsScreen = option.createIntroOptionsScreen();
        add(optionsScreen);
        optionsScreen.setVisible(false);
        
        
        //Create initial Welcome screen for when GUI is opened
        welcomeScreenPanels = createWelcomeScreen();
        add(welcomeScreenPanels[0], "North");
        add(welcomeScreenPanels[1], "South");
        
        
            
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    /**
     * Method to create and add all components on the Welcome Screen
     * 
     * @return JPanel[] returns an array of JPanels containing the upper panel
     * which contains welcome text and lower panel which contains buttons
     */
    private static JPanel[] createWelcomeScreen()
    {
        //Create and set layout for welcome text
        JPanel upperPanel = new JPanel();
        upperPanel.setBackground(Color.DARK_GRAY);
        upperPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        //Create and add welcome text
        JTextArea welcomeText = new JTextArea("Manpower");
        welcomeText.setForeground(Color.WHITE);
        welcomeText.setBackground(Color.DARK_GRAY);
        welcomeText.setEditable(false);
        Font f = new Font("Times", Font.PLAIN, screenSize.width/10);
        welcomeText.setFont(f);
        upperPanel.add(welcomeText);

        //Create and set layout and font for buttons
        JPanel lowerPanel = new JPanel();
        lowerPanel.setBackground(Color.DARK_GRAY);
        lowerPanel.setLayout(new GridLayout(2, 2, (int)(screenSize.getWidth() / 50), (int)(screenSize.getWidth() / 50)));
        lowerPanel.setPreferredSize(new Dimension((int)(screenSize.getWidth() / 2), (int)(screenSize.getHeight() / 2)));
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 80, 50));
        
        JButton attendanceButton = new JButton("Attendance");
        JButton editEmployeeButton = new JButton("Edit Employee");
        JButton optionsButton = new JButton("Options");
        JButton allocateButton = new JButton("Allocate");

        Font buttonFont = new Font("Arial", Font.BOLD, 30);
        attendanceButton.setFont(buttonFont);
        editEmployeeButton.setFont(buttonFont);
        optionsButton.setFont(buttonFont);
        allocateButton.setFont(buttonFont);
        
        //Add attendance button and declare what happens when clicked
        lowerPanel.add(attendanceButton);
        attendanceButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                upperPanel.setVisible(false);
                lowerPanel.setVisible(false);
                attendanceTabPane = attendance.getAttendanceTab();
                attendanceTabPane.setVisible(true);
                
            }
        });
            
        //Add edit employee button and declare what happens when clicked
        lowerPanel.add(editEmployeeButton);
        editEmployeeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                upperPanel.setVisible(false);
                lowerPanel.setVisible(false);
                editEmployeeScreen.setVisible(true);
            }
        });
            
        //Add option button and declare what happens when clicked
        lowerPanel.add(optionsButton);
        optionsButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                upperPanel.setVisible(false);
                lowerPanel.setVisible(false);
                optionsScreen.setVisible(true);
            }
        });
         
        //Add allocate button and declare what happens when clicked
        lowerPanel.add(allocateButton);
        allocateButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                Allocation a = new Allocation(DOCK_JOBS);
            }
        });
        
        JPanel[] welcomeScreenPanel = {upperPanel,lowerPanel};
        return welcomeScreenPanel;
    }
    
    /**
     * Method to create and add all components viewed on the edit employee screen
     * 
     * @return JPanel containing the entirety of the components formatted and added
     * to the screen
     */
    private static JPanel createEditEmployeeScreen()
    {
        //Create and set layout for edit employee panel
        JPanel editEmployeePanel = new JPanel();
        editEmployeePanel.setLayout(new GridLayout(1, 2, 50, 0));
        editEmployeePanel.setBackground(Color.DARK_GRAY);

        //Create and customize buttons to choose type of employee
        JButton newEmployeeButton = new JButton("New Employee");
        JButton existingEmployeeButton = new JButton("Existing Employee");

        Font buttonFont = new Font("Arial", Font.BOLD, 30);
        newEmployeeButton.setFont(buttonFont);
        existingEmployeeButton.setFont(buttonFont);

        //Add new employee button and specify what happens when clicked
        editEmployeePanel.add(newEmployeeButton);
        newEmployeeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                editEmployeeScreen.setVisible(false);
                employeeTabPane.setVisible(true);
                newEmployeeInfoTab.setVisible(true);
            }
        });
        
        //Add existing employee button and specify what happens when clicked
        editEmployeePanel.add(existingEmployeeButton);
        existingEmployeeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                editEmployeeScreen.setVisible(false);
                existingEmployeeScreen.setVisible(true);
            }
        });
        
        return editEmployeePanel;
    }
    
    /**
     * Method to create the new employee info tab and add all components
     * 
     * @return JPanel containing all components to be shown on tab
     */
    private static JPanel createNewEmployeeInfoTab()
    {
        //Create panel to contain components and set layout
        JPanel newEmployeePanel = new JPanel();
        
        GridBagLayout layout = new GridBagLayout();
        newEmployeePanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        newEmployeePanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        //Begin creating all components to add to screen
        JTextArea nameUneditableText = new JTextArea("Name:");
        formatTextField(nameUneditableText, false, Color.WHITE, f);
        
        JTextField lastNameBox = new JTextField("Last", 12);
        lastNameBox.setFont(f);
        
        JTextField firstNameBox = new JTextField("First", 12);
        firstNameBox.setFont(f);
        
        JTextArea employeeIDText = new JTextArea("Employee ID:");
        formatTextField(employeeIDText, false, Color.WHITE, f);
        
        JTextField employeeIDInput = new JTextField("12345", 4);
        employeeIDInput.setFont(f);
        
        JTextArea seniorityDateText = new JTextArea("Seniority:");
        formatTextField(seniorityDateText, false, Color.WHITE, f);
        
        JTextField seniorityDateInput = new JTextField("MM/DD/YYYY");
        seniorityDateInput.setFont(f);
   
        JTextArea gradeText = new JTextArea("Grade 5/6?");
        formatTextField(gradeText, false, Color.WHITE, f);
        
        JComboBox gradePaySelection = new JComboBox();
        gradePaySelection.addItem("No");
        gradePaySelection.addItem("Utility");
        gradePaySelection.addItem("Complexity");
        gradePaySelection.addItem("Team Leader");
        gradePaySelection.setFont(f);
        
        JButton continueButton = new JButton("Save Changes");
        continueButton.setFont(f);
        
        //Save info and go to next tab when clicked
        continueButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                boolean canProceed = false;
                Font errorFont = new Font("Times", Font.BOLD, 30);
                if(validator.validateName(lastNameBox.getText()))
                {
                    if(validator.validateName(firstNameBox.getText()))
                    {
                        if(validator.validateEmployeeID(employeeIDInput.getText()))
                        {
                            if(validator.validateDate(seniorityDateInput.getText()))
                            {
                                canProceed = true;
                            }
                            else
                            {
                                if(errorMessagePresent)
                                {
                                    newEmployeePanel.remove(10);
                                }
                                JTextArea errorMessage = new JTextArea("-Please check date input (MM/DD/YYYY). Numbers only");
                                formatTextField(errorMessage, false, Color.RED, errorFont);
                                gbc.anchor = GridBagConstraints.WEST;
                                addObjects(errorMessage, newEmployeePanel, layout, gbc, 0, 0, 7, 1, 1, 1);
                                newEmployeePanel.repaint();
                                newEmployeePanel.revalidate();
                                errorMessagePresent = true;
                                employeeTabPane.setEnabledAt(3, false);
                            }
                        }
                        else
                        {
                            if (errorMessagePresent) 
                            {
                                newEmployeePanel.remove(10);
                            }
                            JTextArea errorMessage = new JTextArea("-Please check employee ID input (12345)");
                            formatTextField(errorMessage, false, Color.RED, errorFont);
                            gbc.anchor = GridBagConstraints.WEST;
                            addObjects(errorMessage, newEmployeePanel, layout, gbc, 0, 0, 7, 1, 1, 1);
                            newEmployeePanel.repaint();
                            newEmployeePanel.revalidate();
                            errorMessagePresent = true;
                            employeeTabPane.setEnabledAt(3, false);
                        }
                    }
                    else
                    {
                        if (errorMessagePresent) 
                        {
                            newEmployeePanel.remove(10);
                        }
                        JTextArea errorMessage = new JTextArea("-Please check first name input (no whitespace, numbers, special characters)");
                        formatTextField(errorMessage, false, Color.RED, errorFont);
                        gbc.anchor = GridBagConstraints.WEST;
                        addObjects(errorMessage, newEmployeePanel, layout, gbc, 0, 0, 7, 1, 1, 1);
                        newEmployeePanel.repaint();
                        newEmployeePanel.revalidate();
                        errorMessagePresent = true;
                        employeeTabPane.setEnabledAt(3, false);
                    }
                }
                else
                {
                    if (errorMessagePresent) 
                    {
                        newEmployeePanel.remove(10);
                    }
                    JTextArea errorMessage = new JTextArea("-Please check last name input (no whitespace, numbers, special characters)");
                    formatTextField(errorMessage, false, Color.RED, errorFont);
                    gbc.anchor = GridBagConstraints.WEST;
                    addObjects(errorMessage, newEmployeePanel, layout, gbc, 0, 0, 7, 1, 1, 1);
                    newEmployeePanel.repaint();
                    newEmployeePanel.revalidate();
                    errorMessagePresent = true;
                    employeeTabPane.setEnabledAt(3, false);
                }
                if(canProceed)
                {
                    if(errorMessagePresent)
                    {
                        newEmployeePanel.remove(10);
                        newEmployeePanel.repaint();
                        newEmployeePanel.revalidate();
                        errorMessagePresent = false;
                    }
                    if(employeeTabPane.isEnabledAt(2))
                    {
                        employeeTabPane.setEnabledAt(3, true);
                    }
                    String[] employeeNewInfo = {lastNameBox.getText().toUpperCase(), 
                        firstNameBox.getText().toUpperCase(), employeeIDInput.getText(), 
                        seniorityDateInput.getText(), (String)gradePaySelection.getSelectedItem()};
                    employeeInfo.clear();
                    storeEmployeeInfo(employeeNewInfo);
                    employeeTabPane.setSelectedIndex(1);
                    employeeTabPane.setEnabledAt(1, true);
                }
            }
        });

        //Add all components to screen using convenience method for grid bag layout
        addObjects(nameUneditableText, newEmployeePanel, layout, gbc, 0, 1, 1, 1, 1, 1);
        addObjects(lastNameBox, newEmployeePanel, layout, gbc, 1, 1, 3, 1, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        addObjects(firstNameBox, newEmployeePanel, layout, gbc, 2, 1, 5, 1, 1, 1);
        addObjects(employeeIDText, newEmployeePanel, layout, gbc, 0, 2, 3, 1, 1, 1);
        addObjects(employeeIDInput, newEmployeePanel, layout, gbc, 1, 2, 2, 1, 1, 1);
        addObjects(seniorityDateText, newEmployeePanel, layout, gbc, 0, 3, 2, 1, 1, 1);
        addObjects(seniorityDateInput, newEmployeePanel, layout, gbc, 1, 3, 1, 1, 1, 1);
        addObjects(gradeText, newEmployeePanel, layout, gbc, 0, 4, 1, 1, 1, 1);
        addObjects(gradePaySelection, newEmployeePanel, layout, gbc, 1, 4, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.EAST;
        addObjects(continueButton, newEmployeePanel, layout, gbc, 5, 5, 1, 1, 1, 1);
         
        return newEmployeePanel;
    }
    
    /**
     * Method for creating the components shown on the job tab for new employees 
     * 
     * @return JPanel containing all components to be shown on tab 
     */
    private static JPanel createNewEmployeeJobTab()
    {
        //Create job panel and set layout
        JPanel newEmployeeJobPanel = new JPanel();
        
        GridBagLayout layout = new GridBagLayout();
        newEmployeeJobPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        newEmployeeJobPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        //Begin creating all components to be added 
        JTextArea departmentText = new JTextArea("Department:");
        formatTextField(departmentText, false, Color.WHITE, f);
        
        //Components to be variable depending on what user clicks
        JComboBox workGroupSelection = new JComboBox();
        JList jobsPossibleContainer = new JList();
        JList jobsSelectedContainer = new JList();
        ArrayList<String> selectedJobNames = new ArrayList<>();
        ArrayList<String> selectedJobNamesWithJobType = new ArrayList<>();
        
        JComboBox jobTypeSelection = new JComboBox(JOB_TYPES);
        
        getDepartments();
        JComboBox departmentSelection = new JComboBox(DEPARTMENTS);
        departmentSelection.setFont(f);
        
        //Declare which work groups appear when certain departments are selected
        departmentSelection.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String itemSelected = (String)departmentSelection.getSelectedItem();
                switch (itemSelected) {
                    case " ":
                        workGroupSelection.setModel(new DefaultComboBoxModel());
                        break;
                    case "Materials":
                        workGroupSelection.setModel(new DefaultComboBoxModel(WORK_AREA_MATERIALS));
                        break;
                    case "PrePaint Chassis":
                        workGroupSelection.setModel(new DefaultComboBoxModel(WORK_AREA_PPCHASSIS));
                        break;
                    default:
                        break;
                }
            }
        });
        
        
        JTextArea workGroupText = new JTextArea("Work Group:");
        formatTextField(workGroupText, false, Color.WHITE, f);
        
        workGroupSelection.setFont(f);
        workGroupSelection.setPreferredSize(departmentSelection.getPreferredSize());
        Font checkBoxFont = new Font("Times", Font.PLAIN, 40);
        
        //Declare which jobs appear depending on work group selected
        workGroupSelection.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String itemSelected = (String)workGroupSelection.getSelectedItem();
                switch (itemSelected) {
                    case " ":
                        jobsPossibleContainer.removeAll();
                        jobsPossibleContainer.repaint();
                        break;
                    case "Docks":
                        jobsPossibleContainer.removeAll();
                        for(String job : DOCK_JOBS)
                        {
                            if(!selectedJobNames.contains(job))
                            {
                                JCheckBox jobsBox = new JCheckBox(job);
                                formatCheckBox(jobsBox, checkBoxFont);
                                jobsPossibleContainer.add(jobsBox);
                                checkBoxes.add(jobsBox);
                            }
                        }
                        
                        jobsPossibleContainer.revalidate();
                        jobsPossibleContainer.repaint();
                        break;
                    case "PrePaint Chassis":
                        jobsPossibleContainer.removeAll();
                        for(String job : PPCHASSIS_JOBS)
                        {
                            JCheckBox jobsBox = new JCheckBox(job);
                            formatCheckBox(jobsBox, checkBoxFont);
                            jobsPossibleContainer.add(jobsBox);
                            checkBoxes.add(jobsBox);
                        }
                        
                        jobsPossibleContainer.revalidate();
                        jobsPossibleContainer.repaint();
                        break;
                    case "Engine Line":
                        jobsPossibleContainer.removeAll();
                        for(String job : ENGINE_LINE_JOBS)
                        {
                            JCheckBox jobsBox = new JCheckBox(job);
                            formatCheckBox(jobsBox, checkBoxFont);
                            jobsPossibleContainer.add(jobsBox);
                            checkBoxes.add(jobsBox);
                        }
                        
                        jobsPossibleContainer.revalidate();
                        jobsPossibleContainer.repaint();
                        break;
                    default:
                        break;
                }
            }
        });
        
        JTextArea jobTypeText = new JTextArea("Job Type:");
        formatTextField(jobTypeText, false, Color.WHITE, f);
        
        jobTypeSelection.setFont(f);
        jobTypeSelection.setPreferredSize(departmentSelection.getPreferredSize());
        
        //Create Panel to display the possible jobs, buttons to add/remove, and selected jobs
        JPanel jobsPanel = new JPanel();
        GridBagLayout jobsPanelLayout = new GridBagLayout();
        jobsPanel.setLayout(jobsPanelLayout);
        
        jobsPossibleContainer.setLayout(new BoxLayout(jobsPossibleContainer, BoxLayout.Y_AXIS));
        
        //Create headers for selection boxes
        Font jobHeaderFont = new Font("Times", Font.BOLD, 30);
        JTextArea jobsPossibleText = new JTextArea("Positions in Area");
        formatTextField(jobsPossibleText, false, Color.WHITE, jobHeaderFont);
        
        JTextArea jobsSelectedText = new JTextArea("Qualified Positions");
        formatTextField(jobsSelectedText, false, Color.WHITE, jobHeaderFont);
        
        //Make possible jobs container scrollable and set preferred size 
        JScrollPane jobsPossiblePane = new JScrollPane(jobsPossibleContainer);        
        jobsPossiblePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jobsPossiblePane.setPreferredSize(new Dimension(500, 400));
        jobsPossibleContainer.setPreferredSize(new Dimension(200, 1100));
        
        jobsSelectedContainer.setLayout(new BoxLayout(jobsSelectedContainer, BoxLayout.Y_AXIS));
        
        //Make job selected container scrollable and set preferred size
        JScrollPane jobsSelectedPane = new JScrollPane(jobsSelectedContainer);
        jobsSelectedPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jobsSelectedPane.setPreferredSize(jobsPossiblePane.getPreferredSize());
        jobsSelectedContainer.setPreferredSize(jobsPossibleContainer.getPreferredSize());
        
        //Add all components to array to repaint and revalidate when needed 
        JComponent[] componentsToRefresh = {jobsPossiblePane, jobsSelectedPane, 
            jobsPossibleContainer, jobsSelectedContainer};
        
        //Create button to add job(s) to selected jobs container
        JButton addButton = new JButton(">>>");
        addButton.setFont(f);
        //Button will erase job selected from possible job and add to selected jobs
        addButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                //Check which jobs are selected
                for (JCheckBox currentBox : checkBoxes) 
                {
                    if(currentBox.isSelected())
                    {
                        selectedJobNames.add(currentBox.getText());
                        
                        //Condition to ensure user does not click on jobs selected and press add
                        if(!currentBox.getText().contains("<Training>") &&
                                !currentBox.getText().contains("<Primary>") && 
                                !currentBox.getText().contains("<Secondary>"))
                        {
                            currentBox.setText(currentBox.getText() + "<" + jobTypeSelection.getSelectedItem().toString() + ">");
                            selectedJobNamesWithJobType.add(currentBox.getText());
                        }
                        
                        jobSelectedToAdd.add(currentBox);
                        jobsPossibleContainer.remove(currentBox);
                    }
                }
                
                //Condition that checks if the user checked jobs to add
                if(!jobSelectedToAdd.isEmpty())
                {
                    //Loop to add all jobs to selected jobs container
                    while(!jobSelectedToAdd.isEmpty())
                    {
                        jobSelectedToAdd.get(0).setSelected(false);
                        jobsSelectedContainer.add(jobSelectedToAdd.remove(0));
                    }
                    
                    refreshComponents(componentsToRefresh);
                }
            }
        });
        
        //Create remove button and remove job(s) from selected jobs container
        JButton removeButton = new JButton("<<<");
        removeButton.setFont(f);
        //Button will erase job from selected jobs container and add back to possible jobs
        removeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                //Check if boxes are selected and remove 
                for (JCheckBox currentBox : checkBoxes) 
                {
                    if(currentBox.isSelected())
                    {
                        jobSelectedToRemove.add(currentBox);
                        selectedJobNamesWithJobType.remove(currentBox.getText());
                        String jobToRemoveName = currentBox.getText();
                        
                        if(selectedJobNames.contains(jobToRemoveName))
                        {
                            selectedJobNames.remove(jobToRemoveName);    
                        }
                        
                        jobsSelectedContainer.remove(currentBox);
                    }
                }
                
                //Condition to delete the job type specification from the job string
                if(!jobSelectedToRemove.isEmpty())
                {
                    removeJobTypeFromJob(selectedJobNames, jobsPossibleContainer, workGroupSelection);
                    refreshComponents(componentsToRefresh);
                }
            }
        });
        
        //Create panel to hold buttons and add to panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));
        
        JTextField emptySpace = new JTextField();
        emptySpace.setVisible(false);
        
        buttonPanel.add(addButton, 0, 0);
        buttonPanel.add(emptySpace, 0, 1);
        buttonPanel.add(removeButton, 0, 2);
        
        //Add headers and three containers to the jobsPanel container using convenience method for gridBag layout
        gbc.anchor = GridBagConstraints.CENTER;
        addObjects(jobsPossibleText, jobsPanel, jobsPanelLayout, gbc, 0, 0, 1, 1, 1, 1);
        addObjects(jobsSelectedText, jobsPanel, jobsPanelLayout, gbc, 2, 0, 1, 1, 1, 1);
        addObjects(jobsPossiblePane, jobsPanel, jobsPanelLayout, gbc, 0, 1, 1, 1, 1, 1);
        addObjects(buttonPanel, jobsPanel, jobsPanelLayout, gbc, 1, 1, 1, 1, 1, 1);
        addObjects(jobsSelectedPane, jobsPanel, jobsPanelLayout, gbc, 2, 1, 1, 1, 1, 1);
        
        jobsPanel.setBackground(Color.DARK_GRAY);
        
        JButton continueButton = new JButton("Save Changes");
        continueButton.setFont(f);
        
        // Save info and go to next tab when clicked
        continueButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                boolean canProceed = validator.validateJobs(selectedJobNamesWithJobType);
                if(canProceed)
                {
                    if(errorMessagePresent)
                    {
                        newEmployeeJobPanel.remove(8);
                        errorMessagePresent = false;
                        newEmployeeJobPanel.repaint();
                        newEmployeeJobPanel.revalidate();
                    }
                    employeeJobsList.clear();
                    storeEmployeeJobs(selectedJobNamesWithJobType);
                    employeeTabPane.setSelectedIndex(2);
                    employeeTabPane.setEnabledAt(2, true);
                }
                else
                {
                    if(errorMessagePresent)
                    {
                        newEmployeeJobPanel.remove(8);
                    }
                    JTextArea errorMessage = new JTextArea("-Please review jobs. At least 1 job required; only one primary job permitted");
                    Font errorFont = new Font("Times", Font.BOLD, 30);
                    formatTextField(errorMessage, false, Color.RED, errorFont);
                    gbc.anchor = GridBagConstraints.WEST;
                    addObjects(errorMessage, newEmployeeJobPanel, layout, gbc, 0, 0, 7, 1, 1, 1);
                    newEmployeeJobPanel.repaint();
                    newEmployeeJobPanel.revalidate();
                    errorMessagePresent = true;
                    employeeTabPane.setEnabledAt(3, false);
                }
            }
        });
        
        //Add all components to the main Panel using convenience method for gridBag layout
        addObjects(departmentText, newEmployeeJobPanel, layout, gbc, 0, 1, 1, 1, 0.1, 0.1);
        addObjects(departmentSelection, newEmployeeJobPanel, layout, gbc, 1, 1, 1, 1, 0.1, 0.1);
        addObjects(workGroupText, newEmployeeJobPanel, layout, gbc, 2, 1, 1, 1, 0.1, 0.1);
        addObjects(workGroupSelection, newEmployeeJobPanel, layout, gbc, 3, 1, 1, 1, 0.1, 0.1);
        addObjects(jobTypeText, newEmployeeJobPanel, layout, gbc, 4, 1, 1, 1, 0.1, 0.1);
        addObjects(jobTypeSelection, newEmployeeJobPanel, layout, gbc, 5, 1, 1, 1, 0.1, 0.1);
        gbc.anchor = GridBagConstraints.CENTER;
        addObjects(jobsPanel, newEmployeeJobPanel, layout, gbc, 0, 2, 8, 1, 1, 1);
        gbc.anchor = GridBagConstraints.EAST;
        addObjects(continueButton, newEmployeeJobPanel, layout, gbc, 6, 3, 1, 1, 1, 1);
          
        return newEmployeeJobPanel;
    }
    
    /**
     * Method for creating components shown on Notes tab
     * 
     * @return JPanel containing all components to be shown on tab
     */
    private static JPanel createNewEmployeeNotesTab()
    {
        //Create Panel to hold components and set layout
        JPanel newEmployeeNotesPanel = new JPanel();
        
        GridBagLayout layout = new GridBagLayout();
        newEmployeeNotesPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        
        newEmployeeNotesPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        newEmployeeNotesPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        //Begin creating components to add
        JTextArea notesText = new JTextArea("Notes:");
        formatTextField(notesText, false, Color.WHITE, f);
        
        JTextArea notesInput = new JTextArea("N/A", 15, 40);
        notesInput.setFont(f);
        notesInput.setLineWrap(true);
        notesInput.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(notesInput);
        
        JButton continueButton = new JButton("Save Changes");
        continueButton.setFont(f);
        
        //Go to next tab when clicked
        continueButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                boolean canProceed = validator.validateNotes(notesInput.getText());
                String notesConcatenated = notesInput.getText();
                if(notesConcatenated.contains("\n"))
                {
                    notesConcatenated = notesInput.getText().replaceAll("\n", "~`");
                }
                if(canProceed)
                {
                    updateNotes(notesConcatenated);
                }
                else
                {
                    updateNotes("N/A");
                }
                employeeTabPane.setSelectedIndex(3);
                employeeTabPane.setEnabledAt(3, true);
            }
        });
        
        //Add components using convenience method for gridBag layout
        gbc.anchor = GridBagConstraints.NORTHWEST;
        addObjects(notesText, newEmployeeNotesPanel, layout, gbc, 0, 0, 1, 1, .1, .1);
        gbc.anchor = GridBagConstraints.NORTH;
        addObjects(scrollPane, newEmployeeNotesPanel, layout, gbc, 1, 0, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.EAST;
        addObjects(continueButton, newEmployeeNotesPanel, layout, gbc, 1, 1, 1, 1, 1, 1);
        
        return newEmployeeNotesPanel;
    }
    
    /**
     * Method to create all components shown on save tab
     * 
     * @return JPanel containing all components for save tab
     */
    private static JPanel createNewEmployeeSaveTab()
    {
        //Create Panel to hold components and set layout
        JPanel newEmployeeSavePanel = new JPanel();
        
        newEmployeeSavePanel.setLayout(new GridLayout(2,1));
        newEmployeeSavePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        newEmployeeSavePanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        //Begin creating components to be shown
        JButton saveButton = new JButton("Save Employee");
        saveButton.setFont(f);
        newEmployeeSavePanel.add(saveButton);
           
        saveButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String employeeLastName = employeeInfo.get(0);
                String employeeFirstName = employeeInfo.get(1);
                String employeeId = employeeInfo.get(2);
                String seniorityDate = employeeInfo.get(3);
                String gradePay = employeeInfo.get(4);
                ArrayList<String> jobs = employeeJobsList;
                String notes = employeeNotes;
                newEmployee = new Employee(employeeLastName, employeeFirstName, 
                        employeeId, seniorityDate, gradePay,
                        jobs, notes);
                
                try 
                {
                    saveEmployeeToFile(newEmployee);  
                } 
                catch (IOException ex) 
                {
                    //Handle Later
                }
                addEmployeeNameToConfirmationScreen(newEmployee);
                clearAllInputFields();
                existingEmp.updateEmployeeLogAndComboBox(newEmployee, "update");
                employeeTabPane.setSelectedIndex(0);
                employeeTabPane.setVisible(false);
                newEmployeeConfirmationScreen.setVisible(true);
            }
        });
        
        JButton clearButton = new JButton("Clear Employee Data");
        clearButton.setFont(f);
        newEmployeeSavePanel.add(clearButton);
        
        clearButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                clearAllInputFields();
            }
        });
                
        return newEmployeeSavePanel;   
    }
    
    /**
     * Method to create all components shown on save tab
     * 
     * @return JPanel containing all components for save tab
     */
    private static JPanel createNewEmployeeOptionTab()
    {
        //Create Panel to hold components and set layout
        JPanel newEmployeeOptionPanel = new JPanel();
        
        newEmployeeOptionPanel.setLayout(new GridLayout(2,1));
        newEmployeeOptionPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        newEmployeeOptionPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        //Begin creating components to be shown
        JButton printButton = new JButton("Print Employee Matrix");
        printButton.setFont(f);
        newEmployeeOptionPanel.add(printButton);
        
        //Export jobs to printable file when clicked
        printButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                File employeeMatrixFile = new File("EmployeeJobMatrix.txt");
                try(FileWriter fw = new FileWriter("EmployeeJobMatrix.txt", false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
                {
                    if(employeeInfo.get(2) != null)
                    {
                        out.println(employeeInfo.get(0) + " " + employeeInfo.get(1) 
                            + " " + employeeInfo.get(2));
                    }
                    if(employeeJobsList != null)
                    {
                        for(String job : employeeJobsList)
                        {
                            out.println(job);
                        }
                    }
                }
                catch(IOException e)
                {
                    System.out.println("Error Creating File");
                }
                try 
                {
                    PrintTextFile print = new PrintTextFile("EmployeeJobMatrix.txt");
                    print.printFile();
                } 
                catch (PrintException ex) 
                {
                    Logger.getLogger(ManpowerAllocator.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(ManpowerAllocator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PrinterException ex) {
                    Logger.getLogger(ManpowerAllocator.class.getName()).log(Level.SEVERE, null, ex);
                } 
                
                
            }
        });
        
        JButton mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.setFont(f);
        newEmployeeOptionPanel.add(mainMenuButton);
        
        //Return to main menu when clicked
        mainMenuButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                employeeTabPane.setSelectedIndex(0);
                employeeTabPane.setVisible(false);
                welcomeScreenPanels[0].setVisible(true);
                welcomeScreenPanels[1].setVisible(true);
            }
        });
        
        return newEmployeeOptionPanel;
    }
    
    private static JPanel createNewEmployeeConfirmationScreen()
    {
        newEmployeeConfirmationPanel = new JPanel();
        newEmployeeConfirmationPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 80, 50));
        GridBagLayout layout = new GridBagLayout();
        newEmployeeConfirmationPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        newEmployeeConfirmationPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        JTextArea newEmployeeConfirmationText = new JTextArea(" successfully added.");
        formatTextField(newEmployeeConfirmationText, false, Color.GREEN, f);
        
        JButton nextNewEmployeeButton = new JButton("Add Another Employee");
        nextNewEmployeeButton.setFont(f);
        nextNewEmployeeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                newEmployeeConfirmationScreen.setVisible(false);
                clearAllInputFields();
                newEmployee = null;
                employeeTabPane.setSelectedIndex(0);
                employeeTabPane.setEnabledAt(1, false);
                employeeTabPane.setEnabledAt(2, false);
                employeeTabPane.setEnabledAt(3, false);
                employeeTabPane.setVisible(true);
                newEmployeeConfirmationScreen.remove(3);
            }
        
        });
        
        JButton mainMenuButton = new JButton(" Return to Main Menu ");
        mainMenuButton.setFont(f);
        mainMenuButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                clearAllInputFields();
                newEmployeeConfirmationScreen.setVisible(false);
                welcomeScreenPanels[0].setVisible(true);
                welcomeScreenPanels[1].setVisible(true);
            }
        
        });
        
        gbc.anchor = GridBagConstraints.LINE_START;
        addObjects(newEmployeeConfirmationText, newEmployeeConfirmationPanel, layout, gbc, 1, 0, 1, 1, 1, 1);
        gbc.ipadx = 0;
        gbc.ipady = 160;
        addObjects(nextNewEmployeeButton, newEmployeeConfirmationPanel, layout, gbc, 1, 1, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0,0,0,20);
        addObjects(mainMenuButton, newEmployeeConfirmationPanel, layout, gbc, 0, 1, 1, 1, 1, 1);
        return newEmployeeConfirmationPanel;
    }
    
    
    
    private static void addEmployeeNameToConfirmationScreen(Employee newEmployee)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        
        Font f = new Font("Times", Font.BOLD, 40);
        
        JTextArea newEmployeeName = new JTextArea(newEmployee.getLastName() + ", " + newEmployee.getFirstName());
        formatTextField(newEmployeeName, false, Color.WHITE, f);
        
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0,0,0,20);
        gbc.ipadx = 30;

        addObjects(newEmployeeName, newEmployeeConfirmationPanel, layout, gbc, 0, 0, 1, 1, 1, 1);
        newEmployeeConfirmationPanel.repaint();
        newEmployeeConfirmationPanel.revalidate();
    }
    
    private static void storeEmployeeInfo(String[] infoToAdd)
    {
        employeeInfo.addAll(Arrays.asList(infoToAdd));
    }
    
    private static void storeEmployeeJobs(ArrayList<String> jobsToStore)
    {
        for(String job : jobsToStore)
        {
            employeeJobsList.add(job);
        }
    }
    
    private static void updateNotes(String note)
    {
        employeeNotes = note;
    }
    
    private static void clearAllInputFields()
    {
        newEmployeeInfoTab = createNewEmployeeInfoTab();
        newEmployeeJobTab = createNewEmployeeJobTab();
        newEmployeeNotesTab = createNewEmployeeNotesTab();
        employeeInfo.clear();
        employeeJobsList.clear();
        employeeTabPane.removeAll();
        employeeTabPane.addTab("INFO", newEmployeeInfoTab);
        employeeTabPane.addTab("JOBS", newEmployeeJobTab);
        employeeTabPane.addTab("NOTES", newEmployeeNotesTab);
        employeeTabPane.addTab("SAVE", newEmployeeSaveTab);
        employeeTabPane.addTab("OPTIONS", newEmployeeOptionTab);
    }
    
    public static void updateExistingEmployeeTabPane()
    {
        existingEmployeeTabPane = existingEmp.getExistingEmployeeTab(existingEmployeeScreen);
        existingEmployeeTabPane.repaint();
        existingEmployeeTabPane.revalidate();
    }
    
    private static void removeJobTypeFromJob(ArrayList<String> selectedJobNames, 
            JList jobsPossibleContainer, JComboBox workGroupSelection)
    {
        while(!jobSelectedToRemove.isEmpty())
        {
            jobSelectedToRemove.get(0).setSelected(false);
                       
            if(jobSelectedToRemove.get(0).getText().contains("<Training>"))
            {
                String jobMinusJobType = jobSelectedToRemove.get(0).getText().replace("<Training>", "");
                jobSelectedToRemove.get(0).setText(jobMinusJobType);
            }
            else if(jobSelectedToRemove.get(0).getText().contains("<Primary>"))
            {
                String jobMinusJobType = jobSelectedToRemove.get(0).getText().replace("<Primary>", "");
                jobSelectedToRemove.get(0).setText(jobMinusJobType);
            }
            else if(jobSelectedToRemove.get(0).getText().contains("<Secondary>"))
            {
                String jobMinusJobType = jobSelectedToRemove.get(0).getText().replace("<Secondary>", "");
                jobSelectedToRemove.get(0).setText(jobMinusJobType);
            }
                        
            if(DOCK_JOBS.contains(jobSelectedToRemove.get(0).getText()) && ((String)workGroupSelection.getSelectedItem()).equals("Docks"))
            {
                selectedJobNames.remove(jobSelectedToRemove.get(0).getText());
                jobsPossibleContainer.add(jobSelectedToRemove.remove(0));
            }
            else if(PPCHASSIS_JOBS.contains(jobSelectedToRemove.get(0).getText()) && ((String)workGroupSelection.getSelectedItem()).equals("PrePaint Chassis"))
            {
                selectedJobNames.remove(jobSelectedToRemove.get(0).getText());
                jobsPossibleContainer.add(jobSelectedToRemove.remove(0));
            }
            else if(ENGINE_LINE_JOBS.contains(jobSelectedToRemove.get(0).getText()) && ((String)workGroupSelection.getSelectedItem()).equals("Engine Line"))
            {
                selectedJobNames.remove(jobSelectedToRemove.get(0).getText());
                jobsPossibleContainer.add(jobSelectedToRemove.remove(0));
            }
            else
            {
                selectedJobNames.remove(jobSelectedToRemove.get(0).getText());
                jobSelectedToRemove.remove(0);                
            }
        }
    }
    
    private static void addObjects(Component componente, Container yourcontainer, GridBagLayout layout, GridBagConstraints gbc, int x, int y, int gridwidth, int gridheight, double gridWeightX, double gridWeightY)
    {
        gbc.gridx = x;
        gbc.gridy = y;

        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        
        gbc.weightx = gridWeightX;
        gbc.weighty = gridWeightY;

        yourcontainer.add(componente, gbc);
        
    }
    
    public static void formatTextField(JTextComponent textFieldToFormat, boolean isEditable, Color fontColor, Font f)
    {
        textFieldToFormat.setBackground(Color.DARK_GRAY);
        textFieldToFormat.setForeground(fontColor);
        textFieldToFormat.setEditable(isEditable);
        textFieldToFormat.setFont(f);
    }
    
    private static void formatCheckBox(JCheckBox checkBoxToFormat, Font f)
    {
        checkBoxToFormat.setBackground(Color.WHITE);
        checkBoxToFormat.setForeground(Color.BLACK);
        checkBoxToFormat.setFont(f);
    }
    
    private static void saveEmployeeToFile(Employee employeeToSave) throws IOException
    {
        try(FileWriter fw = new FileWriter("EmployeeFile.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(employeeToSave.toString());
        }
        catch(IOException e)
        {
            System.out.println("Error Creating File");
        }
        
    }
    
    private static void refreshComponents(JComponent[] componentsToRefresh)
    {
        for(JComponent currentComponent : componentsToRefresh)
        {
            currentComponent.revalidate();
            currentComponent.repaint();
        }
    }
    
    public static void returnToMainMenu()
    {
        welcomeScreenPanels[0].setVisible(true);
        welcomeScreenPanels[1].setVisible(true);
    }
    
    
    
    private static void addAndSortJobs()
    {
        DOCK_JOBS.add("Dock M/H 1");
        DOCK_JOBS.add("Dock M/H 2");
        DOCK_JOBS.add("Dock M/H 3");
        DOCK_JOBS.add("Dock M/H 4");
        DOCK_JOBS.add("Dock M/H 5");
        DOCK_JOBS.add("Dock M/H 6");
        DOCK_JOBS.add("Dock M/H 7");
        DOCK_JOBS.add("Dock Returns");
        DOCK_JOBS.add("Refurb");
        DOCK_JOBS.add("Audit 1");
        DOCK_JOBS.add("Dispo");
        DOCK_JOBS.add("Shipping 1");
        DOCK_JOBS.add("Western Star PCH");
        DOCK_JOBS.add("Western Star Cab");
        DOCK_JOBS.add("Harness Kitting 1");
        DOCK_JOBS.add("Harness Kitting 2");
        DOCK_JOBS.add("Dock M/H 12");
        DOCK_JOBS.add("Refurb (Store Operator)");
        Collections.sort(DOCK_JOBS);
        
        PPCHASSIS_JOBS.add(" ");
        Collections.sort(PPCHASSIS_JOBS);
        
        ENGINE_LINE_JOBS.add(" ");
        Collections.sort(ENGINE_LINE_JOBS);
    }
    
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            { 
                new ManpowerAllocator().setVisible(true);
            }
        });
    }
    
    private static void getDepartments()
    {
        ArrayList<String> departmentFinder = new ArrayList<>();
        String fileName = "DepartmentsFile.txt";
        String line = null;
        
        if(!departmentFinder.isEmpty())
        {
            departmentFinder.clear();
        }
        
        try 
        {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            while((line = br.readLine()) != null)
            {
                departmentFinder.add(line);
            }
            
            br.close();
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("Unable to open file" + fileName);
        }
        catch(IOException ex)
        {
            System.out.println("Error reading file");
        }
        
        DEPARTMENTS = new String[departmentFinder.size() + 1];
        DEPARTMENTS[0] = " ";
        int index = 1;
        for(String dep : departmentFinder)
        {
            DEPARTMENTS[index] = dep;
            index++;
        }
        
    }
    
    
}
