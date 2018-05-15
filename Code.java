package vrp;

import java.util.ArrayList;
import java.util.Random;




public class vrp {

	public static void main(String[] args) 
	{
		Random ran = new Random(1);

		//Set up Input for VRP
		int numberOfCustomers  = 30;

		//Create the depot
                Node depot = new Node();
		depot.x = 50;
		depot.y = 50;
                depot.demand=0;

		//Create the list with the customers
		ArrayList <Node> customers = new ArrayList<Node>();
		for (int i = 0 ; i < numberOfCustomers; i++)
		{
                    Node cust = new Node();
                    cust.x = ran.nextInt(100);
                    cust.y = ran.nextInt(100);
                     
                    cust.demand = 4 + ran.nextInt(7);
                    cust.ID = i;
                    customers.add(cust);
                    System.out.println("customer ID is: "+cust.ID);
                    System.out.println("customer x is: "+cust.x);
                    System.out.println("customer y is: "+cust.y);
                    System.out.println("demand is: "+cust.demand);
                    System.out.println("");
		}


		//Build the allNodes array and the corresponding distance matrix
		ArrayList <Node> allNodes = new ArrayList<Node>();

		allNodes.add(depot);
		for (int i = 0 ; i < customers.size(); i++)
		{
			Node cust = customers.get(i);
			allNodes.add(cust);
		}

		for (int i = 0 ; i < allNodes.size(); i++)
		{
			Node nd = allNodes.get(i);
			nd.ID = i;
                        
		}
		// This is a 2-D array which will hold the distances between node pairs
		// The [i][j] element of this array is the distance required for moving 
		// from the i-th node of allNodes (node with id : i)
		// to the j-th node of allNodes list (node with id : j)
		double [][] distanceMatrix = new double [allNodes.size()][allNodes.size()];
		for (int i = 0 ; i < allNodes.size(); i++)
		{
			Node from = allNodes.get(i);

			for (int j = 0 ; j < allNodes.size(); j++)
			{
				Node to = allNodes.get(j);

				double Delta_x = (from.x - to.x);
				double Delta_y = (from.y - to.y);
				double distance = Math.sqrt((Delta_x * Delta_x) + (Delta_y * Delta_y));

				distance = Math.round(distance);

				distanceMatrix[i][j] = distance;

			}
		}


		//Nearest Neighbor
		//iterative procedure: at each iteration insert the nearest neighbor into the solution

                // indicate that all customers are non-routed
		for (int i = 0 ; i < customers.size(); i++)
		{
			customers.get(i).isRouted = false;
		}
		// This is the solution object - It will store the solution as it is iteratively generated
		// The constructor of Solution class will be executed
                
                
		Solution s = new Solution();
              
                   for(int i=0 ; i<10 ; i++)
                {   
                    Route rt = new Route();
                    s.rts.add(rt);
                    s.rts.get(i).nodes.add(depot);
                } 
                 
                   int currentload;
for (int k =0 ; k < 10; k++)
                {
	
		 currentload =0;
             
		//INITIALIZATION
		
		//s.rts.get(k).nodes.add(depot);  // same thing - it is the same arraylist object
                
		//ITERATIVE BODY OF THE NN ALGORITHM
		//Q - How many insertions? A - Equal to the number of customers! Thus for i = 0 -> customers.size() 
		for (int i = 0 ; i < customers.size(); i++)
		{
			//this will be the position of the nearest neighbor customer -- initialization to -1
			int positionOfTheNextOne = -1;

			// This will hold the minimal cost for moving to the next customer - initialized to something very large
			double bestCostForTheNextOne = Double.MAX_VALUE;

			//This is the last customer of the route (or the depot if the route is empty)
			Node lastInTheRoute = s.rts.get(k).nodes.get(s.rts.get(k).nodes.size() - 1);


			//First Step: Identify the non-routed nearest neighbor (his position in the customers list) of the last node in the nodeSequence list
			for (int j = 0 ; j < customers.size(); j++)
			{
				// The examined node is called candidate
				Node candidate = customers.get(j);

				// if this candidate has not been pushed in the solution
				if (candidate.isRouted == false && (currentload + candidate.demand) <= s.rts.get(k).capacity )
				{        
                                       
                                         s.rts.get(k).load=s.rts.get(k).load-candidate.demand;
                                         
					//This is the cost for moving from the last to the candidate one
					double trialCost = distanceMatrix[lastInTheRoute.ID][candidate.ID];

					//If this is the minimal cost found so far -> store this cost and the position of this best candidate
					if (trialCost < bestCostForTheNextOne)
					{
						positionOfTheNextOne = j;
						bestCostForTheNextOne = trialCost;
					}
				}
                                
			}
           


			// Step 2: Push the customer in the solution

			// We have found the customer to be pushed!!!
			// He is located in the positionOfTheNextOne position of the customers list
			// Let's inert him and update the cost of the solution and of the route, accordingly
			// Don't forget to indicate that this customer is now routed
			if     (positionOfTheNextOne != -1) {
	        	   
			//Give him a name
			Node insertedNode = customers.get(positionOfTheNextOne);

			//Push him
			s.rts.get(k).nodes.add(insertedNode);
			//route.nodes.add(insertedNode); // same thing 
			//s.rt.nodes.add(insertedNode);  // same thing

			// update the cost of the solution!
			// What is the cost augmentation???
			// We have already found it!!! It is the bestCostForTheNextOne
			// Let's see if we are correct
			double testCost = distanceMatrix[lastInTheRoute.ID][insertedNode.ID];

			if (testCost != bestCostForTheNextOne)
			{
				//If we are not correct
				System.out.println("Something has gone wrong with the cost calculations !!!!");
			}

			s.rts.get(k).cost = s.rts.get(k).cost + bestCostForTheNextOne;
			currentload= 	currentload+ insertedNode.demand ;
			//route.cost = route.cost + bestCostForTheNextOne;
			//s.rt.cost =  s.rt.cost + bestCostForTheNextOne; // same thing

			// Update the isRouted status for the ode just inserted in the solution
			insertedNode.isRouted = true;

			}
			else break;
		}
		// All customers have been pushed in the solution
                
		// Now we have to push the depot in the final point of the route -- finalize tsp solution
		Node lastInTheRoute = s.rts.get(k).nodes.get(s.rts.get(k).nodes.size() - 1);
		s.rts.get(k).nodes.add(depot);

		//The cost is augmented by moving from the last customer back to the depot
		s.rts.get(k).cost = s.rts.get(k).cost + distanceMatrix[lastInTheRoute.ID][depot.ID];
		//route.cost = route.cost + distanceMatrix[lastInTheRoute.ID][depot.ID];
		//s.rt.cost =  s.rt.cost + distanceMatrix[lastInTheRoute.ID][depot.ID];; // same thing
                }
                //Solution
                System.out.println("The solution of nodes is: ");
                for (int p = 0 ; p < 10; p++)
                {   System.out.print("Vehicle number("+p+"): ");
                    for (int i = 0 ; i <s.rts.get(p).nodes.size(); i++)
                    {
                        System.out.print(s.rts.get(p).nodes.get(i).ID + "  ");	
                    }
                    System.out.println();
                }
                
               //The total cost 
               double total;
               total=0;
               System.out.print("The total cost is: ");
               for (int p = 0 ; p < 10 ; p++)
	               {
	            	      
	            	   total=total+s.rts.get(p).cost;
	                    
	               }
               System.out.println(total);	     
	
	


	//START OF LOCAL SEARCH CODE/////////////////////////////////////////////////////////////////////////
	//
	//The NN Solution has been generated
	//
	//Local Search
boolean terminationCondition = false;
for(int routeNumber=1; (routeNumber<9 && terminationCondition == false ); routeNumber++)
{
	
	//this is a counter for holding the local search iterator
	int localSearchIterator = 0;

	
		//This is an object for holding the best swap move that can be applied to the candidate solution
		SwapMove sm = new SwapMove();

		// Until the termination condition is set to true repeat the following block of code
		while (terminationCondition == false)
		{
			//Initialize the swap move sm
			sm.positionOfFirst = -1;
			sm.positionOfSecond = -1;
			sm.moveCost = Double.MAX_VALUE;
			
			//With this function we look for the best swap move
			//the characteristics of this move will be stored in the object sm
			for(int a=0; a<9;a++) 
		      {
			findBestSwapMove(sm, s, distanceMatrix, routeNumber, a);
		      
			// If sm (the identified best swap move) is cost improving move, or in other words
			// if the current solution is not a local optimum
			if (sm.moveCost < 0)
			{
				//This is a function applying the swap move sm to the candidate solution
				applySwapMove(sm, s, distanceMatrix, routeNumber,a);

				//my function just to visualize things
				
			}
			else
			{
				//if no cost improving swap move was found,
				//or in other words if the current solution is a local optimum
				//terminate the local search algorithm
				terminationCondition = true;
			}
			
			localSearchIterator = localSearchIterator + 1;
		   }	
		}
	
		//New Solution
		System.out.println("");
	        System.out.println("The new solution of nodes is: ");
	        for (int p = 0 ; p < 10; p++)
	        {   System.out.print("Vehicle number("+p+"): ");
	            for (int i = 0 ; i <s.rts.get(p).nodes.size(); i++)
	            {
	                System.out.print(s.rts.get(p).nodes.get(i).ID + "  ");	
	            }
	            System.out.println();
	        }
		
		
		 //The total cost after local Search
		System.out.println("");	
	    System.out.print("New better solution is: ");
	    total=0;
	    for (int p = 0 ; p < 10 ; p++)
	        {
	     	      
	     	   total=total+s.rts.get(p).cost;
	             
	        }
	    System.out.println(total);
	    System.out.println("The count of iterations: "+localSearchIterator);
}

	}


private static void findBestSwapMove(SwapMove sm, Solution s, double[][] distanceMatrix, int routeNumber, int a) 
{ 
	{ 
		  
	//This is a variable that will hold the cost of the swap relocation move
    double bestMoveCost = Double.MAX_VALUE;
    
    //We will iterate through all customer nodes that can be swapped with another node
    for (int firstIndex = 1; firstIndex <s.rts.get(routeNumber+a).nodes.size()-1; firstIndex++)
    {
    	// Node A: The predecessor of B
        Node A = s.rts.get(routeNumber).nodes.get(firstIndex - 1);
        // Node B: The node to be swapped
        Node B = s.rts.get(routeNumber).nodes.get(firstIndex);
        //Node C: The successor of B
        Node C = s.rts.get(routeNumber).nodes.get(firstIndex + 1);
    
       //We will go through every node that can be swapped with B
        for (int secondInd = firstIndex + 1; secondInd < s.rts.get(routeNumber+a).nodes.size()-1; secondInd ++)
        {
        	 //Why do we have selected secIndex to start from firstIndex + 1?
            //Symmetric move!!! --- No reason to swap pair B and E and then E and B !!! --- It's the same thing!!!
        	
        	// Node D: The predecessor of E
            Node D = s.rts.get(routeNumber+a).nodes.get(secondInd - 1);
            //Node E: The customer to be swapped with B
            Node E = s.rts.get(routeNumber+a).nodes.get(secondInd);
            //Node F: The successor of E
            Node F = s.rts.get(routeNumber+a).nodes.get(secondInd + 1);
            
            //Based on the mechanics of the move two cases may arise
            //1. the swapped are consecutive nodes (secondInd == firstIndex + 1), in other words B == D and C == E
            //2. the swapped are non-consecutive nodes (secondInd > firstIndex + 1)

            double costRemoved = 0; 
            double costAdded = 0;
            
            if (secondInd == firstIndex + 1)
            {
            	// The arcs A-B, B-C and C-F are broken
                costRemoved =  distanceMatrix[A.ID][B.ID] + distanceMatrix[B.ID][C.ID] +  distanceMatrix[C.ID][F.ID];
                
                // The arcs A-C, C-B and B-F are created
                costAdded = distanceMatrix[A.ID][C.ID] + distanceMatrix[C.ID][B.ID] +  distanceMatrix[B.ID][F.ID] ;
            }
            else
            {
            	// The arcs A-B, B-C, D-E and E-F are broken
                double costRemoved1 =  distanceMatrix[A.ID][B.ID] + distanceMatrix[B.ID][C.ID] ;
                double costRemoved2 =  distanceMatrix[D.ID][E.ID] + distanceMatrix[E.ID][F.ID] ;
                costRemoved = costRemoved1 + costRemoved2;
                
                
              	// The arcs A-E, E-C, D-B and B-F are created
                double costAdded1 =  distanceMatrix[A.ID][E.ID] + distanceMatrix[E.ID][C.ID] ;
                double costAdded2 =  distanceMatrix[D.ID][B.ID] + distanceMatrix[B.ID][F.ID] ;
                costAdded = costAdded1 + costAdded2 ;
            }
            
            //This is the cost of the move, or in other words
            //the change that this move will cause if applied to the current solution
            double moveCost = costAdded - costRemoved;
                
            //If this move is the best found so far
            if (moveCost < bestMoveCost)
            {
            	//set the best cost equal to the cost of this solution
                bestMoveCost = moveCost;

                //store its characteristics
                sm.positionOfFirst = firstIndex;
                sm.positionOfSecond = secondInd;
                sm.moveCost = moveCost;
            }
        }
    }
   
	}
}


//This function applies the relocation move sm to solution s
private static void applySwapMove(SwapMove sm, Solution s, double[][] distanceMatrix, int routeNumber, int a) 
{
	Node swapped1 = s.rts.get(routeNumber).nodes.get(sm.positionOfFirst);
	Node swapped2 = s.rts.get(routeNumber+a).nodes.get(sm.positionOfSecond);

	//Simple Way
	//set the element in the sm.positionOfFirst of the route to be swapped2 and 
	//set the element in the sm.positionOfSecond of the route to be swapped1  
	s.rts.get(routeNumber).nodes.set(sm.positionOfFirst, swapped2);
	s.rts.get(routeNumber+a).nodes.set(sm.positionOfSecond, swapped1);

	//More Complex way -- Take out a node and reinsert the other one ath the empty position
	//s.rt.nodes.remove(sm.positionOfFirst);
	//s.rt.nodes.add(sm.positionOfFirst, swapped2);
	//    
	//s.rt.nodes.remove(sm.positionOfSecond);
	//s.rt.nodes.add(sm.positionOfSecond, swapped1);

	// just for debugging purposes
	// to test if everything is OK
	double newSolutionCost = 0;
	for (int i = 0 ; i < s.rts.get(routeNumber+a).nodes.size() - 1; i++)
	{
		Node A = s.rts.get(routeNumber).nodes.get(i);
		Node B = s.rts.get(routeNumber+a).nodes.get(i + 1);
		newSolutionCost = newSolutionCost + distanceMatrix[A.ID][B.ID];
	}

	//update the cost of the solution and the corresponding cost of the route object in the solution
	//s.cost = s.cost + sm.moveCost;
	s.rts.get(routeNumber).cost = s.rts.get(routeNumber).cost + sm.moveCost;
}
	 
}

class Node 
{
	int x;
	int y;
	int ID;
    int demand;

	// true/false flag indicating if a customer has been inserted in the solution
	boolean isRouted; 
      
	Node() 
	{
	}
}

class Solution 
{
	double cost;
	ArrayList<Route> rts;
    Route rt;
    double load;

	//This is the Solution constructor. It is executed every time a new Solution object is created (new Solution)
	Solution ()
	{
	// A new route object is created addressed by rt
		// The constructor of route is called
		rts = new ArrayList<Route>();
                rt = new Route();
		cost = 0;
	}
     
}

class Route 
{
	ArrayList <Node> nodes;
	double cost;
    double load;
    double capacity;
    
	//This is the Route constructor. It is executed every time a new Route object is created (new Route)
	Route() 
	{
		cost = 0;
		load =0;
                capacity = 50;
		// A new arraylist of nodes is created
		nodes = new ArrayList<Node>();
	}
}


class RelocationMove 
{
	int positionOfRelocated;
	int positionToBeInserted;
	double moveCost;

	RelocationMove() 
	{
	}
}

class SwapMove 
{
    int positionOfFirst;
    int positionOfSecond;
    
    double moveCost;
    
    SwapMove() 
    {
        
    }
}