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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class ManpowerAllocator extends JFrame
{
    private static String CURRENT_STATE;
    private static JPanel[] welcomeScreenPanels;
    private static JPanel editEmployeeScreen;
    private static JPanel newEmployeeScreen;
    private static JPanel existingEmployeeScreen;
    private static JPanel newEmployeeConfirmationScreen;
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final double screenRatio = screenSize.getWidth() / screenSize.getHeight();
    private static final String[] MONTHS = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    private static final String[] DAYS = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                                  "17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    private static final String[] YEARS = {"90","91","92","93","94","95","96","97","98","99","00","01","02","03","04",
                                        "05","06","07","08","09","10","11","12","13","14","15","16","17","18"};
    private static final String[] JOBS = {"Dock M/H 1", "Dock M/H 2", "Dock M/H 3", "Dock M/H 4", "Dock M/H 5",
                                    "Dock M/H 6", "Dock M/H 7", "Dock Returns", "Refurb (Store Operator)", "Refurb",
                                    "Audit 1", "Dispo", "Shipping 1", "Western Star PCH", "Western Star Cab",
                                    "Harness Kitting 1", "Harness Kitting 2", "Dock M/H 12"};
    private static ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private static ArrayList<String> secondaryJobSelected = new ArrayList<>();
    private static Employee newEmployee;
    private static JPanel newEmployeeConfirmationPanel;
            
    public ManpowerAllocator()
    {
        super("ManpowerAllocator");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().setBackground(Color.DARK_GRAY);
        
        setPreferredSize(new Dimension((int)screenSize.getWidth(), (int)screenSize.getHeight()));
        
        newEmployeeScreen = createNewEmployeeScreen();
        add(newEmployeeScreen);
        newEmployeeScreen.setVisible(false);
        
        newEmployeeConfirmationScreen = createNewEmployeeConfrimationScreen();
        add(newEmployeeConfirmationScreen);
        newEmployeeConfirmationScreen.setVisible(false);
        
        editEmployeeScreen = createEditEmployeeScreen();
        add(editEmployeeScreen);
        editEmployeeScreen.setVisible(false);
        
        welcomeScreenPanels = createWelcomeScreen();
        add(welcomeScreenPanels[0], "North");
        add(welcomeScreenPanels[1], "South");
        
        
        
        


            
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private static JPanel[] createWelcomeScreen()
    {
        JPanel upperPanel = new JPanel();
        upperPanel.setBackground(Color.DARK_GRAY);
        upperPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JTextArea welcomeText = new JTextArea("Manpower");
        welcomeText.setForeground(Color.WHITE);
        welcomeText.setBackground(Color.DARK_GRAY);
        welcomeText.setEditable(false);
        Font f = new Font("Times", Font.PLAIN, screenSize.width/10);
        welcomeText.setFont(f);
        upperPanel.add(welcomeText);

        JPanel lowerPanel = new JPanel();
        lowerPanel.setBackground(Color.DARK_GRAY);
        lowerPanel.setLayout(new GridLayout(2, 2, (int)(screenSize.getWidth() / 50), (int)(screenSize.getWidth() / 50)));
        lowerPanel.setPreferredSize(new Dimension((int)(screenSize.getWidth() / 2), (int)(screenSize.getHeight() / 2)));
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 80, 50));
        
        JButton attendanceButton = new JButton("Attendance");
        JButton editEmployeeButton = new JButton("Edit Employee");
        JButton relocateButton = new JButton("Relocate");
        JButton allocateButton = new JButton("Allocate");

        Font buttonFont = new Font("Arial", Font.BOLD, 30);
        attendanceButton.setFont(buttonFont);
        editEmployeeButton.setFont(buttonFont);
        relocateButton.setFont(buttonFont);
        allocateButton.setFont(buttonFont);
        lowerPanel.add(attendanceButton);
        
        attendanceButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                
            }
        });
            
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
            
        lowerPanel.add(relocateButton);
        relocateButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                
            }
        });
            
        lowerPanel.add(allocateButton);
        allocateButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                
            }
        });
        JPanel[] welcomeScreenPanels = {upperPanel,lowerPanel};
        return welcomeScreenPanels;
    }
    
    private static JPanel createEditEmployeeScreen()
    {
        JPanel editEmployeePanel = new JPanel();
        editEmployeePanel.setLayout(new GridLayout(1, 2, 50, 0));
        editEmployeePanel.setBackground(Color.DARK_GRAY);

        JButton newEmployeeButton = new JButton("New Employee");
        JButton existingEmployeeButton = new JButton("Existing Employee");

        Font buttonFont = new Font("Arial", Font.BOLD, 30);
        newEmployeeButton.setFont(buttonFont);
        existingEmployeeButton.setFont(buttonFont);

        editEmployeePanel.add(newEmployeeButton);
        newEmployeeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                editEmployeeScreen.setVisible(false);
                newEmployeeScreen.setVisible(true);
            }
        });
        
        editEmployeePanel.add(existingEmployeeButton);
        existingEmployeeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                editEmployeeScreen.setVisible(false);
            }
        });
        
        return editEmployeePanel;
    }
    
    private static JPanel createNewEmployeeScreen()
    {
        JPanel newEmployeePanel = new JPanel();
        
        GridBagLayout layout = new GridBagLayout();
        newEmployeePanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        newEmployeePanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        JTextArea nameUneditableText = new JTextArea("Name:");
        formatTextField(nameUneditableText, false, f);
        
        JTextArea comma = new JTextArea(" , ");
        formatTextField(comma, false, f); 
        
        JTextField lastNameBox = new JTextField("Last", 12);
        lastNameBox.setFont(f);
        
        JTextField firstNameBox = new JTextField("First", 12);
        firstNameBox.setFont(f);
        
        JTextArea employeeIDText = new JTextArea("Employee ID:");
        formatTextField(employeeIDText, false, f);
        
        JTextField employeeIDInput = new JTextField("12345", 4);
        employeeIDInput.setFont(f);
        
        JTextArea seniorityDateText = new JTextArea("Seniority Date:");
        formatTextField(seniorityDateText, false, f);
        
        JComboBox seniorityDateInputMonth = new JComboBox(MONTHS);
        seniorityDateInputMonth.setFont(f);
        
        JComboBox seniorityDateInputDay = new JComboBox(DAYS);
        seniorityDateInputDay.setFont(f);
        
        JComboBox seniorityDateInputYear = new JComboBox(YEARS);
        seniorityDateInputYear.setFont(f);
                
        JTextArea slash1 = new JTextArea("/");
        formatTextField(slash1, false, f);
        
        JTextArea slash2 = new JTextArea("/");
        formatTextField(slash2, false, f);
        
        JTextArea utilityText = new JTextArea("Utility?");
        formatTextField(utilityText, false, f);
        
        JComboBox yesOrNo = new JComboBox();
        yesOrNo.addItem("Yes");
        yesOrNo.addItem("No");
        yesOrNo.setFont(f);
        
        JTextArea primaryJobText = new JTextArea("Primary Job:");
        formatTextField(primaryJobText, false, f);
        
        JComboBox primaryJobSelection = new JComboBox(JOBS);
        primaryJobSelection.setFont(f);
        
        JTextArea secondaryJobText = new JTextArea("Secondary Job:");
        formatTextField(secondaryJobText, false, f);
        
        JList container = new JList();
        container.setLayout(new FlowLayout(FlowLayout.LEFT));
        container.setVisibleRowCount(3);
        container.setPreferredSize(new Dimension(50, 800));
        container.setVisibleRowCount(3);
        
        Font checkFont = new Font("Times", Font.PLAIN, 20);
        for(String job : JOBS)
        {
            JCheckBox secondaryJobsBox = new JCheckBox(job);
            formatCheckBox(secondaryJobsBox, checkFont);
            container.add(secondaryJobsBox);
            checkBoxes.add(secondaryJobsBox);
        }
        
        JScrollPane secondaryJobsPane = new JScrollPane(container);        
        secondaryJobsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        secondaryJobsPane.setPreferredSize(new Dimension(260, 200));
        
        JTextArea notesText = new JTextArea("Notes:");
        formatTextField(notesText, false, f);
        
        JTextArea notesInput = new JTextArea("N/A", 2, 20);
        notesInput.setLineWrap(true);
        notesInput.setWrapStyleWord(true);
        notesInput.setFont(f);
        JScrollPane notesInputScroll = new JScrollPane(notesInput);
        notesInputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(f);
        submitButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                String employeeLastName = lastNameBox.getText();
                String employeeFirstName = firstNameBox.getText();
                String employeeId = employeeIDInput.getText();
                String seniorityMonth = seniorityDateInputMonth.getSelectedItem().toString();
                String seniorityDay = seniorityDateInputDay.getSelectedItem().toString();
                String seniorityYear = seniorityDateInputYear.getSelectedItem().toString();
                String utility = yesOrNo.getSelectedItem().toString();
                String primaryJob = primaryJobSelection.getSelectedItem().toString();
                for (JCheckBox currentBox : checkBoxes) 
                {
                    if(currentBox.isSelected())
                    {
                        secondaryJobSelected.add(currentBox.getText());
                    }
                }
                String employeeNotes = notesInput.getText();
                newEmployee = new Employee(employeeLastName, employeeFirstName, 
                        employeeId, seniorityMonth, seniorityDay, seniorityYear, utility,
                        primaryJob, secondaryJobSelected, employeeNotes);
                
                try 
                {
                    saveEmployeeToFile(newEmployee);  
                } 
                catch (IOException ex) 
                {
                    //Handle Later
                }
                addEmployeeNameToConfirmationScreen(newEmployee);
                
                newEmployeeScreen.setVisible(false);
                newEmployeeConfirmationScreen.setVisible(true);
                
            }
        });
        
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setFont(f);
        mainMenuButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                newEmployeeScreen.setVisible(false);
                welcomeScreenPanels[0].setVisible(true);
                welcomeScreenPanels[1].setVisible(true);
            }
        });
        
        newEmployeePanel.validate();
        
        addObjects(nameUneditableText, newEmployeePanel, layout, gbc, 0, 0, 1, 1, 0.1, 0.1);
        addObjects(lastNameBox, newEmployeePanel, layout, gbc, 1, 0, 7, 1, 0.1, 0.1);
        addObjects(comma, newEmployeePanel, layout, gbc, 3, 0, 1, 1, 0.1, 0.1);
        addObjects(firstNameBox, newEmployeePanel, layout, gbc, 4, 0, 7, 1, 0.1, 0.1);
        addObjects(employeeIDText, newEmployeePanel, layout, gbc, 0, 1, 3, 1, 0.1, 0.1);
        addObjects(employeeIDInput, newEmployeePanel, layout, gbc, 2, 1, 2, 1, 0.1, 0.1);
        addObjects(seniorityDateText, newEmployeePanel, layout, gbc, 3, 1, 2, 1, 0.1, 0.1);
        addObjects(seniorityDateInputMonth, newEmployeePanel, layout, gbc, 5, 1, 1, 1, 0.1, 0.1);
        addObjects(slash1, newEmployeePanel, layout, gbc, 6, 1, 1, 1, 0.1, 0.1);
        addObjects(seniorityDateInputDay, newEmployeePanel, layout, gbc, 7, 1, 1, 1, 0.1, 0.1);
        addObjects(slash2, newEmployeePanel, layout, gbc, 8, 1, 1, 1, 0.1, 0.1);
        addObjects(seniorityDateInputYear, newEmployeePanel, layout, gbc, 9, 1, 1, 1, 0.1, 0.1);
        addObjects(utilityText, newEmployeePanel, layout, gbc, 0, 2, 1, 1, 0.1, 0.1);
        addObjects(yesOrNo, newEmployeePanel, layout, gbc, 1, 2, 1, 1, 0.1, 0.1);
        addObjects(primaryJobText, newEmployeePanel, layout, gbc, 2, 2, 1, 1, 0.1, 0.1);
        addObjects(primaryJobSelection, newEmployeePanel, layout, gbc, 3, 2, 3, 1, 0.1, 0.1);
        addObjects(secondaryJobText, newEmployeePanel, layout, gbc, 7, 2, 3, 1, 0.1, 0.1);
        addObjects(secondaryJobsPane, newEmployeePanel, layout, gbc, 10, 2, 3, 1, 0.1, 0.1);
        addObjects(notesText, newEmployeePanel, layout, gbc, 2, 3, 2, 1, 0.1, 0.1);
        addObjects(notesInputScroll, newEmployeePanel, layout, gbc, 3, 3, 9, 1, 0.1, 0.1);
        addObjects(mainMenuButton, newEmployeePanel, layout, gbc, 1, 5, 1, 1, 0.1, 0.1);
        addObjects(submitButton, newEmployeePanel, layout, gbc, 10, 5, 1, 1, 0.1, 0.1);
        
        
        return newEmployeePanel;
    }
    
    private static JPanel createNewEmployeeConfrimationScreen()
    {
        newEmployeeConfirmationPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        newEmployeeConfirmationPanel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        
        newEmployeeConfirmationPanel.setBackground(Color.DARK_GRAY);
        Font f = new Font("Times", Font.PLAIN, 40);
        
        JTextArea newEmployeeConfirmationText = new JTextArea(" successfully added.");
        formatTextField(newEmployeeConfirmationText, false, f);
        
        JTextArea bufferZone = new JTextArea();
        
        addObjects(newEmployeeConfirmationText, newEmployeeConfirmationPanel, layout, gbc, 4, 0, 1, 1, 1, 1);
        
        return newEmployeeConfirmationPanel;
    }
    
    private static void addEmployeeNameToConfirmationScreen(Employee newEmployee)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        
        Font f = new Font("Times", Font.PLAIN, 40);
        
        JTextArea newEmployeeName = new JTextArea(newEmployee.getLastName() + ", " + newEmployee.getFirstName());
        formatTextField(newEmployeeName, false, f);
        
        addObjects(newEmployeeName, newEmployeeConfirmationPanel, layout, gbc, 3, 0, 1, 1, .1, 1);
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
    
    public static void formatTextField(JTextComponent textFieldToFormat, boolean isEditable, Font f)
    {
        textFieldToFormat.setBackground(Color.DARK_GRAY);
        textFieldToFormat.setForeground(Color.WHITE);
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
            //Handle later
        }
        
    }
    
    public static void main(String[] args) 
    {
        CURRENT_STATE = "Welcome";
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            { 
                new ManpowerAllocator().setVisible(true);
            }
        });
    }
    
}
