/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpowerallocator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static manpowerallocator.ManpowerAllocator.formatTextField;

/**
 *
 * @author matthewthayer
 */
public class Attendance 
{
    private static ArrayList<String> employees = new ArrayList<>();
    private Date date;
    private static JTabbedPane attendanceTabPane = new JTabbedPane();
    private static final String[] REASON_TYPES = {"Vacation", "Sick/PTO", "IVR"};
    private static final String[] DEPARTMENTS = {" ", "Materials", "PrePaint Chassis"};
    private static ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private static ArrayList<JCheckBox> employeeSelectedToAdd = new ArrayList<>();
    private static ArrayList<JCheckBox> employeeSelectedToRemove = new ArrayList<>();
    private static ArrayList<String> employeeData = new ArrayList<>();
    private static ArrayList<String> employeeInfo = new ArrayList<>();
    private static ArrayList<String> employeeAttendanceList = new ArrayList<>();
    private static String attendanceNotes;
    private static JPanel hoursTab;
    private static JPanel saveTab;
    
    public Attendance()
    {
        String fileName = "EmployeeFile.txt";
        String line = null;
        ArrayList<String> employeesRawData = new ArrayList<>();
        
        try 
            {
                FileReader fr = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fr);
                while((line = br.readLine()) != null)
                {
                     employeesRawData.add(line);
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
        
        for(String emp : employeesRawData)
        {
            ArrayList<String> tokens = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(emp, "*");
            while(st.hasMoreTokens())
            {
                tokens.add(st.nextToken());
            }
            if(tokens.size() > 2)
            {
                employees.add(tokens.get(2) +  " " + tokens.get(1) + " (" + tokens.get(0) + ")");
            }
        }
        
        Collections.sort(employees);
            
        attendanceTabPane.setFont(new Font( "Times New Roman", Font.PLAIN, 40));
        
        attendanceTabPane.addTab("ATTENDANCE", createNewAttendanceTab());
        attendanceTabPane.setVisible(false);
        
        
        hoursTab = createNewAttendanceHoursTab();
        attendanceTabPane.addTab("HOURS", hoursTab);
        attendanceTabPane.setEnabledAt(1, false);
        
        saveTab = createNewAttendanceSaveTab();
        attendanceTabPane.addTab("SAVE", saveTab);
        attendanceTabPane.setEnabledAt(2, false);
        
        attendanceTabPane.addTab("OPTIONS", createNewAttendanceOptionTab());
        
        
        
        
        
            
    }
    
    public JTabbedPane getAttendanceTab()
    {
        return attendanceTabPane;
    }
    
    public String getEmployee(String employee)
    {
        boolean found = false;
        String employeeToFind = "not found";
        while(!found)
        {
            for(String emp : employees)
            {
                if(emp.contains(employee))
                {
                    found = true;
                    employeeToFind = emp;
                }
            }
        }
        return employeeToFind;
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
    
    public static JPanel createNewAttendanceTab()
    {
       //Create job panel and set layout
        JPanel newAttendancePanel = new JPanel();
        
        GridBagLayout layout = new GridBagLayout();
        newAttendancePanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        newAttendancePanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        //Begin creating all components to be added 
        JTextArea departmentText = new JTextArea("Department:");
        formatTextField(departmentText, false, Color.WHITE, f);
        
        //Components to be variable depending on what user clicks
        JList jobsPossibleContainer = new JList();
        JList jobsSelectedContainer = new JList();
        ArrayList<String> selectedJobNames = new ArrayList<>();
        ArrayList<String> selectedJobNamesWithJobType = new ArrayList<>();
        
        JComboBox jobTypeSelection = new JComboBox(REASON_TYPES);
        
        JComboBox departmentSelection = new JComboBox(DEPARTMENTS);
        departmentSelection.setFont(f);
        
        Font checkBoxFont = new Font("Times", Font.PLAIN, 40);
        
        //Declare which work groups appear when certain departments are selected
        departmentSelection.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String itemSelected = (String)departmentSelection.getSelectedItem();
                switch (itemSelected) {
                    case " ":
                        break;
                    case "Materials":
                           
                        for(String emp : employees)
                        {
                            JCheckBox employeeBox = new JCheckBox(emp);
                            formatCheckBox(employeeBox, checkBoxFont);
                            jobsPossibleContainer.add(employeeBox);
                            checkBoxes.add(employeeBox);
                            jobsPossibleContainer.revalidate();
                            jobsPossibleContainer.repaint();
                        }
                        break;
                    case "PrePaint Chassis":
                        break;
                    default:
                        break;
                }
            }
        });
        
        JTextArea jobTypeText = new JTextArea("Attendance Reason:");
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
        JTextArea jobsPossibleText = new JTextArea("Employees in Department");
        formatTextField(jobsPossibleText, false, Color.WHITE, jobHeaderFont);
        
        JTextArea jobsSelectedText = new JTextArea("Absentees");
        formatTextField(jobsSelectedText, false, Color.WHITE, jobHeaderFont);
        
