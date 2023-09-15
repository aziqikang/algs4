# Algorithms - Princeton University

This repository contains my solutions for the online courses "Algorithms, Part I" and "Algorithms, Part II" from Princeton University.

**Coursera:**

[Algorithms, Part I](https://www.coursera.org/learn/algorithms-part1)

[Algorithms, Part II](https://www.coursera.org/learn/algorithms-part2)

**Text:** 

[_Algorithms, 4th ed._](https://algs4.cs.princeton.edu/home/), by Robert Sedgewick and Kevin Wayne

# Projects

## Percolation
[Project Specification](https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php)

"Percolation" calculates whether we can traverse through adjacent open sites to get from the top of the lattice to the bottom.
As an analogy, I like to think of water being poured over a lattice containing sites that are either open (holes!) or closed. The question now is whether the water percolate through the lattice...

![image](https://github.com/aziqikang/algs4/assets/142746919/fb741c91-9df0-4cc2-82e7-a5981943aec5)


### Calculating percolation using the Union Find Data Structure

The percolation problem implements the Union-Find, or Disjoint-Set, data structure, which simulates set partitioning.
Union Find uses an array id[] to append an id to each index; if the two elements corresponding to indexes _a_ and _b_ are such that id[_a_] = id[_b_], then the two elements are in the same equivalence class. 

The time complexity of normal Union-Find, which I found most intuitive, is _O(n)_. With optimizations, Sedgewick and Wayne's [WeightedQuickUnionUF](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/WeightedQuickUnionUF.html) takes _O(n)_ to construct and _O(log n)_ to merge equivalence classes and find the representative element of a set.

### Estimating the value of the _percolation threshold_ using a Monte Carlo simulation

Finally, the "Percolation" class is used to empirically estimate the value of the _percolation threshold_ for a square lattice. By running a Monte Carlo simulation and randomly opening sites, as shown below, we experimentally estimate when a lattice transitions from non-percolating to percolating state.

![image](https://github.com/aziqikang/algs4/assets/142746919/4809d9cf-4e10-415c-8cae-9e559e362f9d)

### Notes:

When I was working on this project two years ago, I ran into some issues (now fixed!) with backwashing.

In cases where a Percolation object _does_ percolate, some sites that aren't connected appear to be connected because the we union() sites at the top of the lattice to sites at the bottom of the lattice as a part of implementing Union-Find.

For example, running _PercolationVisualizer.java_ on _input10.txt_ results in the solution to the right rather than the solution to the left (this is mentioned in the FAQ!).

![image](https://github.com/aziqikang/algs4/assets/142746919/4314c926-6d67-4245-a066-6eb51fbc63ef)

The solution I implemented simply uses two Union-Find objects. Doing this seems to satisfy the Coursera autograder, but it's also pretty costly in terms of memory. I'd be interested in hearing about any other solutions out there!

