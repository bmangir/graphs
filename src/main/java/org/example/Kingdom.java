package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Kingdom {

    // TODO: You should add appropriate instance variables.
    public HashMap<Integer, List<Integer>> adjList = new HashMap<>();
    public int N; // number of cities for creating array as marking the cities as visited

    public void initializeKingdom(String filename) {
        // Read the txt file and fill your instance variables
        // TODO: Your code here

        int i = 1;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                int[] splitLine = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();

                // if ith city has no any edges with other vertexes
                if (Arrays.stream(splitLine).allMatch(element -> element == 0)) {
                    adjList.put(i, new ArrayList<>());
                }
                // For decreasing the run time
                else {
                    for (int j = 0; j < splitLine.length; j++) {
                        if (splitLine[j] == 1) { // has an edge
                            if (!adjList.containsKey(i)) {
                                adjList.put(i, new ArrayList<>(Collections.emptyList()));
                            }
                            adjList.get(i).add(j + 1);
                        }
                    }
                }
                i++;
            }
            br.close();
        }
        catch (Exception ignored) {
        }

        this.N = i;
    }


    public List<Colony> getColonies() {
        List<Colony> colonies = new ArrayList<>();
        // TODO: DON'T READ THE .TXT FILE HERE!
        // Identify the colonies using the given input file.
        // TODO: Your code here

        Set<Integer> visited = new HashSet<>();

        for (int vertex : adjList.keySet()) {
            if (!visited.contains(vertex)) {
                List<Integer> nodes = new ArrayList<>(); // Nodes list which will include the nodes of the graph
                Colony colony = new Colony();
                DFS(vertex, visited, nodes);
                colony.cities = nodes;

                // For checking key vertexes which has visited vertexes
                for (int i = 0; i < nodes.size(); i++) {
                    List<Integer> checkedKeys = new ArrayList<>(); // A list to check keys which may contains the visited vertexes
                    for (int j : adjList.keySet()) {
                        if (adjList.get(j).contains(nodes.get(i)) && !visited.contains(j)) {
                            DFS(j, visited, checkedKeys);
                        }
                    }
                    colony.cities.addAll(checkedKeys);
                }

                // Sort the list increasing order
                Collections.sort(colony.cities);

                // Find the colony has an any cycle.
                colony.trapMap = findCycles(colony);
                fillRoadNetwork(colony);

                colonies.add(colony);
            }
        }
        return colonies;
    }

    // It fills the roadNetworks of the colony with adj list of each city in cities list
    public void fillRoadNetwork(Colony c) {
        for (int i : c.cities) {
            if(!c.roadNetwork.containsKey(i)) {
                c.roadNetwork.put(i, new ArrayList<>(Collections.emptyList()));
            }
            c.roadNetwork.get(i).addAll(adjList.get(i));
        }
    }

    // It finds the graphs by using Depth-First-Searching
    public void DFS(int vertex, Set<Integer> visited, List<Integer> nodes) {
        if (!nodes.contains(vertex))
            nodes.add(vertex);

        visited.add(vertex);
        for (int adjacentVertex : adjList.get(vertex)) {
            if (!visited.contains(adjacentVertex)) {
                DFS(adjacentVertex, visited, nodes);
            }
        }
    }

    // It finds graphs has a cycle or not
    public HashMap<Integer, List<Integer>> findCycles(Colony colony) {
        HashMap<Integer, List<Integer>> cycles = new HashMap<>();
        boolean[] visited = new boolean[N+1];
        for (int i : colony.cities) {
            if (!visited[i]) {
                List<Integer> cycle = new ArrayList<>();

                // If there is a cycle
                if (cycleDetector(i, visited, -1, cycle)) {
                    cycles.put(cycle.get(0), cycle);
                }
            }
        }
        return cycles;
    }

    // It detects the cycle as parent child method
    private boolean cycleDetector(int u, boolean[] visited, int parent, List<Integer> cycle) {
        visited[u] = true;
        cycle.add(u);
        for (int v : adjList.get(u)) {
            if (!visited[v]) {
                if (cycleDetector(v, visited, u, cycle)) {
                    return true;
                }
            }
            // Found a cycle
            else if (v != parent && cycle.contains(v)) {
                cycle.add(v);
                return true;
            }
        }
        cycle.remove(cycle.size() - 1);
        return false;
    }

    public void printColonies(List<Colony> discoveredColonies) {
        // Print the given list of discovered colonies conforming to the given output format.
        // TODO: Your code here
        System.out.println("Discovered colonies are: ");
        int i = 1;
        for (Colony c : discoveredColonies) {
            System.out.println("Colony " + i + ": " + c.cities);
            i++;
        }
    }
}
