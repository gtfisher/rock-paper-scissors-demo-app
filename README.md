# Rock Paper Scissors Spring Boot Example App
  
  Based on this blog postby Jan Kronquist  on aggragate + [Event Sourcing Distilled](https://www.jayway.com/2013/03/08/aggregates-event-sourcing-distilled/)
  this is a demo app that shows CQRS/Event Sourcing Spring Boot Java Application. 
  
  ## This is still WIP at this time
    
  The Command part works ok as can be seen from the tests, but the GameProjection side and views are not complete.
  
  This is a Spring Boot App with a Controller in place to support the just the command side
    
  The REST API currently supports
  
  Create Game POST http://localhost:8080/api/v1/rps
   this must provide a header of SimpleIdentity set to "Some email address"
  
  This will return a new Game ID
   
   The api then expects two moves to be made for that game id using a PUT requests as follows: 
  
  Make Move   PUT http://localhost:8080/api/v1/rps/move/<gmaeid>/<move>
  this must also provide a header of SimpleIdentity set to "Some email address"
  
  where move is one of rock,paper,scissors
  
  The second move must provide a different email address
  
 For the time being the Command controller will display the contents of the event store at the following path, so the result of the game can be determined
 HTTP GET http://localhost:8080/api/v1/rps/events/<gameId>
 
 This example is TBC
 
 ###TODO
 * Add second controller for query side
 * Sort out Event propagation
 * get GameProjection working
 
 
 
  
  
  
  
  
    
    


