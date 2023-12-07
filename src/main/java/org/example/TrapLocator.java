package org.example;

import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    public List<List<Integer>> revealTraps() {

        // Trap positions for each colony, should contain an empty array if the colony is safe.
        // I.e.:
        // 0 -> [2, 15, 16, 31]
        // 1 -> [4, 13, 22]
        // 3 -> []
        // ...
        List<List<Integer>> traps = new ArrayList<>();

        // Identify the time traps and save them into the traps variable and then return it.
        // TODO: Your code here

        for (Colony c : colonies) {
            List<Integer> trapList = new ArrayList<>();

            // Safe(no cycle)
            if (c.trapMap.size() == 0)
                traps.add(new ArrayList<>());
            else {
                List<Integer> keySet = new ArrayList<>(c.trapMap.keySet());
                for (int k : keySet) {
                    for (int j = 0; j < c.trapMap.get(k).size() - 1; j++) {
                        trapList.add(c.trapMap.get(k).get(j));
                    }
                }
                traps.add(trapList);
            }
        }

        return traps;
    }

    public void printTraps(List<List<Integer>> traps) {
        // For each colony, if you have encountered a time trap, then print the cities that create the trap.
        // If you have not encountered a time trap in this colony, then print "Safe".
        // Print the your findings conforming to the given output format.
        // TODO: Your code here

        System.out.println("Danger exploration conclusions:");
        int i = 1;
        for (List<Integer> t : traps) {
            if (t.size() != 0) // It is not safe(cycle)
                System.out.println("Colony " + i + ":  Dangerous. Cities on the dangerous path: " + t);
            else // It is safe(no cycle)
                System.out.println("Colony " + i + " Safe");
            i++;
        }
    }
}
