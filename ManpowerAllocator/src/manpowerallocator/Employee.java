package manpowerallocator;

import java.util.ArrayList;

public class Employee 
{
    String firstName;
    String lastName;
    String employeeID;
    String seniorityMonth;
    String seniorityDay;
    String seniorityYear;
    boolean utility;
    String primaryJob;
    ArrayList<String> secondaryJob = new ArrayList<>();
    String notes;
    
    public Employee(String last, String first, String ID, String seniorMM, String seniorDD, String seniorYY, String isUtil, String pJob, ArrayList<String> sJob, String note)
    {
        firstName = first;
        lastName = last;
        employeeID = ID;
        seniorityMonth = seniorMM;
        seniorityDay = seniorDD;
        seniorityYear = seniorYY;
        
        if(isUtil.equals("Yes"))
        {
            utility = true;
        }
        else
        {
            utility = false;
        }
        
        primaryJob = pJob;
        secondaryJob = (ArrayList<String>) sJob.clone();
        notes = note;
    }
    
    public Employee()
    {
        firstName = null;
        lastName = null;
        employeeID = null;
        seniorityMonth = null;
        seniorityDay = null;
        seniorityYear = null;
        utility = false;
        primaryJob = null;
        secondaryJob = null;
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
    
    public void setSeniorityDate(String seniorityMM, String seniorityDD, String seniorityYY)
    {
        seniorityMonth = seniorityMM;
        seniorityDay = seniorityDD;
        seniorityYear = seniorityYY;
    }
    
    public String[] getSeniorityDate()
    {  
        String[] seniorityDate = {seniorityMonth, seniorityDay, seniorityYear};
        return seniorityDate;
    }
    
    public void setUtility(String utilityYOrN)
    {
        if(utilityYOrN.equals("Yes"))
        {
            utility = true;
        }
        else
        {
            utility = false;
        }
    }
    
    public boolean isUtility()
    {
        return utility;
    }
    
    public void setPrimaryJob(String primary)
    {
        primaryJob = primary;
    }
    
    public String getPrimaryJob()
    {
        return primaryJob;
    }
    
    public void setSecondaryJobs(ArrayList<String> jobs)
    {
        secondaryJob = (ArrayList<String>) jobs.clone();
    }
    
    public ArrayList<String> getSecondaryJobs()
    {
        return secondaryJob;
    }
    
    private String concatenateSecondaryJobsToSingleString(ArrayList<String> secondJobArray)
    {
        StringBuilder build = new StringBuilder();
        for(String job : secondJobArray)
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
                + this.seniorityMonth + "*" + this.seniorityDay + "*" + this.seniorityYear + "*"
                + this.utility + "*" + this.primaryJob + "*" + concatenateSecondaryJobsToSingleString(this.secondaryJob)
                + "*" + notes;
        return stringToPrint;
    }
    
}