        //Make possible jobs container scrollable and set preferred size 
        JScrollPane jobsPossiblePane = new JScrollPane(jobsPossibleContainer);        
        jobsPossiblePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jobsPossiblePane.setPreferredSize(new Dimension(800, 400));
        jobsPossibleContainer.setPreferredSize(new Dimension(200, 4200));
        
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
                        if(!currentBox.getText().contains("<Vacation>") &&
                                !currentBox.getText().contains("<Sick/PTO>") && 
                                !currentBox.getText().contains("<IVR>"))
                        {
                            currentBox.setText(currentBox.getText() + "<" + jobTypeSelection.getSelectedItem().toString() + ">");
                            selectedJobNamesWithJobType.add(currentBox.getText());
                        }
                        
                        employeeSelectedToAdd.add(currentBox);
                        jobsPossibleContainer.remove(currentBox);
                    }
                }
                
                //Condition that checks if the user checked jobs to add
                if(!employeeSelectedToAdd.isEmpty())
                {
                    //Loop to add all jobs to selected jobs container
                    while(!employeeSelectedToAdd.isEmpty())
                    {
                        employeeSelectedToAdd.get(0).setSelected(false);
                        jobsSelectedContainer.add(employeeSelectedToAdd.remove(0));
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
                        employeeSelectedToRemove.add(currentBox);
                        selectedJobNamesWithJobType.remove(currentBox.getText());
                        String jobToRemoveName = currentBox.getText();
                        
                        if(selectedJobNames.contains(jobToRemoveName))
                        {
                            selectedJobNames.remove(jobToRemoveName);    
                        }
                        
                        jobsSelectedContainer.remove(currentBox);
                        refreshComponents(componentsToRefresh);
                    }
                }
                if(!employeeSelectedToRemove.isEmpty())
                {
                    removeAbsenceTypeFromEmployee(selectedJobNames, jobsPossibleContainer, departmentSelection);
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
        
        JButton continueButton = new JButton("Continue");
        continueButton.setFont(f);
        
        // Save info and go to next tab when clicked
        continueButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if(!employeeAttendanceList.isEmpty())
                {
                    employeeAttendanceList.clear();
                }
                storeAttendance(selectedJobNamesWithJobType);
                saveTab.repaint();
                saveTab.revalidate();
                attendanceTabPane.setSelectedIndex(2);
                attendanceTabPane.setEnabledAt(2, true);
            }
        });
        
        //Add all components to the main Panel using convenience method for gridBag layout
        addObjects(departmentText, newAttendancePanel, layout, gbc, 0, 0, 1, 1, 0.1, 0.1);
        addObjects(departmentSelection, newAttendancePanel, layout, gbc, 1, 0, 1, 1, 0.1, 0.1);
        addObjects(jobTypeText, newAttendancePanel, layout, gbc, 4, 0, 1, 1, 0.1, 0.1);
        addObjects(jobTypeSelection, newAttendancePanel, layout, gbc, 5, 0, 1, 1, 0.1, 0.1);
        gbc.anchor = GridBagConstraints.CENTER;
        addObjects(jobsPanel, newAttendancePanel, layout, gbc, 0, 1, 8, 1, 1, 1);
        gbc.anchor = GridBagConstraints.EAST;
        addObjects(continueButton, newAttendancePanel, layout, gbc, 6, 2, 1, 1, 1, 1);
          
        return newAttendancePanel; 
    }
    
    private static JPanel createNewAttendanceHoursTab()
    {
        JPanel newAttendanceHoursPanel = new JPanel();
        
        GridBagLayout layout = new GridBagLayout();
        newAttendanceHoursPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        newAttendanceHoursPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        int line = 0;
        for(String attend : employeeAttendanceList)
        {
            JTextArea employeeAttendanceText = new JTextArea(attend);
            formatTextField(employeeAttendanceText, false, Color.WHITE, f);
            addObjects(employeeAttendanceText, newAttendanceHoursPanel, layout, gbc, line, 0, 1, 1, 1, 1);
            line++;
        }
        
        return newAttendanceHoursPanel;
    }
    
    private static void refreshComponents(JComponent[] componentsToRefresh)
    {
        for(JComponent currentComponent : componentsToRefresh)
        {
            currentComponent.revalidate();
            currentComponent.repaint();
        }
    }
    
    public static JPanel createNewAttendanceNotesTab()
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
        
        JButton continueButton = new JButton("Continue");
        continueButton.setFont(f);
        
        //Go to next tab when clicked
        continueButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                
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
    public static JPanel createNewAttendanceSaveTab()
    {
        //Create Panel to hold components and set layout
        JPanel newEmployeeSavePanel = new JPanel();
        
        newEmployeeSavePanel.setLayout(new GridLayout(2,1));
        newEmployeeSavePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        newEmployeeSavePanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        //Begin creating components to be shown
        JButton saveButton = new JButton("Save Attendance");
        saveButton.setFont(f);
        newEmployeeSavePanel.add(saveButton);
           
        saveButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                editEmployeeAttendance();
                attendanceTabPane.setVisible(false);
                ManpowerAllocator.returnToMainMenu();
            }
        });
        
        JButton clearButton = new JButton("Clear Attendance Data");
        clearButton.setFont(f);
        newEmployeeSavePanel.add(clearButton);
        
        clearButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                clearAttendanceData();
                attendanceTabPane.setVisible(false);
                ManpowerAllocator.returnToMainMenu();
            }
        });
                
        return newEmployeeSavePanel;   
    }
    
    /**
     * Method to create all components shown on save tab
     * 
     * @return JPanel containing all components for save tab
     */
    public static JPanel createNewAttendanceOptionTab()
    {
        //Create Panel to hold components and set layout
        JPanel newEmployeeOptionPanel = new JPanel();
        
        newEmployeeOptionPanel.setLayout(new GridLayout(2,1));
        newEmployeeOptionPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        newEmployeeOptionPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        //Begin creating components to be shown
        JButton printButton = new JButton("Print Attendance Report");
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
               
            }
        });
        
        return newEmployeeOptionPanel;
    }
    
    private static void removeAbsenceTypeFromEmployee(ArrayList<String> selectedJobNames, 
            JList jobsPossibleContainer, JComboBox workGroupSelection)
    {
        while(!employeeSelectedToRemove.isEmpty())
        {
            employeeSelectedToRemove.get(0).setSelected(false);
                       
            if(employeeSelectedToRemove.get(0).getText().contains("<Vacation>"))
            {
                String jobMinusJobType = employeeSelectedToRemove.get(0).getText().replace("<Vacation>", "");
                 employeeSelectedToRemove.get(0).setText(jobMinusJobType);
            }
            else if(employeeSelectedToRemove.get(0).getText().contains("<Sick/PTO>"))
            {
                String jobMinusJobType = employeeSelectedToRemove.get(0).getText().replace("<Sick/PTO>", "");
                employeeSelectedToRemove.get(0).setText(jobMinusJobType);
            }
            else if(employeeSelectedToRemove.get(0).getText().contains("<IVR>"))
            {
                String jobMinusJobType = employeeSelectedToRemove.get(0).getText().replace("<IVR>", "");
                employeeSelectedToRemove.get(0).setText(jobMinusJobType);
            }
                        
            selectedJobNames.remove(employeeSelectedToRemove.get(0).getText());
            jobsPossibleContainer.add(employeeSelectedToRemove.remove(0));
        }
    }
    
    private static void storeAttendance(ArrayList<String> attendanceList)
    {
        for(String att : attendanceList)
        {
            employeeAttendanceList.add(att);
        }
    }
    
    private static void editEmployeeAttendance()
    {
        if(!employeeAttendanceList.isEmpty())
        {
            loadEmployees();
            ArrayList<String> employeeNumbers = retrieveEmployeeNumberOnAttendanceList();
            ArrayList<Integer> lineNumbersToEdit = new ArrayList<>();

            for(String employeeNum : employeeNumbers)
            {
                lineNumbersToEdit.add(findLineNumberToEditAttendance(employeeNum));
            }

            for(Integer lineToEdit : lineNumbersToEdit)
            {
                String oldEmployeeData = employeeData.get(lineToEdit);
                String newEmployeeData = oldEmployeeData.replace("*P*", "*A*");
                employeeData.set(lineToEdit, newEmployeeData);
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
            employeeAttendanceList.clear();
        }
        
    }
    
    private static void clearAttendanceData()
    {
        loadEmployees();
        
        int index = 0;
        for(String employee : employeeData)
        {
            if(employee.contains("*A*"))
            {
                String oldEmployeeData = employeeData.get(index);
                String newEmployeeData = oldEmployeeData.replace("*A*", "*P*");
                employeeData.set(index, newEmployeeData);
            }
            index++;
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
    
    
    private static Integer findLineNumberToEditAttendance(String employeeNum)
    {
        int index = 0;
        int lineToOverwrite = 0;
        for(String lineOfEmployeeData : employeeData)
        {
            if(lineOfEmployeeData.contains(employeeNum))
            {
                lineToOverwrite = index;
                break;
            }
            index++;
        }
        
        return lineToOverwrite;
    }
    
    private static ArrayList<String> retrieveEmployeeNumberOnAttendanceList()
    {
        ArrayList<String> employeeNumbers = new ArrayList<String>();
        for(String dat : employeeAttendanceList)
        {
            StringBuilder sb = new StringBuilder(5);
            int index = dat.indexOf("(");
            index++;
            for(int i = index; i < index + 5; i++)
            {
                sb.append(dat.charAt(i));
            }
            employeeNumbers.add(sb.toString());
        }
        return employeeNumbers;
    }
    
    private static void formatCheckBox(JCheckBox checkBoxToFormat, Font f)
    {
        checkBoxToFormat.setBackground(Color.WHITE);
        checkBoxToFormat.setForeground(Color.BLACK);
        checkBoxToFormat.setFont(f);
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
}
