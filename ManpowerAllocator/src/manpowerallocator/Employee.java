package manpowerallocator;

import java.util.ArrayList;

public class Employee 
{
    String firstName;
    String lastName;
    String employeeID;
    String seniorityDate;
    String gradePay;
    ArrayList<String> jobList = new ArrayList<>();
    String notes;
    
    public Employee(String last, String first, String ID, String seniorDate, String gradePaySelection, ArrayList<String> jobs, String note)
    {
        firstName = first;
        lastName = last;
        employeeID = ID;
        seniorityDate = seniorDate;
        gradePay = gradePaySelection;
        jobList = (ArrayList<String>) jobs.clone();
        notes = note;
    }
    
    public Employee()
    {
        firstName = null;
        lastName = null;
        employeeID = null;
        seniorityDate = null;
        gradePay = null;
        jobList = null;
        notes = null;     
    }
    
    public void setFirstName(String first)
    {
        firstName = first;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public void setLastName(String last)
    {
        lastName = last;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public void setEmployeeID(String id)
    {
        employeeID = id;
    }
    
    public String getEmployeeID()
    {  
        return employeeID;
    }
    
    public void setSeniorityDate(String seniorityMMDDYY)
    {
        seniorityDate = seniorityMMDDYY;
    }
    
    public String getSeniorityDate()
    {  
        return seniorityDate;
    }
    
    public void setGradePay(String gradePaySelection)
    {
        gradePay = gradePaySelection;
    }
    
    public String getGradePay()
    {
        return gradePay;
    }
    
    
    public void setJobs(ArrayList<String> jobs)
    {
        jobList = (ArrayList<String>) jobs.clone();
    }
    
    public ArrayList<String> getJobs()
    {
        return jobList;
    }
    
    private String concatenateJobsToSingleString(ArrayList<String> jobArray)
    {
        StringBuilder build = new StringBuilder();
        for(String job : jobArray)
        {
            build.append(job);
            build.append(";");
        }
        return build.toString();
    }
    
    public void setNotes(String note)
    {
        notes = note;
    }
    
    public String getNotes()
    {
        return notes;
    }
    
    @Override
    public String toString()
    {
        String stringToPrint = this.employeeID + "*" + this.lastName + "*" + this.firstName + "*"
                + this.seniorityDate + "*" + this.gradePay + "*" 
                + concatenateJobsToSingleString(this.jobList) + "*" + notes;
        return stringToPrint;
    }
    
}
