package manpowerallocator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InputValidator 
{
    
    public InputValidator()
    {
        
    }
    
    public boolean validateName(String name)
    {
        boolean isValid = false;
        if(name.matches("[a-zA-Z]+"))
        {
            isValid = true;
        }
        
        return isValid;
    }
    
    public boolean validateDate(String date)        
    {
        boolean isValid;
        if(date.contains(" ") || date.length() != 10)
        {
            isValid = false;
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            sdf.setLenient(false);
            try
            {
                sdf.parse(date);
                isValid = true;
            }
            catch(ParseException e)
            {
                isValid = false;
            }
        }

        
        return isValid;
    }
    
    public boolean validateEmployeeID(String id)
    {
        boolean isValid = false;
        if(id.matches("^([0-9]{5})$"))
        {
            isValid = true;
        }
        
        return isValid;
    }
    
    public boolean validateJobs(ArrayList<String> jobNamesWithJobType)
    {
        boolean isValid = false;
        int primaryCount = 0;
        if(!jobNamesWithJobType.isEmpty())
        {
            for(String job : jobNamesWithJobType)
            {
                if(job.contains("<Primary>"))
                {
                    primaryCount++;
                }
            }
            if(primaryCount < 2)
            {
                isValid = true;
            }
        }
           
        return isValid;
    }
    
    public boolean validateNotes(String notes)
    {
        boolean isValid = false;
        if(!notes.equals(""))
        {
            isValid = true;
        }
        return isValid;
    }
    
    
}
