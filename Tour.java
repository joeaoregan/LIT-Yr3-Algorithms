/*
 * Joe O'Regan
 * K00203642
 * Algorithms CA1
 */


public class Tour {
	private Node first, last;   	// A reference to the "first" node of the circular linked list
	private int tourSize = 0;		// The number of Nodes in the tour

	 /**
     * Node represents a Node for the linked list Tour, and contains a two dimensional point object, and a
     * pointer to the next node in the list.
     */
	private class Node{
		private Point p;			// Node contains a point
		private Node next;			// The next node
	}
	
	// Create an empty tour
	public Tour()
	{
		first = null;				// If the tour is empty the first Node will be null
		last = null;				// So will the last node
	}
	
	// OK For debugging purposes only, make a constructor that takes four points as arguments,
	// and constructs a circular linked list using those four Point objects
	public Tour(Point a){
		Node nodeA = new Node();
		nodeA.p = a;
		
		nodeA.next = null;
		this.first = nodeA;			// The first Node in the list
		this.last = nodeA;			// The last Node in the list
	}
	
	public Tour(Point a, Point b, Point c, Point d){	// OK Create a 4 point tour a->b->c->d->a (full circle back to beginning)
		// Declare Nodes
		Node nodeA = new Node();
		nodeA.p = a;				// (100.0, 100.0)
		Node nodeB = new Node();
		nodeB.p = b;				// (500.0, 100.0)
		Node nodeC = new Node();
		nodeC.p = c;				// (500.0, 500.0)
		Node nodeD = new Node();	
		nodeD.p = d;				// (100.0, 500.0)
				
		nodeA.next = nodeB;			// Set next nodes
		nodeB.next = nodeC;
		nodeC.next = nodeD;
		nodeD.next = null;			// Set the last next to null
		
		// Set the first and last nodes of the tour
		this.first = nodeA;			// The first Node in the list
		this.last = nodeD;			// The last Node in the list
    }

	
	/**
     * Show prints a list of all point objects in the Tour in the order they are currently in through standard output.
     */
	public void show() {			// Print the tour to standard output
		// Loop through the tour, and display each point
		for(Node current = first; current != null; current = current.next)
			StdOut.print(current.p);// Display each point in the tour

		StdOut.print("\n");			// Some unnecessary formatting
	}

	
	/** 
	 * Number of points on tour 
	 */
	public int size(){
		tourSize = 0;										// Reset the number of nodes in the tour
		if (first == null) return tourSize;					// If the first Node is null, the counter remains 0

		for(Node current = first; current != null; current = current.next)					
			++tourSize;										// Increment the number of nodes in the tour

		return tourSize;									// Null - Add in the final Node
	}
	
	
	/**
     * Draw utilizes the drawTo method in the Point class to display the points with connecting lines
     * as a way to visualize the current Tour.
     */
	public void draw() {									// Draw the tour to standard draw
		if (first == null) return;							// if no points, no need to to draw line
						
		for(Node current = first; current != last; current = current.next) {
			if(current.next != null) {						// Everything except the last node
				current.p.drawTo(current.next.p);			// Draw a line from the current node to the next node

				StdOut.print(current.p + " to " + current.next.p + "\n");	// The points to connect
			}
		}
		
		if(first.next != null)	{							// No need to draw line if only one point
			last.p.drawTo(first.p);							// Draw a line from the first to the last			

			StdOut.print(last.p + " to " + first.p + "\n");	// Show the line from last to first
		}
	}
		
	
	/**
     * The distance method iterates through all of the current nodes in the linked list, calculates the
     * total length of the Tour as a double, and returns it.
     */
	public double distance() {									// Return the total distance of the tour
		double tourLength = 0.0;								// Total length of the tour		
		if (first == null || first.next == null) {				// If there's no points or 1 point in the tour 
			return tourLength;									// no need to add to the tour length
		}
		
		for(Node current = first; current != last; current = current.next) {
			tourLength += current.p.distanceTo(current.next.p);	// Add distance between current node & the next node
		}
		
		return tourLength += last.p.distanceTo(first.p);		// Add distance from last node to first to tour length	
	}	

		
    /**
     * insertSmallest reads in the next point and adds it to the current tour after the point where it results 
     * in the least possible increase in tour length.  If there is more than one point that is equidistant,  
     * it will be inserted after the first point that is discovered.
     */
	public void insertSmallest(Point p) {						// Insert p using smallest increase heuristic
		Node newNode = new Node();                   			// Create a new Node to hold the point p
		Node smallestNode = new Node();							// Keep track of the node which affects tour length least
		newNode.p = p;											// Set the new nodes point to p
		newNode.next = null;									// Set the new nodes next node to null
		double newTotalTourLength = Double.POSITIVE_INFINITY;	// What the tour length will be when a new node is inserted
		double increaseInTourLength = Double.POSITIVE_INFINITY;
		
		/** Create a node if there is none, OR add a new Node to the tour */
		if (first == null){										// If the first Node is empty
			first = newNode;									// The new node is the first node
			last = first;										// One node so the last node is the first node
			return;
		}

		for(Node current = first; current != null; current = current.next) {
			last = current;										// Set the last node
			if(current.next != null){							// Everything except the last node
				increaseInTourLength = (current.p.distanceTo(newNode.p) + newNode.p.distanceTo(current.next.p)) - current.p.distanceTo(current.next.p);

				if(increaseInTourLength < newTotalTourLength) {
					newTotalTourLength = increaseInTourLength;
					smallestNode = current;						// Set the current node as the point to insert a new node after
				}
			}
			else{												// The last node
				increaseInTourLength = (last.p.distanceTo(newNode.p) + newNode.p.distanceTo(first.p)) - last.p.distanceTo(first.p);

				if(increaseInTourLength < newTotalTourLength) {
					newTotalTourLength = increaseInTourLength;
					smallestNode = last;						// Set the last node as the point to insert a new node after
					last = newNode;								// This makes the new node the last node
				}					
			}
		} // end for
		
		// Insert the node
		newNode.next = smallestNode.next;						// Point the new Node to the smallest Nodes next Node
		smallestNode.next = newNode;							// Redirect the smallest Node to the new Node
	}	
		
	
    /**
     * insertNearest reads in the next point and adds it to the current tour after the point to which it is closest.
     * If there are more than one equidistant points, it will be added after the first point that is discovered.
     */
	public void insertNearest(Point p) {
		Node newNode = new Node();                   							// Create a new Node to hold the point p
		Node closestNode = new Node();											// Node the new Node is closest to
		newNode.p = p;															// Set the new nodes point to p
		newNode.next = null;													// Set the new nodes next pointer to null
		double nearestDistance = Double.POSITIVE_INFINITY;						// Represent infinity in Java
		
		/** Create a node if there is none, OR add a new Node to the tour */
		if (first == null){														// If the first Node is empty
			first = newNode;													// One node in the list

			return;
		} 
		
		for(Node current = first; current != null; current = current.next) {	// Loop through list of Nodes
			last = current;														// Set the last node
			
			if(current.next != null && current.p.distanceTo(newNode.p) < nearestDistance){ // Distance between each list node and the new node
				nearestDistance = current.p.distanceTo(newNode.p);
				closestNode = current;
			}
		}
		
		if(last.p.distanceTo(newNode.p) < nearestDistance) {	// Check last node
			nearestDistance = last.p.distanceTo(newNode.p);		// Set nearest distance
			
			closestNode = last;									// The last node is the closest, insert new node at end of list
			last = newNode;										// The new node is now the last node
		}

		newNode.next = closestNode.next;						// Point the new Node to the smallest Nodes next Node
		closestNode.next = newNode;								// Redirect the smallest Node to the new Node
	}

	
	
	
	// Main method for Testing
	public static void main(String[] args) {
		// Need to implement before calling draw - get dimensions
        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(0, 600);
        StdDraw.setYscale(0, 600);
        StdDraw.setPenRadius(.006);								// Will decide the size of the point to be drawn
        
		// define 4 points forming a square
		Point a = new Point(100.0, 100.0);
		Point b = new Point(500.0, 100.0);
		Point c = new Point(500.0, 500.0);
		Point d = new Point(100.0, 500.0);
		Point e = new Point(50.0, 50.0);
		
		/** INSERT ONE POINT */

		Tour onePointTour = new Tour(a);
		onePointTour.show();
		/** Insert Smallest */
		StdOut.print("\nTour Size: " + onePointTour.size());
		StdOut.print("\nTour Distance before insert: " + onePointTour.distance() + "\n");
		onePointTour.insertSmallest(b);
		StdOut.print("\nTour Distance after insert b: " + onePointTour.distance() + "\n");	// New distance
		onePointTour.show();
		onePointTour.insertSmallest(c);
		StdOut.print("\nTour Distance after insert c: " + onePointTour.distance() + "\n");	// New distance
		onePointTour.show();
		onePointTour.insertSmallest(d);
		StdOut.print("\nTour Distance after insert d: " + onePointTour.distance() + "\n");	// New distance
		onePointTour.show();
		onePointTour.insertSmallest(e);
		StdOut.print("\nTour Distance after insert e: " + onePointTour.distance() + "\n");	// New distance
		onePointTour.show();
		onePointTour.draw();
		StdOut.print("\nNEW Tour Size: " + onePointTour.size() + "\n");	// Displays the number of Nodes in the tour
		StdOut.print("\nTour Distance: " + onePointTour.distance());	// New distance
		
		/** Insert Nearest 
		onePointTour.insertNearest(b);
		StdOut.print("\nTour Distance after insert b: " + onePointTour.distance() + "\n");	// New distance
		onePointTour.show();
		onePointTour.insertNearest(c);
		StdOut.print("\nTour Distance after insert c: " + onePointTour.distance() + "\n");	// New distance
		onePointTour.show();
		onePointTour.insertNearest(d);
		StdOut.print("\nTour Distance after insert d: " + onePointTour.distance() + "\n");	// New distance
		onePointTour.show();
		onePointTour.insertNearest(e);
		StdOut.print("\nTour Distance after insert e: " + onePointTour.distance() + "\n");	// New distance
		onePointTour.show();
		onePointTour.draw();
		StdOut.print("\nNEW Tour Size: " + onePointTour.size() + "\n");	// Displays the number of Nodes in the tour
		StdOut.print("\nTour Distance: " + onePointTour.distance());	// New distance
		*/
		/*
		// Set up a Tour with those four points
		// The constructor should link a->b->c->d->a
		Tour squareTour = new Tour(a, b, c, d);

		// Output the Tour
		//squareTour.show();	// Displays the points in the tour
		
		StdOut.print("\nINSERT SMALLEST");
		
		StdOut.print("\nTour Size: " + squareTour.size());	// Displays the number of Nodes in the tour
		
		//squareTour.draw();

		StdOut.print("\nTour Distance before insert: " + squareTour.distance());	// Displays the number of Nodes in the tour
		//squareTour.distance();
		
		// Insert a new point into squareTour then call show and draw
		Point e = new Point(0.0, 300.0);
		
		squareTour.insertSmallest(e);

		StdOut.print("\nNEW Tour Size: " + squareTour.size() + "\n");	// Displays the number of Nodes in the tour
		squareTour.show();
		squareTour.draw();

		StdOut.print("\nTour Distance after insert: " + squareTour.distance());	// New distance
		*/
		
		
		/*
		StdOut.print("\n\nINSERT NEAREST");
		

		StdOut.print("\nTour Size: " + squareTour.size());	// Displays the number of Nodes in the tour

		StdOut.print("\nTour Distance before insert: " + squareTour.distance());	// Displays the number of Nodes in the tour
		//squareTour.distance();
		
		// Insert a new point into squareTour then call show and draw
		Point e = new Point(0.0, 300.0);
		Point f = new Point(300.0, 0.0);
		
		squareTour.insertSmallest(e);
		
		squareTour.insertNearest(f);

		StdOut.print("\nNEW Tour Size: " + squareTour.size() + "\n");	// Displays the number of Nodes in the tour
		squareTour.show();
		squareTour.draw();

		StdOut.print("\nTour Distance after insert: " + squareTour.distance());	// New distance		
		*/
		}
}