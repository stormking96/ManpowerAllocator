/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpowerallocator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 *
 * @author storm
 */
public class Allocation 
{
    private static ArrayList<String> DOCK_JOBS;
    private static ArrayList<String> employeeData = new ArrayList<>();
    private static ArrayList<String> assemblers = new ArrayList<>();
    private static ArrayList<String> utility = new ArrayList<>();
    private static ArrayList<String> teamLeads = new ArrayList<>();
    private static ArrayList<String> allEmployeeDataSorted = new ArrayList<>();
    private static String[][] allEmployeePreferences;
    private static int[][] employeeJobInterestMatrix;
    private static int numberOfEmployees;
    private static int numberOfJobs;
    
    private static final int AUDIT_INDEX = 0;
    private static final int DISPO_INDEX = 1;
    private static final int MH1_INDEX = 2;
    private static final int MH12_INDEX = 3;
    private static final int MH2_INDEX = 4;
    private static final int MH3_INDEX = 5;
    private static final int MH4_INDEX = 6;
    private static final int MH5_INDEX = 7;
    private static final int MH6_INDEX = 8;
    private static final int MH7_INDEX = 9;
    private static final int DOCK_RETURNS_INDEX = 10;
    private static final int HARNESS_KITTING1_INDEX = 11;
    private static final int HARNESS_KITTING2_INDEX = 12;
    private static final int REFURB_INDEX = 13;
    private static final int REFURB_STORE_OPERATOR_INDEX = 14;
    private static final int SHIPPING1_INDEX = 15;
    private static final int WESTERN_STAR_CAB_INDEX = 16;
    private static final int WESTERN_STAR_PCH_INDEX = 17;
    
    private static final String[] JOB_PREFERENCE_TYPES = {"<Primary>","<Secondary>","<Training>"};

    public Allocation(ArrayList<String> dockJobs)
    {
        DOCK_JOBS = dockJobs;
        getPresentEmployees();
        numberOfEmployees = employeeData.size();
        numberOfJobs = DOCK_JOBS.size();
        orderEmployeesBySeniorityDate();
        orderEmployeesByGradePay();
        allEmployeePreferences = getAllEmployeePreferences();
        employeeJobInterestMatrix = createEdmondsMatrix(allEmployeePreferences);
        recombineAllEmployeesSorted();
        matchEmployees(employeeJobInterestMatrix);
    }
    
    private static void matchEmployees(int[][] interestGraph)
    {
        GFG employeeMatchAlgorithm = new GFG(numberOfEmployees, numberOfJobs);
        int[] employeeAssignments = new int[numberOfJobs];
        employeeAssignments = employeeMatchAlgorithm.maxBPM(interestGraph);
        printAssignedEmployees(employeeAssignments);
        System.out.println("        " + employeeMatchAlgorithm.getNumberOfJobsFilled() + 
                " / " + numberOfJobs + " jobs filled");
        
        printUnassignedEmployees(employeeMatchAlgorithm, employeeAssignments);
        
    }
    
    private static void printAssignedEmployees(int[] employeeAssign)
    {
        int index = 0;
        for(int employeeAssigned : employeeAssign)
        {
            if(employeeAssigned != -1)
            {
                String lineOfData = allEmployeeDataSorted.get(employeeAssigned);
                ArrayList<String> tokens = new ArrayList<>();
                StringTokenizer st = new StringTokenizer(lineOfData, "*");
                while(st.hasMoreTokens())
                {
                    tokens.add(st.nextToken());
                }
                String[] employeeName = new String[2];
                employeeName[0] = tokens.get(2);
                employeeName[1] = tokens.get(1);
                System.out.println((index + 1) + ". " + employeeName[0] + " " + employeeName[1]  
                + " ------ " + DOCK_JOBS.get(index));
                index++;
            }
            else if(employeeAssigned == -1)
            {
                System.out.println(" ! " + (index + 1) + ". NONE AVAILABLE" + " ------ "
                + DOCK_JOBS.get(index));
                index++;
            }
        }
    }
    
    private static void printUnassignedEmployees(GFG matchingAlgorithm, int[] employeeAssign)
    {
        System.out.println("\nEMPLOYEES NOT ASSIGNED: ");
        ArrayList<Integer> employeesNoAssignment = matchingAlgorithm.getIndexOfUnassignedEmployees(employeeAssign);
        for(int employeeNotAssigned : employeesNoAssignment)
        {
            String lineOfData = allEmployeeDataSorted.get(employeeNotAssigned);
            ArrayList<String> tokens = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(lineOfData, "*");
            while(st.hasMoreTokens())
            {
                tokens.add(st.nextToken());
            }
            String[] employeeName = new String[2];
            employeeName[0] = tokens.get(2);
            employeeName[1] = tokens.get(1);
            ArrayList<String> employeeJobsKnown = new ArrayList<>();
            for(String data : tokens)
            {
                if(data.contains(JOB_PREFERENCE_TYPES[0]) || data.contains(JOB_PREFERENCE_TYPES[1])
                    || data.contains(JOB_PREFERENCE_TYPES[2]))
                {
                    StringTokenizer st2 = new StringTokenizer(data, ";");
                    while(st2.hasMoreTokens())
                    {
                        employeeJobsKnown.add(st2.nextToken());
                    }
                }
            }
            System.out.println(employeeName[0] + " " + employeeName[1] + " ");
            System.out.println("Employee Knows: ");
            for(String jobs : employeeJobsKnown)
            {
                System.out.println("\t" + jobs);
            }
            System.out.println();
        }
    }
    
