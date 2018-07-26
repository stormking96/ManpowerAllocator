package manpowerallocator;

import java.util.ArrayList;

public class Employee 
{
    String firstName;
    String lastName;
    String employeeID;
    String seniorityDate;
    String gradePay;
    String primaryJob;
    ArrayList<String> secondaryJob = new ArrayList<>();
    String notes;
    
    public Employee(String last, String first, String ID, String seniorDate, String gradePaySelection, String pJob, ArrayList<String> sJob, String note)
    {
        firstName = first;
        lastName = last;
        employeeID = ID;
        seniorityDate = seniorDate;
        gradePay = gradePaySelection;
        primaryJob = pJob;
        secondaryJob = (ArrayList<String>) sJob.clone();
        notes = note;
    }
    
    public Employee()
    {
        firstName = null;
        lastName = null;
        employeeID = null;
        seniorityDate = null;
        gradePay = null;
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
                + this.seniorityDate + "*" + this.gradePay + "*" + this.primaryJob + "*" 
                + concatenateSecondaryJobsToSingleString(this.secondaryJob) + "*" + notes;
        return stringToPrint;
    }
    
}
