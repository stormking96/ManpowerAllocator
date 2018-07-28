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
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static manpowerallocator.ManpowerAllocator.formatTextField;

/**
 *
 * @author matthewthayer
 */
public class Attendance 
{
    private ArrayList<String> employees;
    private Date date;
    private static final String[] REASON_TYPES = {"Vacation", "Sick/PTO", "IVR"};
    private static final String[] DEPARTMENTS = {" ", "Materials", "PrePaint Chassis"};
    private static ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private static ArrayList<JCheckBox> empoyeeSelectedToAdd = new ArrayList<>();
    private static ArrayList<JCheckBox> employeeSelectedToRemove = new ArrayList<>();
    private static ArrayList<String> employeeInfo = new ArrayList<>();
    private static ArrayList<String> employeeAttendanceList = new ArrayList<>();
    private static String attendanceNotes;
    
    public Attendance(ArrayList<String> empl)
    {
       employees = (ArrayList<String>)empl.clone();
       date = new Date();
    }
    
    public Attendance()
    {
        
    }
    
    public String getEmployee(String employee)
    {
        boolean found = false;
        String employeeInfo = "not found";
        while(!found)
        {
            for(String emp : employees)
            {
               found = true;
               employeeInfo = emp;
            }
        }
        return employeeInfo;
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
                        if(!currentBox.getText().contains("<Vacation>") &&
                                !currentBox.getText().contains("<Sick/PTO>") && 
                                !currentBox.getText().contains("<IVR>"))
                        {
                            currentBox.setText(currentBox.getText() + "<" + jobTypeSelection.getSelectedItem().toString() + ">");
                            selectedJobNamesWithJobType.add(currentBox.getText());
                        }
                        
                        empoyeeSelectedToAdd.add(currentBox);
                        jobsPossibleContainer.remove(currentBox);
                    }
                }
                
                //Condition that checks if the user checked jobs to add
                if(!empoyeeSelectedToAdd.isEmpty())
                {
                    //Loop to add all jobs to selected jobs container
                    while(!empoyeeSelectedToAdd.isEmpty())
                    {
                        empoyeeSelectedToAdd.get(0).setSelected(false);
                        jobsSelectedContainer.add(empoyeeSelectedToAdd.remove(0));
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
                    }
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
    
    private static void refreshComponents(JComponent[] componentsToRefresh)
    {
        for(JComponent currentComponent : componentsToRefresh)
        {
            currentComponent.revalidate();
            currentComponent.repaint();
        }
    }
    
}
