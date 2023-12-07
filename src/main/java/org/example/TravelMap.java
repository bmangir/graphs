package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;

public class TravelMap {

    // Maps a single id to a single Location.
    public Map<Integer, Location> locationMap = new HashMap<>();

    // List of locations, read in the given order
    public List<Location> locations = new ArrayList<>();

    // List of trails, read in the given order
    public List<Trail> trails = new ArrayList<>();

    // TODO: You are free to add more variables if necessary.

    public void initializeMap(String filename) {
        // Read the XML file and fill the instance variables locationMap, locations and trails.
        // TODO: Your code here
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(filename);
            doc.getDocumentElement().normalize();

            // Read Locations' information
            NodeList locList = doc.getElementsByTagName("Location");
            for (int temp = 0; temp < locList.getLength(); temp++) {
                Node nNode = locList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Location location = new Location(eElement.getElementsByTagName("Name").item(0).getTextContent(),
                            Integer.parseInt(eElement.getElementsByTagName("Id").item(0).getTextContent()));
                    locations.add(location);
                    locationMap.put(location.id, location);

                }
            }

            // Read Trails
            NodeList trailList = doc.getElementsByTagName("Trail");
            for (int temp = 0; temp < trailList.getLength(); temp++) {
                Node nNode = trailList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    int sourceId = Integer.parseInt(eElement.getElementsByTagName("Source").item(0).getTextContent());
                    int destinationId = Integer.parseInt(eElement.getElementsByTagName("Destination").item(0).getTextContent());
                    trails.add(new Trail(locationMap.get(sourceId), locationMap.get(destinationId),
                            Integer.parseInt(eElement.getElementsByTagName("Danger").item(0).getTextContent())));
                }
            }

        } catch (Exception ignored) {
        }
    }

    public List<Trail> getSafestTrails() {
        List<Trail> safestTrails = new ArrayList<>();
        // Fill the safestTrail list and return it.
        // Select the optimal Trails from the Trail list that you have read.
        // TODO: Your code here

        // Sort the trails by danger in ascending order for Kruskal's Algorithm
        trails.sort(Comparator.comparingInt(Trail::getDanger));

        // For Kruskal's Algorithm to check locations are connected or not.
        DisjointSet disjointSet = new DisjointSet(locations.size());

        for (Trail trail : trails) {
            int sourceIndex = trail.getSource().getId();
            int destIndex = trail.getDestination().getId();

            // Check the source and destination locations are belong to different sets
            if (disjointSet.find(sourceIndex) != disjointSet.find(destIndex)) {
                disjointSet.union(sourceIndex, destIndex);
                safestTrails.add(trail);
            }

            // All locations have been visited, break the loop(there is a cycle)
            if (safestTrails.size() == locations.size() - 1) {
                break;
            }
        }

        return safestTrails;
    }

    public void printSafestTrails(List<Trail> safestTrails) {
        // Print the given list of safest trails conforming to the given output format.
        // TODO: Your code here

        System.out.println("Safest trails are:");
        int totalDanger = 0;
        for (Trail trail : safestTrails) {
            System.out.println("The trail from " + trail.source.name +" to " + trail.destination.name + " with danger " + trail.danger);
            totalDanger += trail.danger;
        }
        System.out.println("Total Danger: " + totalDanger);
    }
}
