package Final;

import java.util.ArrayList;
import java.util.List;

/* Class: CMSC 350
Student: Benjamin Sims 
Assignment: Final
Due Date: 12 Oct 2014
This File: SocialGraph.java
Required Files: none
Description: Final for CMSC 350 
Input: none                       
Output: a social graph
*/


public class SocialGraph extends Graph<String> {
    
    //Method used to create basic SocialGraph
    public SocialGraph(){
        
    }
    
    //Method used to create a populated SocialGraph
    public SocialGraph(int[][] edges, String[] vertices){
        super(edges, vertices);
    }
    
    //Method used to find the centrality of a vertex
    public double normalizedDegreeOfCentrality(int index){
        double centrality;
        
        //this part ensures that the divisor later is not 0
        if (this.getSize() == 1){
            return 0.0;
        }
        centrality = (double)this.getDegree(index) / (this.getSize() - 1);
        
        return centrality;
    }
    
    //Method used to find the number of triangles connected to a vertex.  This 
    //method starts by making a list of neighbors of the selected vertex.
    public int numberOfTrianglesIncidentToVertex(int index){
        int numberOfTriangles = 0;
        int count = 0;
        List<Integer> adjacentVertices = this.getNeighbors(index);
        List<Integer> tempAdjacentVertices;
        
        int size = adjacentVertices.size();
        //This while loop runs as many times as there are neighbors.
        while (count != size){
            //Here a list of a neighbor's neighbors are made
            tempAdjacentVertices = this.getNeighbors(adjacentVertices.get(count));
            
            //These two for loops compare every neighbor from both lists.  If the
            //lists have a similar neighbor then a triangle is counted.
            for (Integer i: adjacentVertices){
                for (Integer j : tempAdjacentVertices){
                    if (i == j){
                        numberOfTriangles++;
                    }
                }
            }
            count++;
        }
        //this number has to be divided in half because of the nature of the algorithm
        //above.  Each triangle is counted twice, so the number must be divided by 2
        numberOfTriangles = numberOfTriangles / 2;
        return numberOfTriangles;
    }
    
    //This method works the same way numberOfTrianglesIncidentToVertex() does, only
    //it recordes the names of the vertices.
    public ArrayList<ArrayList<String>> listOfTrianglesIncidentToVertex(int index){
        int count = 0;
        int marker = 0;
        ArrayList<ArrayList<String>> triangleList = new ArrayList();
        List<Integer> adjacentVertices = this.getNeighbors(index);
        List<Integer> tempAdjacentVertices;
        int size = adjacentVertices.size();
        
        while (count < size){
            tempAdjacentVertices = this.getNeighbors(adjacentVertices.get(count));
            
            for (Integer i : adjacentVertices){
                for (Integer j : tempAdjacentVertices){
                    if (i == j){
                        //Here if a triangle is found the triangleList is checked
                        //to see if it has the same triangle in it already.  If
                        //it does marker is set to 1 and the triangle will not 
                        //be added.
                        for (ArrayList<String> a : triangleList){
                            if (a.contains(this.getVertex(i)) && 
                                    a.contains(this.getVertex(adjacentVertices.get(count)))){
                                marker = 1;
                            }
                        }
                        if (marker != 1){
                            triangleList.add(new ArrayList());
                            triangleList.get(triangleList.size() - 1).add(this.getVertex(index));
                            triangleList.get(triangleList.size() - 1).add(this.getVertex(adjacentVertices.get(count)));
                            triangleList.get(triangleList.size() - 1).add(this.getVertex(i));
                        }
                    }
                    //here marker is set back to 0 to ensure no triangle is skiped
                    marker = 0;
                }
            }
            count++;
        }
        return triangleList;
    }
    
    //This mehtod computes the cluster percentage of an individual.
    public double clusterIndividual(int index){
        double clusterPercentage;
        
        double divisor = ((double)(this.getDegree(index)*(this.getDegree(index) - 1) / 2));
        
        //This ensures that the calculation below is not divided by 0 and returns
        //false if it is.
        if (divisor == 0.0){
            return 0.0;
        }
        
        clusterPercentage = ((double)(this.numberOfTrianglesIncidentToVertex(index)
                / divisor))
                * 100.0;
        
        return clusterPercentage;
    }
    
    //This method computes the clustering average
    public double averageClustering(){
        double totalAverage = 0.0;
        
        int size = this.getSize();
        //This loop finds the sum of all the cluster percentages
        for (int i = 0; i < size; i++){
            totalAverage += this.clusterIndividual(i);
        }
        
        //Here it is checkd to see if the divisor below will be 0.
        if (this.getSize() == 0){
            return 0.0;
        }
        
        totalAverage = ((double)1 / this.getSize()) * totalAverage;
        
        return totalAverage;
    }
    
    //This method finds if there is a connection between two vertecies.  A list
    //is made of all the vertecies attached to index1.
    public boolean isIndirectAcquaintance(int index1, int index2){
        List<Integer> connectedPersonnel;
        
        connectedPersonnel = this.dfs(index1);
        
        //Here the list is steamed and each element is compared to index2.  If
        //there is a match true is returned else false is.
        return connectedPersonnel.stream().anyMatch((i) -> (i == index2));
    }

}
