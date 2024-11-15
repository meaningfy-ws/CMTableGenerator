/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eformsupgrade;

/**
 *
 * @author achid
 */
class Edge
{
    public final int source, dest;
 
    private Edge(int source, int dest)
    {
        this.source = source;
        this.dest = dest;
    }
 
    // Factory method for creating an immutable instance of `Edge`
    public static Edge of(int a, int b) {
        return new Edge(a, b);        // calls private constructor
    }
}
