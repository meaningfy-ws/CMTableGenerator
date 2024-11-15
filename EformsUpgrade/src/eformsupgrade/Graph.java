/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eformsupgrade;

import java.util.ArrayList;
import java.util.List;

class Graph
{
    // A list of lists to represent an adjacency list
    List<List<Integer>> adjList = null;
    int nout;
    int getSize(){
        return nout;
    }
    // Constructor
    Graph(List<Edge> edges, int n)
    {
        nout=n;
        adjList = new ArrayList<>();
 
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }
 
        // add edges to the directed graph
        for (Edge edge: edges) {
            adjList.get(edge.source).add(edge.dest);
        }
    }
}