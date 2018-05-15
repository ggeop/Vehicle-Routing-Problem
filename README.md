# VehicleRoutingProblem

The code is about a java application for dealing with the Vehicle Routing Problem.
In this impelementation we should include the depot, the customers, the set of routes, the solution and the
distance matrix.

### Local Search
The algorythm includes a local search method for improving the initial solution. This local
search method will consider intra & inter-route relocations. This means that at each iteration, the method
should explore all potential relocations of the customers within their routes. The relocation yielding the
best solution cost improvement should be selected to be applied to the candidate solution. The method
should terminate if no improving intra-route relocation can be identified
