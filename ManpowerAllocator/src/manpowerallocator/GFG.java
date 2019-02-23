/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpowerallocator;

import java.util.ArrayList;

/**
 *
 * @author storm
 */
// A Java program to find maximal 
// Bipartite matching. 

public class GFG {
    // M is number of applicants 
    // and N is number of jobs 

    static int M;
    static int N;
    static int numberOfJobsFilled = 0;
    
    public GFG(int numberOfEmployeesPresent, int numberOfJobsToFill)
    {
        M = numberOfEmployeesPresent;
        N = numberOfJobsToFill;
    }

    // A DFS based recursive function that 
    // returns true if a matching for 
    // vertex u is possible 
    boolean bpm(int bpGraph[][], int u, boolean seen[], int matchR[]) 
    {
        // Try every job one by one 
        for (int v = 0; v < N; v++) 
        {
            // If applicant u is interested 
            // in job v and v is not visited 
            if (bpGraph[u][v] > 0 && !seen[v]) 
            {

                // Mark v as visited 
                seen[v] = true;

                // If job 'v' is not assigned to 
                // an applicant OR employee is not in training
                // OR previously assigned applicant for job v (which 
                // is matchR[v]) has lower preference and 
                // an alternate job available. 
                // Since v is marked as visited in the 
                // above line, matchR[v] in the following 
                // recursive call will not get job 'v' again 
                if(bpGraph[u][v] != 1)
                {
                    if (matchR[v] < 0 ||( (bpGraph[u][v] >= bpGraph[matchR[v]][v]) && 
                            bpm(bpGraph, matchR[v], seen, matchR))) 
                    {
                        matchR[v] = u;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Returns maximum number 
    // of matching from M to N 
    int[] maxBPM(int bpGraph[][]) 
    {
        // An array to keep track of the 
        // applicants assigned to jobs. 
        // The value of matchR[i] is the 
        // applicant number assigned to job i, 
        // the value -1 indicates nobody is assigned. 
        int matchR[] = new int[N];

        // Initially all jobs are available 
        for (int i = 0; i < N; ++i)
        {
            matchR[i] = -1;
        }

        // Count of jobs assigned to applicants 
        int result = 0;
        for (int u = 0; u < M; u++) 
        {
            // Mark all jobs as not seen 
            // for next applicant. 
            boolean seen[] = new boolean[N];
            for (int i = 0; i < N; ++i) {
                seen[i] = false;
            }

            // Find if the applicant 'u' can get a job 
            if (bpm(bpGraph, u, seen, matchR)) {
                result++;
            }
        }
        setResult(result);
        return matchR;
    }
    
    
    private static void setResult(int result)
    {
        numberOfJobsFilled = result;
    }
    
    public int getNumberOfJobsFilled()
    {
        return numberOfJobsFilled;
    }
    
    public ArrayList<Integer> getIndexOfUnassignedEmployees(int[] matches)
    {
        ArrayList<Integer> unassignedEmployees = new ArrayList<>();
        boolean present = false;
        for(int index = 0; index < M; index++)
        {
            for(int employeeMatched : matches)
            {
                if(employeeMatched == index)
                {
                    present = true;
                }
            }
            if(!present)
            {
               unassignedEmployees.add(index);
            }
            else
            {
                present = false;
            }
        }
        return unassignedEmployees;
    }

}