    private static int[][] createEdmondsMatrix(String[][] employeePreferences)
    {
        int[][] edmondsMatrix = new int[numberOfEmployees][numberOfJobs];
        
        for(int employee = 0; employee < employeePreferences.length; employee++)
        {
            for(int job = 0; job < employeePreferences[employee].length; job++)
            {
                if(employeePreferences[employee][job] != null)
                {
                    if(employeePreferences[employee][job].contains("<Primary>"))
                    {
                        if(employeePreferences[employee][job].contains("$UTIL$"))
                        {
                            edmondsMatrix[employee][job] = 5;
                        }
                        else if(employeePreferences[employee][job].contains("$TLEAD$"))
                        {
                            edmondsMatrix[employee][job] = 2;
                        }
                        else
                        {
                            edmondsMatrix[employee][job] = 6;
                        }
                    }
                    else if(employeePreferences[employee][job].contains("<Secondary>"))
                    {
                        if(employeePreferences[employee][job].contains("$UTIL$"))
                        {
                            edmondsMatrix[employee][job] = 4;
                        }
                        else if(employeePreferences[employee][job].contains("$TLEAD$"))
                        {
                            edmondsMatrix[employee][job] = 2;
                        }
                        else
                        {
                            edmondsMatrix[employee][job] = 3;
                        }
                    }
                    else if(employeePreferences[employee][job].contains("<Training>"))
                    {
                        edmondsMatrix[employee][job] = 1;
                    }
                }
                else
                {
                    edmondsMatrix[employee][job] = 0;
                }
            }
        }
        
        return edmondsMatrix;
    }
    
    private static String[][] getAllEmployeePreferences()
    {
        String[][] allPreferences = new String[numberOfEmployees][numberOfJobs];
        int rowCount = 0;
        int colCount = 0;
        for(String assemblerData : assemblers)
        {
            String[] preferences = getSingleEmployeePreferences(assemblerData);
            for(String prefToAdd : preferences)
            {
                 allPreferences[rowCount][colCount] = prefToAdd;
                colCount++;
            }
            if(rowCount < numberOfEmployees-1)
            {
                rowCount++;
                colCount = 0;
            }
        }
        for(String utilityData : utility)
        {
            String[] preferences = getSingleEmployeePreferences(utilityData);
            for(String prefToAdd : preferences)
            {
                if(prefToAdd != null)
                {
                    allPreferences[rowCount][colCount] = prefToAdd + "$UTIL$";
                }
                colCount++;
            }
            if(rowCount < numberOfEmployees-1)
            {
                rowCount++;
                colCount = 0;
            }
        }
        for(String teamLeadData : teamLeads)
        {
            String[] preferences = getSingleEmployeePreferences(teamLeadData);
            for(String prefToAdd : preferences)
            {
                if(prefToAdd != null)
                {
                    allPreferences[rowCount][colCount] = prefToAdd + "$TLEAD$";
                }
                colCount++;
            }
            if(rowCount < numberOfEmployees-1)
            {
                rowCount++;
                colCount = 0;
            }
        }
        
        return allPreferences;
    }
    
    private static String[] getSingleEmployeePreferences(String lineOfEmployeeData)
    {
        String[] jobPreferences = new String[numberOfJobs];
        int index;
        
        ArrayList<String> tokens = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(lineOfEmployeeData, "*");
        while(st.hasMoreTokens())
        {
            tokens.add(st.nextToken());
        }
        
        for(String data : tokens)
        {
            if(data.contains(JOB_PREFERENCE_TYPES[0]) || data.contains(JOB_PREFERENCE_TYPES[1])
                    || data.contains(JOB_PREFERENCE_TYPES[2]))
            {
                ArrayList<String> tokens2 = new ArrayList<>();
                StringTokenizer st2 = new StringTokenizer(data, ";");
                while(st2.hasMoreTokens())
                {
                    tokens2.add(st2.nextToken());
                }
                for(String data2 : tokens2)
                {
                    index = findCorrespondingIndexToJob(data2);
                    jobPreferences[index] = data2;
                }
            }
        }
        
        return jobPreferences;
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
    
    private static void getPresentEmployees()
    {
        loadEmployees();
        ArrayList<String> employeeDataTemp;
        employeeDataTemp = (ArrayList<String>)employeeData.clone();
        for(String data : employeeData)
        {
            if(data.contains("*A*"))
            {
                employeeDataTemp.remove(data);
            }
        }
        employeeData = employeeDataTemp;
    }
    
    
    private static Date getSeniorityAsComparableDate(String employeeDate)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        Date seniorityDate = null;
        try {
         seniorityDate = dateFormatter.parse(employeeDate);  
      } catch (ParseException e) { 
         System.out.println("Unparseable using " + dateFormatter); 
      }
        return seniorityDate;
    }
    
