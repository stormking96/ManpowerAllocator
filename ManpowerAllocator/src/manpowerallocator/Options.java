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
import java.util.StringTokenizer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import static manpowerallocator.ManpowerAllocator.attendance;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author storm
 */
public class Options 
{
    private static JFrame mainJFrame;
    private static JPanel editDepartmentPanel;
    private static JPanel introEditWorkGroupPanel;
    private static JPanel editWorkGroupPanel;
    private static ArrayList<JPanel> lines = new ArrayList<>();
    private static JList departmentsContainer = new JList();
    private static JList workGroupsContainer = new JList();
    private static ArrayList<String> allDepartments = new ArrayList<>();
    private static ArrayList<String> allWorkGroups = new ArrayList<>();
    private static int lineNumber = 0;
    private static int lineToEdit = -1;
    private static boolean buttonPressed = false;
    private static JComboBox departmentsBox;
    
    public Options()
    {
        
    }
    
    public JPanel createIntroOptionsScreen()
    {
        JPanel optionsPanel = new JPanel();
        
        GridBagLayout layout = new GridBagLayout();
        optionsPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        optionsPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40); 
        
        JButton editDepartmentsButton = new JButton("Edit Departments");
        Font buttonFont = new Font("Arial", Font.BOLD, 30);
        editDepartmentsButton.setFont(buttonFont);
        editDepartmentsButton.setMaximumSize(new Dimension(2000, 300));
        editDepartmentsButton.setMinimumSize(new Dimension(2000, 300));
        editDepartmentsButton.setPreferredSize(new Dimension(2000, 300));
        
        editDepartmentsButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if(editDepartmentPanel == null)
                {
                    editDepartmentPanel = createEditDepartmentsPanel();
                }
                else
                {
                    editDepartmentPanel.removeAll();
                    lines.clear();
                    departmentsContainer.removeAll();
                    allDepartments.clear();
                    lineNumber = 0;
                    lineToEdit = -1;
                    buttonPressed = false;
                    editDepartmentPanel = createEditDepartmentsPanel();
                }
                optionsPanel.setVisible(false);
                editDepartmentPanel.setVisible(true);
                
            }
        });
        
        JButton editWorkGroupsButton = new JButton("Edit Work Groups");
        editWorkGroupsButton.setFont(buttonFont);
        editWorkGroupsButton.setMaximumSize(new Dimension(2000, 300));
        editWorkGroupsButton.setMinimumSize(new Dimension(2000, 300));
        editWorkGroupsButton.setPreferredSize(new Dimension(2000, 300));
        
        editWorkGroupsButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if(editDepartmentPanel == null)
                {
                    introEditWorkGroupPanel = createIntroEditWorkGroupPanel();
                }
                else
                {
                    introEditWorkGroupPanel.removeAll();
                    editWorkGroupPanel.removeAll();
                    lines.clear();
                    workGroupsContainer.removeAll();
                    allWorkGroups.clear();
                    lineNumber = 0;
                    lineToEdit = -1;
                    buttonPressed = false;
                    introEditWorkGroupPanel = createIntroEditWorkGroupPanel();
                }
                
                optionsPanel.setVisible(false);
                introEditWorkGroupPanel.setVisible(true);
                
            }
        });
        
        JButton editJobsButton = new JButton("Edit Jobs For Work Group");
        editJobsButton.setFont(buttonFont);
        editJobsButton.setMaximumSize(new Dimension(2000, 300));
        editJobsButton.setMinimumSize(new Dimension(2000, 300));
        editJobsButton.setPreferredSize(new Dimension(2000, 300));
        
        editJobsButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                
                
            }
        });
        
        
        
        
        gbc.anchor = GridBagConstraints.NORTH;
        addObjects(editDepartmentsButton, optionsPanel, layout, gbc, 0, 0, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.CENTER;
        addObjects(editWorkGroupsButton, optionsPanel, layout, gbc, 0, 0, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.SOUTH;
        addObjects(editJobsButton, optionsPanel, layout, gbc, 0, 0, 1, 1, 1, 1);
        
        
        return optionsPanel;
    }
    
    public JPanel createEditDepartmentsPanel()
    {
        JPanel editDepartmentsPanel = new JPanel();
        
        GridBagLayout layout = new GridBagLayout();
        editDepartmentsPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        Font buttonFont = new Font("Arial", Font.BOLD, 30);
        
        editDepartmentsPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40); 
        
        
        departmentsContainer.setLayout(layout);
        JPanel departmentsPanel = new JPanel();
        departmentsPanel.add(departmentsContainer);
        
        departmentsPanel.setPreferredSize(new Dimension(1200, 600));
        departmentsContainer.setPreferredSize(new Dimension(1200, 600));
        
        extractDepartments();
        
        //Add Departments Line by Line
        
        if(!allDepartments.isEmpty())
        {
            for(String department : allDepartments)
            {
                addGroupToContainer(department, "Department", departmentsContainer, allDepartments);
            }
        }
        
        JButton addNewDepartmentButton = new JButton("+");
        if(allDepartments.isEmpty())
        {
            addNewDepartmentButton.setFont(buttonFont);
            addNewDepartmentButton.setBackground(Color.GREEN);
            addNewDepartmentButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent event) 
                {
                    String input = "Enter Department Here";
                    addGroupToContainer(input, "Department", departmentsContainer, allDepartments);
                    editDepartmentsPanel.remove(addNewDepartmentButton);
                    editDepartmentsPanel.repaint();
                    editDepartmentsPanel.revalidate();
                }
            });
        }
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(buttonFont);
        saveButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent event) 
                {
                    allDepartments.clear();
                    int index = 0;
                    for (JPanel line : lines) 
                    {
                        JPanel lineToSave = lines.get(index);
                        JTextArea departmentName = (JTextArea) lineToSave.getComponent(2);
                        if(!departmentName.getText().contains("Enter Department Here"))
                        {
                            allDepartments.add(departmentName.getText());
                        }
                        index++;
                    }
                    try(FileWriter fw = new FileWriter("DepartmentsTemp.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw))
                    {
                        for(String dep : allDepartments)
                        {
                           out.println("DS*@" + dep + "*@");
                        }
                    }
                    catch(IOException e)
                    {
                        System.out.println("Error Creating File");
                    }
        
                    File realName = new File("DepartmentsFile.txt");
                    realName.delete();
                    new File("DepartmentsTemp.txt").renameTo(realName);
                    
                    editDepartmentPanel.setVisible(false);
                    ManpowerAllocator.returnToMainMenu();
                }
            });
        
         //Create headers for selection boxes
        Font departmentHeaderFont = new Font("Times", Font.BOLD, 40);
        JTextArea departmentsText = new JTextArea("Current Departments");
        formatTextField(departmentsText, false, Color.WHITE, departmentHeaderFont);
        
 
        gbc.anchor = GridBagConstraints.NORTH;
        addObjects(departmentsText, editDepartmentsPanel, layout, gbc, 1, 0, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.CENTER;
        addObjects(departmentsPanel, editDepartmentsPanel, layout, gbc, 1, 1, 1, 4, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        addObjects(addNewDepartmentButton, editDepartmentsPanel, layout, gbc, 0, 5, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.EAST;
        addObjects(saveButton, editDepartmentsPanel, layout, gbc, 2, 5, 1, 1, 1, 1);
        
        mainJFrame.add(editDepartmentsPanel);
        editDepartmentsPanel.setVisible(false);
        
        return editDepartmentsPanel;
    }
    
    private static JPanel createEditWorkGroupsPanel(String departmentEditing)
    {
        JPanel panelCreated = new JPanel();        
        
        GridBagLayout layout = new GridBagLayout();
        panelCreated.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        Font buttonFont = new Font("Arial", Font.BOLD, 30);
        
        panelCreated.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40); 
        
        
        workGroupsContainer.setLayout(layout);
        JPanel workGroupsPanel = new JPanel();
        workGroupsPanel.add(workGroupsContainer);
        
        workGroupsPanel.setPreferredSize(new Dimension(1200, 600));
        workGroupsContainer.setPreferredSize(new Dimension(1200, 600));
        
        //Add Departments Line by Line
        
        if(!allWorkGroups.isEmpty())
        {
            lines.clear();
            for(String workGroup : allWorkGroups)
            {
                addGroupToContainer(workGroup, "Work Group", workGroupsContainer, allWorkGroups);
            }
        }
        
        JButton addNewDepartmentButton = new JButton("+");
        if(allWorkGroups.isEmpty())
        {
            addNewDepartmentButton.setFont(buttonFont);
            addNewDepartmentButton.setBackground(Color.GREEN);
            addNewDepartmentButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent event) 
                {
                    String input = "Enter Work Group Here";
                    addGroupToContainer(input, "Work Group", workGroupsContainer, allWorkGroups);
                    panelCreated.remove(addNewDepartmentButton);
                    panelCreated.repaint();
                    panelCreated.revalidate();
                }
            });
        }
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(buttonFont);
        saveButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent event) 
                {
                    allWorkGroups.clear();
                    int index = 0;
                    for (JPanel line : lines) 
                    {
                        JPanel lineToSave = lines.get(index);
                        JTextArea workGroupName = (JTextArea) lineToSave.getComponent(2);
                        if(!workGroupName.getText().contains("Enter Work Group Here"))
                        {
                            allWorkGroups.add(workGroupName.getText());
                        }
                        index++;
                    }
                    
                    ArrayList<String> rawDataToEdit = loadDepartmentsFileData();
                    StringBuilder sb = new StringBuilder();
                    sb.append("DS*@");
                    sb.append(departmentEditing);
                    sb.append("*@WGS*@");
                    for(String wg : allWorkGroups)
                    {
                        sb.append(wg);
                        sb.append("*@");
                    }
                    sb.append("WGE");
                    
                    String newData = sb.toString();
                    rawDataToEdit.set(lineToEdit, newData);
                    
                    try(FileWriter fw = new FileWriter("DepartmentsTemp.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw))
                    {
                        for(String line: rawDataToEdit)
                        {
                           out.println(line);
                        }
                    }
                    catch(IOException e)
                    {
                        System.out.println("Error Creating File");
                    }
        
                    File realName = new File("DepartmentsFile.txt");
                    realName.delete();
                    new File("DepartmentsTemp.txt").renameTo(realName);
                    
                    panelCreated.setVisible(false);
                    ManpowerAllocator.returnToMainMenu();
                }
            });
        
         //Create headers for selection boxes
        Font workGroupHeaderFont = new Font("Times", Font.BOLD, 40);
        JTextArea workGroupText = new JTextArea("Current Work Groups For " + departmentEditing + ":");
        formatTextField(workGroupText, false, Color.WHITE, workGroupHeaderFont);
        
 
        gbc.anchor = GridBagConstraints.NORTH;
        addObjects(workGroupText, panelCreated, layout, gbc, 1, 0, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.CENTER;
        addObjects(workGroupsPanel, panelCreated, layout, gbc, 1, 1, 1, 4, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        addObjects(addNewDepartmentButton, panelCreated, layout, gbc, 0, 5, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.EAST;
        addObjects(saveButton, panelCreated, layout, gbc, 2, 5, 1, 1, 1, 1);
        
        panelCreated.setVisible(false);
        
        
        
        return panelCreated;
    }
    
    
    public JPanel createIntroEditWorkGroupPanel()
    {
        JPanel introPanel = new JPanel();
        
        introPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        GridBagLayout layout = new GridBagLayout();
        introPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        Font titleFont = new Font("Times", Font.PLAIN, 60);
        JTextArea employeeSearchText = new JTextArea("Select Department To Edit: ");
        formatTextField(employeeSearchText, false, Color.WHITE, titleFont);
        
        extractDepartments();
        ArrayList<String> departmentsList = (ArrayList<String>)allDepartments.clone();
        allDepartments.clear();
        departmentsBox = new JComboBox();
        for(String department : departmentsList)
        {
            departmentsBox.addItem(department);
        }
        
        departmentsBox.setFont(f);
        AutoCompleteDecorator.decorate(departmentsBox);
        departmentsBox.setMaximumRowCount(4);
        departmentsBox.setPreferredSize(new Dimension(
                departmentsBox.getPreferredSize().width + 300, 
                departmentsBox.getPreferredSize().height));
        
        JButton editButton = new JButton("Edit");
        
        editButton.setFont(f);
        introPanel.add(editButton);
        
        //Search for employee when clicked
        editButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String departmentSelected = (String)departmentsBox.getSelectedItem();
                introEditWorkGroupPanel.removeAll();
                lines.clear();
                workGroupsContainer.removeAll();
                allWorkGroups.clear();
                lineNumber = 0;
                lineToEdit = -1;
                buttonPressed = false;
                
                extractWorkGroups(departmentSelected);
                
                
                editWorkGroupPanel = createEditWorkGroupsPanel(departmentSelected);
                mainJFrame.add(editWorkGroupPanel);
                
                introEditWorkGroupPanel.setVisible(false);
                editWorkGroupPanel.setVisible(true);
                
            }
        });
        
        gbc.anchor = GridBagConstraints.CENTER;
        addObjects(employeeSearchText, introPanel, layout, gbc, 1, 0, 5, 1, 1, 1);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0,500,0,0);
        addObjects(departmentsBox, introPanel, layout, gbc, 1, 1, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(0,0,0,400);
        addObjects(editButton, introPanel, layout, gbc, 2, 1, 1, 1, 1, 1);
        
        mainJFrame.add(introPanel);
        introPanel.setVisible(false);
        
        return introPanel;
    }
    
    public static void getJFrame(JFrame frame)
    {
        mainJFrame = frame;
    }
    
    private static void addGroupToContainer(String group, String groupName, JList groupContainer, ArrayList<String> allOfGroup)
    {
        JPanel line = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        line.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        Font buttonFont = new Font("Arial", Font.BOLD, 30);
        
        Font departmentFont = new Font("Times", Font.BOLD, 40);
        JTextArea groupText = new JTextArea(group);
        groupText.setFont(departmentFont);
        
                
        JButton editButton = new JButton("...");
        editButton.setFont(buttonFont);
        editButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if(allOfGroup.contains(groupText.getText()))
                {
                    lineToEdit = allOfGroup.indexOf(groupText.getText()) - 1;
                    int indexToEdit = allOfGroup.indexOf(groupText.getText());
                    JButton saveEditButton = new JButton("[š]");
                    saveEditButton.setFont(buttonFont);
                    saveEditButton.addActionListener(new ActionListener() 
                    {
                        @Override
                        public void actionPerformed(ActionEvent event) 
                        {
                            groupText.setEditable(false);
                            line.remove(saveEditButton);
                            addObjects(editButton, line, layout, gbc, 1, lineToEdit, 1, 1, 1, 0);
                            line.repaint();
                            line.revalidate();
                        }
                    });
                    groupText.setEditable(true);
                    line.remove(editButton);
                    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                    addObjects(saveEditButton, line, layout, gbc, 1, lineToEdit, 1, 1, 1, 0);
                    line.repaint();
                    line.revalidate();
                }
            }
        });
        
        
        
        
        JButton addNewGroupButton = new JButton("+");
        addNewGroupButton.setFont(buttonFont);
        addNewGroupButton.setBackground(Color.GREEN);
        addNewGroupButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                buttonPressed = true;
                groupText.setEditable(false);
                addGroupToContainer("Enter " + groupName + " Here", groupName, groupContainer, allOfGroup);
                if(line.getComponent(line.getComponentCount() - 1) == addNewGroupButton)
                {
                    line.remove(addNewGroupButton);
                }
                else
                {
                    line.remove(line.getComponentCount() - 1);
                }
                line.repaint();
                line.revalidate();
                groupContainer.repaint();
                groupContainer.revalidate();
            }
        });
        
        JButton deleteButton = new JButton("X");
        deleteButton.setFont(buttonFont);
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if(lines.get(lines.size() -1) == line)
                {
                    addObjects(addNewGroupButton, lines.get(lines.size() - 2),  layout, gbc, 4, lines.size() - 2, 1, 1, 1, 0);
                }
                lines.remove(line);
                groupContainer.remove(line);
                groupContainer.repaint();
                groupContainer.revalidate();
            }
        });

        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        addObjects(deleteButton, line, layout, gbc, 0, lineNumber, 1, 1, 0, 0);
        addObjects(editButton, line, layout, gbc, 1, lineNumber, 2, 1, 1, 0);
        addObjects(groupText, line, layout, gbc, 3, lineNumber, 1, 1, 1, 0);
        if(lineNumber == allDepartments.size() - 1 || allDepartments.isEmpty())
        {
            addObjects(addNewGroupButton, line, layout, gbc, 4, lineNumber, 1, 1, 1, 0);
        }
        if(buttonPressed)
        {
            addObjects(addNewGroupButton, line, layout, gbc, 4, lineNumber, 1, 1, 1, 0);
            buttonPressed = false;
        }

        gbc.ipadx = 200;
        gbc.ipady = 5;

        if(lineNumber == 0)
        {
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            addObjects(line, groupContainer, layout, gbc, 0, lineNumber, 1, 1, 1, 0);
        }
        else
        {
            gbc.anchor = GridBagConstraints.LINE_START;
            addObjects(line, groupContainer, layout, gbc, 0, lineNumber, 1, 1, 1, 0);
        }
        
        lineNumber++;
        lines.add(line);
    }
    
    private static void formatTextField(JTextComponent textFieldToFormat, boolean isEditable, Color fontColor, Font f)
    {
        textFieldToFormat.setBackground(Color.DARK_GRAY);
        textFieldToFormat.setForeground(fontColor);
        textFieldToFormat.setEditable(isEditable);
        textFieldToFormat.setFont(f);
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
    
    private static ArrayList<String> loadDepartmentsFileData()
    {
        String fileName = "DepartmentsFile.txt";
        String line = null;
        
        if(!allDepartments.isEmpty())
        {
            allDepartments.clear();
        }
        
        ArrayList<String> rawData = new ArrayList<>();
        
        try 
        {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            while((line = br.readLine()) != null)
            {
                rawData.add(line);
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
        
        return rawData;
    }
    
    private static void extractDepartments()
    {
        ArrayList<String> rawData = loadDepartmentsFileData();
        ArrayList<String> tokens = new ArrayList<>();
        for(String lineOfData : rawData)
        {
            StringTokenizer st = new StringTokenizer(lineOfData, "*@");
            while(st.hasMoreTokens())
            {
                tokens.add(st.nextToken());
            }
        }
        boolean isNextIndexDept = false;
        for(String titles : tokens)
        {
            if(isNextIndexDept)
            {
                allDepartments.add(titles);
                isNextIndexDept = false;
            }
            if(titles.equals("DS"))
            {
                isNextIndexDept = true;
            }
        }
    }
    
    private static void extractWorkGroups(String dept)
    {
        allWorkGroups.clear();
        ArrayList<String> rawData = loadDepartmentsFileData();
        ArrayList<String> tokens = new ArrayList<>();
        String lineToExtract = null;
        int index = 0;
        int lineCounter = 0;
        boolean isEndOfLine = false;
        for(String lineOfData : rawData)
        {
            if(lineOfData.contains(dept))
            {
                lineToExtract = lineOfData;
                lineToEdit = lineCounter;
                break;
            }
            lineCounter++;
        }
        StringTokenizer st = new StringTokenizer(lineToExtract, "*@");
        while(st.hasMoreTokens())
        {
            tokens.add(st.nextToken());
        }
        while(!tokens.get(index).equals("WGS"))
        {
            if(index < tokens.size()-1)
            {
                index++;
            }
            else
            {
                isEndOfLine = true;
                break;
            }
        }
        if(index < tokens.size()-1)
        {
            index++;
        }
        while(!tokens.get(index).equals("WGE") && !isEndOfLine)
        {
            allWorkGroups.add(tokens.get(index));
            
            if(index < tokens.size()-1)
            {
                index++;
            }
            else
            {
                isEndOfLine = true;
                break;
            }
        }
        
    }
    
    
}
