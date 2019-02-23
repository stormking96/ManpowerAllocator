package manpowerallocator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.print.PrintException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class ExistingEmployee 
{
    private static JFrame mainJFrame;
    private static ArrayList<String> employeeData = new ArrayList<>();
    
    private static String employeeDataSelected;
    private static int lineToOverwrite;
    private static String employeeDataSelectedLastName;
    private static String employeeDataSelectedFirstName;
    private static String employeeDataSelectedEmployeeID;
    private static String employeeDataSelectedSeniority;
    private static String employeeDataSelectedGrade;
    private static ArrayList<String> employeeDataSelectedJobs;
    private static String employeeDataSelectedNotes;
    
    private static JPanel existingEmployeeSearchScreen;
    private static JComboBox existingEmployeeBox;
    private static ArrayList<String> employeeInfo;
    
    private static JTabbedPane employeeTabPane;
    private static JPanel existingEmployeeInfoTab;
    private static JPanel existingEmployeeJobTab;
    private static JPanel existingEmployeeNotesTab;
    private static JPanel existingEmployeeSaveTab;
    private static JPanel confirmationScreen;
    private static JPanel existingEmployeeOptionTab;
    
    private static String[] DEPARTMENTS;
    private static final String[] WORK_AREA_MATERIALS = {" ", "Docks", "PrePaint Chassis"};
    private static final String[] WORK_AREA_PPCHASSIS = {" ", "Engine Line"};
    private static final ArrayList<String> DOCK_JOBS = new ArrayList<>();
    private static final ArrayList<String> PPCHASSIS_JOBS = new ArrayList<>();
    private static final ArrayList<String> ENGINE_LINE_JOBS = new ArrayList<>();
    private static final String[] JOB_TYPES = {"Training", "Primary", "Secondary"};
    
    private static ArrayList<JCheckBox> checkBoxes;
    private static ArrayList<JCheckBox> jobSelectedToAdd;
    private static ArrayList<JCheckBox> jobSelectedToRemove;
    
    private static ArrayList<String> employeeJobsList;
    private static String employeeNotes;
    
    private static Employee newEmployee;
    private static ExistingEmployee existingEmp;
    
    private static JPanel existingEmployeeConfirmationPanel;
    private static JPanel existingEmployeeConfirmationScreen;
    private static JPanel previousScreen;
    
    
    private static InputValidator validator;
    private static boolean errorMessagePresent = false;
    
    public ExistingEmployee()
    {
        validator = new InputValidator();
        employeeTabPane = new JTabbedPane();
        
        
        
        employeeInfo = new ArrayList<>();
        checkBoxes = new ArrayList<>();
        jobSelectedToAdd = new ArrayList<>();
        jobSelectedToRemove = new ArrayList<>();
        employeeJobsList = new ArrayList<>();
        employeeDataSelected = null;
        addAndSortJobs();
    }
    
    public JTabbedPane getExistingEmployeeTab(JPanel screenToHide)
    {
        previousScreen = screenToHide;
        return employeeTabPane;
    }
    
    
    public JPanel createExistingEmployeeSearchScreen()
    {
        existingEmployeeSearchScreen = new JPanel();
        
        existingEmployeeSearchScreen.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        GridBagLayout layout = new GridBagLayout();
        existingEmployeeSearchScreen.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        Font titleFont = new Font("Times", Font.PLAIN, 60);
        JTextArea employeeSearchText = new JTextArea("Select Employee To Edit: ");
        formatTextField(employeeSearchText, false, Color.WHITE, titleFont);
        
        ArrayList<String> employeeNameList = getEmployeeNames();
        existingEmployeeBox = new JComboBox();
        for(String employeeName : employeeNameList)
        {
            existingEmployeeBox.addItem(employeeName);
        }
        
        existingEmployeeBox.setFont(f);
        AutoCompleteDecorator.decorate(existingEmployeeBox);
        existingEmployeeBox.setMaximumRowCount(4);
        existingEmployeeBox.setPreferredSize(new Dimension(
                existingEmployeeBox.getPreferredSize().width + 300, 
                existingEmployeeBox.getPreferredSize().height));
        
        JButton searchButton = new JButton("Search");
        
        searchButton.setFont(f);
        existingEmployeeSearchScreen.add(searchButton);
        
        //Search for employee when clicked
        searchButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String employeeSelected = (String)existingEmployeeBox.getSelectedItem();
                ArrayList<String> tokens = new ArrayList<>();
                StringTokenizer st = new StringTokenizer(employeeSelected, " ");
                while(st.hasMoreTokens())
                {
                    tokens.add(st.nextToken());
                }
                
                String employeeIDSelected = tokens.get(2);
                employeeIDSelected = employeeIDSelected.replace("(", "");
                employeeIDSelected = employeeIDSelected.replace(")", "");
                
                int index = 0;
                loadEmployees();
                for(String lineOfEmployeeData : employeeData)
                {
                    if(lineOfEmployeeData.contains(employeeIDSelected))
                    {
                        employeeDataSelected = lineOfEmployeeData;
                        lineToOverwrite = index;
                        break;
                    }
                    index++;
                }
                
                setEmployeeDataSelectedFields();
                String[] employeeSelectedInfo = {employeeDataSelectedEmployeeID, 
                    employeeDataSelectedLastName, employeeDataSelectedFirstName, 
                    employeeDataSelectedSeniority, employeeDataSelectedGrade};
                storeEmployeeInfo(employeeSelectedInfo);
                
                updateNotes(employeeDataSelectedNotes);
                storeEmployeeJobs(employeeDataSelectedJobs);
                employeeTabPane.removeAll();
                
                existingEmployeeInfoTab = createExistingEmployeeInfoTab();
                employeeTabPane.setFont(new Font( "Times New Roman", Font.PLAIN, 40));
                employeeTabPane.addTab("INFO", existingEmployeeInfoTab);

                existingEmployeeJobTab = createNewEmployeeJobTab();
                employeeTabPane.addTab("JOBS", existingEmployeeJobTab);
                

                existingEmployeeNotesTab = createNewEmployeeNotesTab();
                employeeTabPane.addTab("NOTES", existingEmployeeNotesTab);

                JPanel[] panelsFromSave = createNewEmployeeSaveTab();
                existingEmployeeConfirmationScreen = panelsFromSave[1];
                mainJFrame.add(existingEmployeeConfirmationScreen);
                
                existingEmployeeSaveTab = panelsFromSave[0];
                employeeTabPane.addTab("SAVE", existingEmployeeSaveTab);

                existingEmployeeOptionTab = createNewEmployeeOptionTab();
                employeeTabPane.addTab("OPTIONS", existingEmployeeOptionTab);
                
                employeeTabPane.repaint();
                employeeTabPane.revalidate();
                
                ManpowerAllocator.updateExistingEmployeeTabPane();
                
                mainJFrame.repaint();
                mainJFrame.revalidate();
                
                previousScreen.setVisible(false);
                employeeTabPane.setVisible(true);
                existingEmployeeConfirmationScreen.setVisible(false);
            }
        });
        
        gbc.anchor = GridBagConstraints.CENTER;
        addObjects(employeeSearchText, existingEmployeeSearchScreen, layout, gbc, 1, 0, 5, 1, 1, 1);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0,500,0,0);
        addObjects(existingEmployeeBox, existingEmployeeSearchScreen, layout, gbc, 1, 1, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(0,0,0,400);
        addObjects(searchButton, existingEmployeeSearchScreen, layout, gbc, 2, 1, 1, 1, 1, 1);
        
        
        return existingEmployeeSearchScreen;
    }
    
    public static JPanel createExistingEmployeeTabbedScreen()
    {
        return new JPanel();
    }
    
     /**
     * Method to create the new employee info tab and add all components
     * 
     * @return JPanel containing all components to be shown on tab
     */
    private static JPanel createExistingEmployeeInfoTab()
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
        
        JTextField lastNameBox = new JTextField(employeeDataSelectedLastName, 12);
        lastNameBox.setFont(f);
        
        JTextField firstNameBox = new JTextField(employeeDataSelectedFirstName, 12);
        firstNameBox.setFont(f);
        
        JTextArea employeeIDText = new JTextArea("Employee ID:");
        formatTextField(employeeIDText, false, Color.WHITE, f);
        
        JTextField employeeIDInput = new JTextField(employeeDataSelectedEmployeeID, 4);
        employeeIDInput.setFont(f);
        
        JTextArea seniorityDateText = new JTextArea("Seniority:");
        formatTextField(seniorityDateText, false, Color.WHITE, f);
        
        JTextField seniorityDateInput = new JTextField(employeeDataSelectedSeniority);
        seniorityDateInput.setFont(f);
   
        JTextArea gradeText = new JTextArea("Grade 5/6?");
        formatTextField(gradeText, false, Color.WHITE, f);
        
        JComboBox gradePaySelection = new JComboBox();
        gradePaySelection.addItem("No");
        gradePaySelection.addItem("Utility");
        gradePaySelection.addItem("Complexity");
        gradePaySelection.addItem("Team Leader");
        gradePaySelection.setFont(f);
        gradePaySelection.setSelectedItem(employeeDataSelectedGrade);
        
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
                                JTextArea errorMessage = new JTextArea("-Please check date input (MM/DD/YYYY) Numbers only");
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
                    String[] employeeNewInfo = {employeeIDInput.getText(), lastNameBox.getText(), 
                        firstNameBox.getText(), seniorityDateInput.getText(), 
                        (String)gradePaySelection.getSelectedItem()};
                    employeeInfo.clear();
                    storeEmployeeInfo(employeeNewInfo);
                    employeeTabPane.setSelectedIndex(1);
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
        Font checkBoxFont = new Font("Times", Font.PLAIN, 40);
        
        //Begin creating all components to be added 
        JTextArea departmentText = new JTextArea("Department:");
        formatTextField(departmentText, false, Color.WHITE, f);
        
        //Components to be variable depending on what user clicks
        JComboBox workGroupSelection = new JComboBox();
        JList jobsPossibleContainer = new JList();
        JList jobsSelectedContainer = new JList();
        ArrayList<String> selectedJobNames = new ArrayList<>();
        ArrayList<String> selectedJobNamesWithJobType = new ArrayList<>();
        for(String jobWithJobType : employeeDataSelectedJobs)
        {
            selectedJobNamesWithJobType.add(jobWithJobType);
            JCheckBox jobBox = new JCheckBox(jobWithJobType);
            formatCheckBox(jobBox, checkBoxFont);
            checkBoxes.add(jobBox);
            jobsSelectedContainer.add(jobBox);
        }
        ArrayList<String> jobsToLoad = (ArrayList<String>)removeJobTypeFromJob(employeeDataSelectedJobs).clone();
        for(String job : jobsToLoad)
        {
            selectedJobNames.add(job);
        }
        
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
                if(validator.validateJobs(selectedJobNamesWithJobType))
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
                    employeeTabPane.setEnabledAt(3, true);
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
        
        String notesDataToInput = employeeDataSelectedNotes.replaceAll("~`", "\n");
        JTextArea notesInput = new JTextArea(notesDataToInput, 15, 40);
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
    private static JPanel[] createNewEmployeeSaveTab()
    {
        //Create Panel to hold components and set layout
        JPanel newEmployeeSavePanel = new JPanel();
        existingEmployeeConfirmationScreen = createExistingEmployeeConfirmationScreen("update");
        
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
                String employeeId = employeeInfo.get(0);
                String employeeLastName = employeeInfo.get(1);
                String employeeFirstName = employeeInfo.get(2);
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
                updateEmployeeLogAndComboBox(newEmployee, "update");
                employeeTabPane.setVisible(false);
                existingEmployeeConfirmationScreen.setVisible(true);
            }
        });
        
        JButton clearButton = new JButton("Remove Employee");
        clearButton.setFont(f);
        newEmployeeSavePanel.add(clearButton);
        
        clearButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String employeeId = employeeInfo.get(0);
                String employeeLastName = employeeInfo.get(1);
                String employeeFirstName = employeeInfo.get(2);
                String seniorityDate = employeeInfo.get(3);
                String gradePay = employeeInfo.get(4);
                ArrayList<String> jobs = employeeJobsList;
                String notes = employeeNotes;
                newEmployee = new Employee(employeeLastName, employeeFirstName, 
                        employeeId, seniorityDate, gradePay,
                        jobs, notes);
                int confirmationButton = JOptionPane.YES_NO_OPTION;
                int confirmationResult = JOptionPane.showConfirmDialog (null, 
                        "Are you sure you want to delete this employee?",
                        "Warning",confirmationButton);
                if(confirmationResult == JOptionPane.YES_OPTION)
                {
                    deleteEmployeeFromFile(employeeInfo);
                    addEmployeeNameToConfirmationScreen(newEmployee);
                    clearAllInputFields();
                    updateEmployeeLogAndComboBox(newEmployee, "delete");
                    employeeTabPane.setVisible(false);
                    existingEmployeeConfirmationScreen.setVisible(true);
                }
            }
        });
         
        JPanel[] panels = {newEmployeeSavePanel, existingEmployeeConfirmationScreen};
        return panels;   
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
                ManpowerAllocator.returnToMainMenu();
            }
        });
        
        return newEmployeeOptionPanel;
    }
    
    public static JPanel createExistingEmployeeConfirmationScreen(String updateOrDelete)
    {
        existingEmployeeConfirmationPanel = new JPanel();
        existingEmployeeConfirmationPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 80, 50));
        GridBagLayout layout = new GridBagLayout();
        existingEmployeeConfirmationPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        existingEmployeeConfirmationPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        JTextArea newEmployeeConfirmationText = new JTextArea("");
        
        if(updateOrDelete.equals("update"))
        {
            newEmployeeConfirmationText = new JTextArea(" successfully updated.");
            formatTextField(newEmployeeConfirmationText, false, Color.GREEN, f);
        }
        else
        {
            newEmployeeConfirmationText = new JTextArea(" successfully removed.");
            formatTextField(newEmployeeConfirmationText, false, Color.GREEN, f);
        }
        
        employeeDataSelectedEmployeeID = null; 
        employeeDataSelectedLastName = null;
        employeeDataSelectedFirstName = null;
        employeeDataSelectedSeniority = null;
        employeeDataSelectedGrade = null;
        
        JButton nextNewEmployeeButton = new JButton("Edit Another Employee");
        nextNewEmployeeButton.setFont(f);
        nextNewEmployeeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                existingEmployeeConfirmationScreen.setVisible(false);
                clearAllInputFields();
                employeeTabPane.removeAll();
                existingEmployeeSearchScreen.setVisible(true);
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
                existingEmployeeConfirmationScreen.setVisible(false);
                ManpowerAllocator.returnToMainMenu();
            }
        
        });
        
        gbc.anchor = GridBagConstraints.LINE_START;
        addObjects(newEmployeeConfirmationText, existingEmployeeConfirmationPanel, layout, gbc, 1, 0, 1, 1, 1, 1);
        gbc.ipadx = 0;
        gbc.ipady = 160;
        addObjects(nextNewEmployeeButton, existingEmployeeConfirmationPanel, layout, gbc, 1, 1, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0,0,0,20);
        addObjects(mainMenuButton, existingEmployeeConfirmationPanel, layout, gbc, 0, 1, 1, 1, 1, 1);
        return existingEmployeeConfirmationPanel;
    }
    
    private static void loadEmployees()
    {
        String fileName = "EmployeeFile.txt";
        String line = null;
        
        if(!employeeData.isEmpty())
        {
            employeeData.clear();
        }
        
        try 
        {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            while((line = br.readLine()) != null)
            {
                employeeData.add(line);
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
        
        
        
    }
    
    public static void updateEmployeeLogAndComboBox(Employee employeeUpdated, String updateOrDelete)
    {
        String fileName = "EmployeeFile.txt";
        String line = null;
        
        if(updateOrDelete.equals("update"))
        {
            ArrayList<String> newEmployeesAdded = new ArrayList<>();
            ArrayList<String> currentEmployees = new ArrayList<>();

            for(int index = 0; index < existingEmployeeBox.getItemCount(); index++)
            {
                if(!existingEmployeeBox.getItemAt(index).toString().contains(employeeUpdated.getEmployeeID())
                        && (!existingEmployeeBox.getItemAt(index).toString().contains(employeeUpdated.getFirstName())
                        || !existingEmployeeBox.getItemAt(index).toString().contains(employeeUpdated.getLastName())))
                {
                    currentEmployees.add((String)existingEmployeeBox.getItemAt(index));
                }
            }

            try 
            {
                FileReader fr = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fr);
                while((line = br.readLine()) != null)
                {
                    if(!employeeData.contains(line))
                    {
                        employeeData.add(line);
                        newEmployeesAdded.add(line);
                    }
                }

                br.close();
            }
            catch(FileNotFoundException ex)
            {
                System.out.println("Unable to open file");
            }
            catch(IOException ex)
            {
                System.out.println("Error reading file");
            }

            ArrayList<String> newEmployeesAddedNames = getEmployeeNames(newEmployeesAdded);
            while(!newEmployeesAddedNames.isEmpty())
            {
                currentEmployees.add(newEmployeesAddedNames.get(0));
                newEmployeesAddedNames.remove(0);
            }

            Collections.sort(currentEmployees);
            existingEmployeeBox.removeAllItems();

            for(String employeeName : currentEmployees)
            {
                existingEmployeeBox.addItem(employeeName);
            }
        }
        else if(updateOrDelete.equals("delete"))
        {
            ArrayList<String> currentEmployees = new ArrayList<>();

            for(int index = 0; index < existingEmployeeBox.getItemCount(); index++)
            {
                currentEmployees.add((String)existingEmployeeBox.getItemAt(index));
                if(existingEmployeeBox.getItemAt(index).toString().contains(employeeUpdated.getEmployeeID()))
                {
                    currentEmployees.remove(index);
                }
            } 
            
            try 
            {
                FileReader fr = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fr);
                while((line = br.readLine()) != null)
                {
                    if(employeeData.contains(line))
                    {
                        employeeData.remove(line);
                    }
                }

                br.close();
            }
            catch(FileNotFoundException ex)
            {
                System.out.println("Unable to open file");
            }
            catch(IOException ex)
            {
                System.out.println("Error reading file");
            }
            
            Collections.sort(currentEmployees);
            existingEmployeeBox.removeAllItems();

            for(String employeeName : currentEmployees)
            {
                existingEmployeeBox.addItem(employeeName);
            }
            
        }
        
        existingEmployeeBox.repaint();
        existingEmployeeBox.revalidate();
    }
    
    private static void setEmployeeDataSelectedFields()
    {
        ArrayList<String> tokens = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(employeeDataSelected, "*");
        while(st.hasMoreTokens())
        {
            tokens.add(st.nextToken());
        }
        employeeDataSelectedEmployeeID = tokens.get(0);
        employeeDataSelectedLastName = tokens.get(1);
        employeeDataSelectedFirstName = tokens.get(2);
        employeeDataSelectedSeniority = tokens.get(3);
        employeeDataSelectedGrade = tokens.get(4);
        
        ArrayList<String> tokens2 = new ArrayList<>();
        StringTokenizer st2 = new StringTokenizer(tokens.get(5), ";");
        while(st2.hasMoreTokens())
        {
            tokens2.add(st2.nextToken());
        }
        employeeDataSelectedJobs = (ArrayList<String>)tokens2.clone();
        employeeDataSelectedNotes = tokens.get(6);
    }
    
    private static ArrayList<String> getEmployeeNames()
    {
        loadEmployees();
        ArrayList<String> employeeRawData = (ArrayList<String>)employeeData.clone();
        ArrayList<String> employeeNameOnly = new ArrayList<>();
        
        for(String currentLine : employeeRawData)
        {
            ArrayList<String> tokens = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(currentLine, "*");
            while(st.hasMoreTokens())
            {
                tokens.add(st.nextToken());
            }
            if(tokens.size() > 2)
            {
                employeeNameOnly.add(tokens.get(2) +  " " + tokens.get(1) + " (" + tokens.get(0) + ")");
            }
        }
          
        Collections.sort(employeeNameOnly);
        return employeeNameOnly;
    }
    
    private static void deleteEmployeeFromFile(ArrayList<String> employeeToDelete)
    {
        ArrayList<String> employeeCurrentData = (ArrayList<String>)employeeData.clone();
        for (String emp : employeeCurrentData) 
        {
            if(emp.contains(employeeToDelete.get(1)))
            {
                employeeData.remove(emp);
            }
        }
        
        try(FileWriter fw = new FileWriter("EmployeeFileTemp.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for(String employee : employeeData)
            {
                out.println(employee);
            }
        }
        catch(IOException e)
        {
            System.out.println("Error Creating File");
        }
        
        File realName = new File("EmployeeFile.txt");
        realName.delete();
        new File("EmployeeFileTemp.txt").renameTo(realName);
    }
    
    private static ArrayList<String> getEmployeeNames(ArrayList<String> names)
    {
        ArrayList<String> employeeNameOnly = new ArrayList<>();
        
        for(String currentLine : names)
        {
            ArrayList<String> tokens = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(currentLine, "*");
            while(st.hasMoreTokens())
            {
                tokens.add(st.nextToken());
            }
            employeeNameOnly.add(tokens.get(2) +  " " + tokens.get(1) + " (" + tokens.get(0) + ")");
        }
          
        Collections.sort(employeeNameOnly);
        return employeeNameOnly;
    }
    
    private static void storeEmployeeInfo(String[] infoToAdd)
    {
        employeeInfo.addAll(Arrays.asList(infoToAdd));
    }
    
    private static void updateNotes(String note)
    {
        employeeNotes = note;
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
                        
            if(DOCK_JOBS.contains(jobSelectedToRemove.get(0).getText()) && workGroupSelection.getSelectedItem() != null && ((String)workGroupSelection.getSelectedItem()).equals("Docks"))
            {
                selectedJobNames.remove(jobSelectedToRemove.get(0).getText());
                jobsPossibleContainer.add(jobSelectedToRemove.remove(0));
            }
            else if(PPCHASSIS_JOBS.contains(jobSelectedToRemove.get(0).getText()) && workGroupSelection.getSelectedItem() != null && ((String)workGroupSelection.getSelectedItem()).equals("PrePaint Chassis"))
            {
                selectedJobNames.remove(jobSelectedToRemove.get(0).getText());
                jobsPossibleContainer.add(jobSelectedToRemove.remove(0));
            }
            else if(ENGINE_LINE_JOBS.contains(jobSelectedToRemove.get(0).getText()) && workGroupSelection.getSelectedItem() != null && ((String)workGroupSelection.getSelectedItem()).equals("Engine Line"))
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
    
    private static ArrayList<String> removeJobTypeFromJob(ArrayList<String> jobsToRemove)
    {
        ArrayList<String> toRemove = new ArrayList<String>();
        ArrayList<String> toAdd = new ArrayList<String>();
        for(String job : jobsToRemove)
        {
            if(job.contains("<Training>"))
            {
                String jobMinusJobType = job.replace("<Training>", "");
                toRemove.add(job);
                toAdd.add(jobMinusJobType);
            }
            else if(job.contains("<Primary>"))
            {
                String jobMinusJobType = job.replace("<Primary>", "");
                toRemove.remove(job);
                toAdd.add(jobMinusJobType);
            }
            else if(job.contains("<Secondary>"))
            {
                String jobMinusJobType = job.replace("<Secondary>", "");
                toRemove.remove(job);
                toAdd.add(jobMinusJobType);
            }
        }
        jobsToRemove.removeAll(toRemove);
        jobsToRemove.addAll(toAdd);
        return jobsToRemove;
    }
    
    private static void storeEmployeeJobs(ArrayList<String> jobsToStore)
    {
        for(String job : jobsToStore)
        {
            employeeJobsList.add(job);
        }
    }
    
    private static void formatCheckBox(JCheckBox checkBoxToFormat, Font f)
    {
        checkBoxToFormat.setBackground(Color.WHITE);
        checkBoxToFormat.setForeground(Color.BLACK);
        checkBoxToFormat.setFont(f);
    }
    
    private static void formatTextField(JTextComponent textFieldToFormat, boolean isEditable, Color fontColor, Font f)
    {
        textFieldToFormat.setBackground(Color.DARK_GRAY);
        textFieldToFormat.setForeground(fontColor);
        textFieldToFormat.setEditable(isEditable);
        textFieldToFormat.setFont(f);
    }
    
    private static void refreshComponents(JComponent[] componentsToRefresh)
    {
        for(JComponent currentComponent : componentsToRefresh)
        {
            currentComponent.revalidate();
            currentComponent.repaint();
        }
    }
    
    private static void clearAllInputFields()
    {
        employeeInfo.clear();
        employeeJobsList.clear();
        employeeTabPane.removeAll();
    }
    
    private static void saveEmployeeToFile(Employee employeeToSave) throws IOException
    {
        employeeData.remove(lineToOverwrite);
        employeeData.add(employeeToSave.toString());
        
        try(FileWriter fw = new FileWriter("EmployeeFileTemp.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for(String employee : employeeData)
            {
                out.println(employee);
            }
        }
        catch(IOException e)
        {
            System.out.println("Error Creating File");
        }
        
        File realName = new File("EmployeeFile.txt");
        realName.delete();
        new File("EmployeeFileTemp.txt").renameTo(realName);
        
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

        addObjects(newEmployeeName, existingEmployeeConfirmationPanel, layout, gbc, 0, 0, 1, 1, 1, 1);
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
    
    public static void getJFrame(JFrame frame)
    {
        mainJFrame = frame;
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
        
        ArrayList<String> tokens = new ArrayList<>();
        for(String lineOfData : departmentFinder)
        {

            StringTokenizer st = new StringTokenizer(lineOfData, "*@");
            while(st.hasMoreTokens())
            {
                tokens.add(st.nextToken());
            }
        }
        
        int index = 1;
        boolean isNextIndexDept = false;
        for(String titles : tokens)
        {
            if(isNextIndexDept)
            {
                DEPARTMENTS[index] = titles;
                index++;
                isNextIndexDept = false;
            }
            if(titles.equals("DS"))
            {
                isNextIndexDept = true;
            }
        }
        
    }
    private static void getWorkAreas()
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