    private static String retrieveSeniorityDateOfEmployee(String lineOfEmployeeData)
    {
        ArrayList<String> tokens = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(lineOfEmployeeData, "*");
        while(st.hasMoreTokens())
        {
            tokens.add(st.nextToken());
        }
        return tokens.get(3);
    }
    
    private static String retrieveJobTypeOfEmployee(String lineOfEmployeeData)
    {
        ArrayList<String> tokens = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(lineOfEmployeeData, "*");
        while(st.hasMoreTokens())
        {
            tokens.add(st.nextToken());
        }
        return tokens.get(4);
    }
    
    private static void orderEmployeesBySeniorityDate()
    {
        ArrayList<String> employeeSeniorityDates = new ArrayList<>();
        ArrayList<Date> employeeSeniorityComparable = new ArrayList<>();
        for(String lineOfData : employeeData)
        {
            employeeSeniorityDates.add(retrieveSeniorityDateOfEmployee(lineOfData));
        }
        for(String employeeDate : employeeSeniorityDates)
        {
            employeeSeniorityComparable.add(getSeniorityAsComparableDate(employeeDate));
        }
        QuickSorter dateSort = new QuickSorter();
        dateSort.sort(employeeSeniorityComparable, employeeData);
        
    }
    
    private static void recombineAllEmployeesSorted()
    {
        for(String lineOfData : assemblers)
        {
            allEmployeeDataSorted.add(lineOfData);
        }
        for(String lineOfData : utility)
        {
            allEmployeeDataSorted.add(lineOfData);
        }
        for(String lineOfData : teamLeads)
        {
            allEmployeeDataSorted.add(lineOfData);
        }
    }
    
    private static void orderEmployeesByGradePay()
    {
        for(String lineOfData : employeeData)
        {
            switch (retrieveJobTypeOfEmployee(lineOfData)) {
                case "No":
                    assemblers.add(lineOfData);
                    break;
                case "Utility":
                    utility.add(lineOfData);
                    break;
                case "Team Leader":
                    teamLeads.add(lineOfData);
                    break;
                default:
                    System.out.println("Error: Complexity Functionality Not Yet Implemented");
                    break;
            }
        }
    }
    
    private static int findCorrespondingIndexToJob(String jobName)
    {
        int index = -1;
        
        if(jobName.contains("Audit 1"))
        {
            index = AUDIT_INDEX;
        }
        else if(jobName.contains("Dispo"))
        {
    
            index = DISPO_INDEX;
        }
        else if(jobName.contains("Dock M/H 1"))
        {
            if(jobName.contains("Dock M/H 12"))
            {
                index = MH12_INDEX;
            } 
            else
            {
                index = MH1_INDEX;
            }
        }    
        else if(jobName.contains("Dock M/H 2"))
        {
            index = MH2_INDEX;
        }
               
        else if(jobName.contains("Dock M/H 3"))
        {
            index = MH3_INDEX;  
        }            
        else if(jobName.contains("Dock M/H 4"))
        {
            index = MH4_INDEX;
        }
        else if(jobName.contains("Dock M/H 5"))
        {
            index = MH5_INDEX;
        }
        else if(jobName.contains("Dock M/H 6"))
        {
            index = MH6_INDEX;
        }
        else if(jobName.contains("Dock M/H 7"))
        {
            index = MH7_INDEX;
        }
        else if(jobName.contains("Dock Returns"))
        {
            index = DOCK_RETURNS_INDEX;
        }
        else if(jobName.contains("Harness Kitting 1"))
        {
            index = HARNESS_KITTING1_INDEX;
        }
        else if(jobName.contains("Harness Kitting 2"))
        {
            index = HARNESS_KITTING2_INDEX;
        }
        else if(jobName.contains("Refurb"))
        {
            if(jobName.contains("Refurb (Store Operator)"))
            {
                index = REFURB_STORE_OPERATOR_INDEX;
            }
            else
            {
                index = REFURB_INDEX;
            }
        }
        else if(jobName.contains("Shipping 1"))
        {
            index = SHIPPING1_INDEX;
        }
        else if(jobName.contains("Western Star Cab"))
        {
            index = WESTERN_STAR_CAB_INDEX;
        }
        else if(jobName.contains("Western Star PCH"))
        {
            index = WESTERN_STAR_PCH_INDEX;
        }
       
        return index;         
    }
    
    
}
