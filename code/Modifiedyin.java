package code;
/*
 * @author - Vinodh K 
 * K shortest path implementation
 */


import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.alg.*;


public final class Modifiedyin
{
   
    private Modifiedyin()
    {
    } 
   
    public static void main(String [] args) 
    {
   
        // create a graph 
    	//add the details of the graph, vertices and edges in the create Graph function
        DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> origGraph = createGraph();
       
        //The graph
        System.out.println("The graph is \n"+origGraph.toString());
        //yin's algorithm
        
        int k = 3; //set the k here
        String startingnode = "1"; // this is the starting node
        String dest = "5"; //this is the ending node
        yinalgo(origGraph,startingnode,dest,k); //function which finds the k shortest paths
       
       
    }
    
    /*
     * @param : The original directed weighted graph
     */
    public static void yinalgo(DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> origGraph,String startingnode, String dest,int k)
    {
    	
        DijkstraShortestPath<String, DefaultWeightedEdge> sp = new  DijkstraShortestPath<String, DefaultWeightedEdge>(origGraph,startingnode,dest);
        
        List<DefaultWeightedEdge> shortestpath = new ArrayList<DefaultWeightedEdge>(sp.getPathEdgeList());
        double length = sp.getPathLength();
        List<String> deviationnodes = new ArrayList<String>();
        
        int i=0;
        System.out.println("The shortest path is : \n");
        for(i=0;i<shortestpath.size();i++)
        {
        	System.out.println(shortestpath.get(i));
        	deviationnodes.add(shortestpath.get(i).toString().split("[\\(: \\)]")[1]);
        }
        System.out.println("");
       deviationnodes.add(shortestpath.get(i-1).toString().split("[\\(: \\)]")[4]);
      
       //initial setting - list b should contain all the nodes of the shortest path
       List<Data> lista = new ArrayList<Data>();  //this will finally contain the k shortest paths
       List<Data> listb = new ArrayList<Data>();   //the shortest path in this list will be put into lista in every iteration
    //   for(i=0;i<deviationnodes.size();i++)
    	//   System.out.println(deviationnodes.get(i));
      Data temp = new Data(deviationnodes,length);
     
      listb.add(temp);
      int numberofpaths = 0;
      int returnstatus = 1;  //not needed ....... just to see whether the while loop was broken or whether the condition was satisfied
      //the temp graph is the one in which all the operations are done
      DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> tempgraph = origGraph;
      
      //the method starts now
      while(numberofpaths<=k)
      {
    	  //finding minimum in listb
    	  int min = 0;   //contains the index of the smallest length path in list b
    	  for(i=0;i<listb.size();i++)
    	  {
    		  if(listb.get(i).pathlength<listb.get(min).pathlength)
    			  min = i;
    			  
    	  }
    	  int numbmin = 0;   //if we have (k - number of paths already) paths of same length in listb and they have the shortest length then break
    	  for(i=0;i<listb.size();i++)
    	  {
    		  if(listb.get(i).pathlength==listb.get(min).pathlength)
    			numbmin++;  
    	  }
    	  if(numberofpaths+numbmin>k)
    	  {
    		  for(i=0;i<listb.size();i++)
    		  {
    			  if(listb.get(i).pathlength==listb.get(min).pathlength)
    			  {
    				  lista.add(new Data(listb.get(i).path,listb.get(i).pathlength));
    				  listb.remove(i);
    				
    			  }
    		  }
    		  returnstatus = 0;
    		  break;
    	  }
    	  
    	  //else
    	  //iteration k+1 starting
    	  numberofpaths++;
    	  //temp1 is what i do my operations with
    	  Data temp1 = new Data(listb.get(min).path,listb.get(min).pathlength);
    	  //adding in list a
    	  lista.add(temp1);
       	  listb.remove(min);
   
    	  tempgraph = origGraph;
    	  //remove the edges containing ith node to i+1th node in the paths tree
    	  int j=0;
    	//  System.out.println("Temp.path.size is "+temp1.path.size());
    	  for(i=0;i<temp1.path.size()-1;i++)
    	  {
    		  String devnode = temp1.path.get(i);
    	//	  System.out.println("Devnode is "+devnode);
    		  for(j=0;j<lista.size();j++)
    		  {
    			
    			  if(lista.get(j).path.subList(0, i+1).equals(temp1.path.subList(0, i+1)))
    			  {
    				  //set that graph edge in temp graph to infinity
    			//	  System.out.println("The edge removed is "+lista.get(j).path.get(i)+" "+lista.get(j).path.get(i+1));
    				  tempgraph.removeEdge(lista.get(j).path.get(i), lista.get(j).path.get(i+1));
    			  }
    		  }
    		  if(i>0)
    			  tempgraph.removeAllVertices(temp1.path.subList(0, i-1));
    		  
    		  //finding the shortest path from that deviationnode to the last node
    		  sp = new  DijkstraShortestPath<String, DefaultWeightedEdge>(tempgraph,temp1.path.get(i),dest);
    		 //if no path exists skip the remaining operations
    		  if(sp.getPathEdgeList()==null)
    			  continue;
    	     
    		  shortestpath = new ArrayList<DefaultWeightedEdge>(sp.getPathEdgeList());
    	      
    		  //length is the initia llength till the deviation node plus sp.pathlength
    	      //calculating initial length
    	     length = 0;
    	      for(j=0;j<i;j++)
    	      {
    	    	  length+= origGraph.getEdgeWeight(origGraph.getEdge(temp1.path.get(j),temp1.path.get(j+1)));
    	      }
    	      
    	      length += sp.getPathLength();  // this will add the initial length to the length of the shortest path from the deviation node till the destination
    	     deviationnodes = new ArrayList<String>();
    	     for(j=0;j<i;j++)
    	     {
    	    	 deviationnodes.add(temp1.path.get(j));
    	     }
    	       // System.out.println("The shortest path is : \n");
    	     int loop=0;
    	     for(loop=0;loop<shortestpath.size();loop++)
    	     {
    	     // 	System.out.println(shortestpath.get(loop));
    	       	deviationnodes.add(shortestpath.get(loop).toString().split("[\\(: \\)]")[1]);
             }
         //   System.out.println("");
   	       deviationnodes.add(shortestpath.get(loop-1).toString().split("[\\(: \\)]")[4]);
    	    listb.add(new Data(deviationnodes,length));  
    		  
    		  
    	  }
    	//  System.out.println("List B is : ");
    	 //print(listb);
        	  
    	  
    	  
    	  
    	 
      }
      //print list a which contains all the shortest paths 
   //   System.out.println(returnstatus);
      int j=0;
      for(i=0;i<k;i++)
      {
    	  System.out.println(i+1+"th Shortest Path : "+"Length :"+lista.get(i).pathlength);
    	  for(j=0;j<lista.get(i).path.size();j++)
    	  {
    		  System.out.println(lista.get(i).path.get(j));
    	  }
    	  System.out.println();
      }
    	  
       
       System.out.println();
       
       
       
    }
    public static void print(List<Data> listb)
    {
    	int i=0;
    	int j=0;
    	 for(i=0;i<listb.size();i++)
         {
       	  System.out.println("Length :"+listb.get(i).pathlength);
       	  for(j=0;j<listb.get(i).path.size();j++)
       	  {
       		  System.out.println(listb.get(i).path.get(j));
       	  }
       	  System.out.println();
         }
    }
    private static DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> createGraph()
    {
        DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> g =
            new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        

        // add the vertices
        g.addVertex("1");
        g.addVertex("2");
        g.addVertex("3");
        g.addVertex("4");
        g.addVertex("5");
       
        // add edges to create linking structure
        DefaultWeightedEdge e1 = (DefaultWeightedEdge)g.addEdge("1","2");
        g.setEdgeWeight(e1, 0);
        e1 = g.addEdge("2", "5");
        g.setEdgeWeight(e1, 0);
        e1 = g.addEdge("1","4");
        g.setEdgeWeight(e1, 3);
        e1 = g.addEdge("4","5");
        g.setEdgeWeight(e1, 0);
        e1 = g.addEdge("1","3");
        g.setEdgeWeight(e1, 1);
        e1 = g.addEdge("3","5");
        g.setEdgeWeight(e1, 2);
        e1 = g.addEdge("2","4");
        g.setEdgeWeight(e1, 2);
        e1 = g.addEdge("3","4");
        g.setEdgeWeight(e1, 0);

        return g;
    }

}