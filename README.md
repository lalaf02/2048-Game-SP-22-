=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: ***
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D arrays: the 2D array store the values of each cell in the 2048 game.
  2D arrays are helpful for conveying rows and columns, and since 2048 is a
  game that is organized into rows and cols, I though that 2D arrays would be
  a reasonable way to store information. Also, since the size of the game board
  is fixed I decided that 2D arrays were a good since their size is immutable.
  Additionally, 2D arrays have a quick look up time, and it is incredibly helpful
  to be able to look up elements quickly.

  2. Collections & Sets: I used these to implement undo functionality in my game.
  Collections are mutable, which makes it easy to add new game states to its end.
  Moreover, the functions available for collections (such as get and remove) helped
  me minimize the amount of code that I needed to write myself. When I submitted my
  proposal, I did not consider utilizing collections in the context of undo
  functionality; however, I was given feedback that they might be a good concept
  to implement.

  3. IO exceptions: I have a feature whereby the user can save the current game state
  to a file. What is specifically saved are the values of the 2D array (the values
  in play of the game). Upon saving the file, I use IO exceptions to make sure that
  there is not currently a file with the name I am intending to save the game's contents
  to. Also, I have a constructor that reads a file's inputs, assigned each line
  of the file, which should be a number, to a specific row and col. If the file is
  not of the correct length, I throw an exception, moreover if the content being
  read from the file is not valid game number, another exception is thrown.

  4. Testing: I utilized JUnit testing to test the rigidity/functionality of the
  internal, model state of my game. JUnit test was very helpful in the beginning
  and middle stages of my game when I was in the midst of debugging the functionality
  of the game.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  RunTwentyFourEightView: This is the class whereby I implement all
  the java swing operations that provide graphics to the game. The class
  handles the users interactions, such as mouse clicks, and
  allocates those events to the proper component.

  TwentyFourEightGameBoard: This class instantiates a twentyFourEightModel object,
  which is the model for the game. As the user has some type of interaction
  (presses a key or clicks a button), the model is updated.
  Whenever the model is updated, the game board repaints itself and
  updates its status JLabel to reflect the current state of the model.
  This class is also responsible for how the mouse clicks will be
  allocated to the proper component.

  TwentyFourEightModel: This model is completely independent of the
  view and controller. This class houses all the game's logic. In
  fact, the game can be entirely played in the class's main
  method


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  I had a lot of trouble with the logic of the game. I was not shifting the
  array's values properly when the move methods were invoked. I spent several
  hours merely debugging the model aspect of the game. Also, it took some
  time to get comfortable with java swing.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  I think that my game is relatively well encapsulated. The fact that most of
  the game functionality is housed in one class conveys this. There is some overlap
  in taking user inputs in the run and view classes; however, one of these classes
  focuses more on keyboard clicks and internal shift of the game state while the
  other is more dependent on user interactions with the other components of the JFrame.
  The private state of the game, itself, is well encapsulated as the other methods must
  use getters and setter to access the game's internal state. If I could refactor the game,
  I would try to put all the user interaction related methods in one class.



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
  I relied on the course material, and only the actual 2048 game (as game strategy and rules)
  for resources.
