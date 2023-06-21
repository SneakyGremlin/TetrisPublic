# Tetris

> Note that for paths I use "." to denote repository root, "src",  (as is denominated by) IDEA INTELLIJ. 

### What is tetris? <br/>
Well it's a game that teaches children (subconsciously) about spatial constructs et cetera. <br/>

---

### How to play? 
You will first be presented with a Frame which asks you to "Click to Continue". Do that. <br/>
Upon this you will be presented with the game itself. The controls are simple: 

0. You may press 'SPACE' for the `welcomeFrame` and choose not to click the pretty button ( :( )
1. Press "A" to move the BlockCluster to the left.
2. Press "D" to move the BlockCluster to the right.
3. Press "S" to move the BlockCluster down (note that the default behaviour is descent so this expedites the process).
4. Press "Q" to rotate the BlockCluster left
5. Press "R" to rotate the BlockCluster right
6. Press "SPACE" to pause the game
7. Press "ESC" to end the game 

All normal rules of tetris follow:
1. A row is cleared once it has a row of blocks.
2. The game ends when you run out of space (i.e. there is a collision between a block in the top and the "floating" (please refer to exposition section) object).

When the game is over, you will be presented with a termination screen, which displays your calcuated score.

---

### Exposition on the game cycle and implementation 
> A "floating" BlockCluster is a cluster that is a part of the game world but the CellSpatialUnits do not register them (and thus DO NOT set their status to occupied i.e. there is a block present. <br/>
However, there is an exception to this rule: for the rendering of the game world i.e. in class GameBoard (where all the action listeners reside) the method `updateDisplayGrid()` must be used in concert with calls to 
> `Container's amalgamateGraphics()` which for a moment renders the "floating" object non-floating. I made this decision  to ease the process the process of updating the underlying grid itself (I have a workaround but this aligns with some gameConcepts I like: imaginary walls etc)

For brevity, I offer a gist of the application. For increased detail, it would be much more commodious if the reader reads the documentation in tandem with the code. <br/>

`classes.gameSpace.Container` is the underlying game space populated with `class.gameSpace.CellSpatialUnit`. This is analagous to an an empty grid container. 
'CellSpatialUnit' has a member variable occupie that determines whether or not there is a block there (see above for "floating" objects).

`classes.ui.gui.GameBoard` is the graphical component of the application. It has a member variable `grid` which contains `GraphicCell`, each of which contains a reference to the `CellSpatialUnit` of the `Container` class. It has has all the `Swing` components (the `JFrames` etc.)

`classes.ui,gui.GraphicsConstants`, `classes.GameConstants` contain constants to ensure Single Point of Control.

The enumerations `classes.ui.managers.PossibleUserInput`, `classes.gameObjects.compoundConstructs.Forms` and `.Orientations` were utilised for increase readibility. 

`classes.gameObjects.compoundConstructs.BlockCluster` is composed of `classes.gameObjects.Block`. 
>n.b. a block is either a block or null (well that applies for all Java objects but i ACTUALLY use this feature here).

In the package, `classes.ui.managers`, Generator and Updater are quite important. 

Firstly, `Generator`, is responsible for producing a new `BlockCluster` (`Updater` is responsible for maintaining references i.e. one reference for itself and another for `Container` (there is more on this later !!!)).
> `Generator` is a highly coupled with the enumeration `Forms`, the class `BlockCluster` itself, and `GameConstants` (specifically member `MAX_DIMENSION_OF_BLOCK_CLUSTER`). Introduction of new `BlockCluster` requires close monitoring of appropriate changes made. <br/>
> 
> Introducing BlockClusters of different dimensions would be a different kettle of fish since you would have to update the member variable `blocks` inside `BlockCluster` as well as `MAX_DIMENSION_OF_BLOCK_CLUSTER`.

The `Updater` class is a liaison between the graphics and the underlying sub structures.

### The Cycle

The cycle will revolve (primarily) around three classes `classes.gameSpace.Container`, `classes.ui.gui.GameBoard`, and `classes.ui.managers.Updater`
with references to the other classes as needed. 

1. `Container:` responsible for the underlying "computation" that shall keeps the game running; analogous to the brain.
2. `GameBoard:` responsible for updating the display. Is also responsible for appropriate observer signalling (Via `Events`) by invocations to `Updater  Is the graphics component; analogous to the corporeal body
3. `Updater:`   acts as the mediator between the upper two classes. Appropriate `switch` statements nested inside appropriately named `update...` methods are called by the `GameBoard` (since User Generated Input comes therefrom); analogous to the nervous system.

the cycle can be depicted via (where the arrows depict dependencies): <br/> <br/>
![GameCycle](./TetrisGameCycle.svg)

> 1. Note two key minutiae here. Arguably the `Updater` class is entirely redundant, since the `GameBoard` can merely invoke the relevant methods. I chose the idea of 
> a mediator is, in my smattering, of experience immensely important: had this been a more involved error consider vast arrays of user inputs in concert (as in a PS4 Controller).
> Do we really want the logic behind complex user input nested inside the graphics component or would we rather have a module that appropriately processes the data. 
> 2. There are times when the lines between what should be in each of the three classes is blurred e.g. arguably `checkForTermination` inside `GameBoard` should be `Updater's' responsibility
> (the simple workaround would be to have the aforementioned method invoke a method inside `Updater` that returns `boolean`; this sounds circuitous but I do believe in following best practices 
> I should have done just that since I would be working with others).
> 3. I have tried to buffer the `GameBoard` and `Container` as much as possible however it goes without saying that the 
> visual depiction of a construct would inevitably recourse to it (Consider methods `amalgamateGraphics`...)

Now for a quasi-superficial exposition on the actual cycle (I attempted to be thorough with the actual documentation so please do reference it if I was a bit unclear): <br/>
After `main` is executed and an instance of `GameBoard` is created, the `welcomeFrame` is created. After the user clicks on the relevant button (or space) the `mainFrame` is initialised; both of these actions primarily involve population of the Frames (the `Event Dispatch Thread` figures here). Thereafter, the GameCycle itself is initialised which entails appropriate interfacing with `Updater` and `KeyEvents` (and initialising the timer) <br/>
The code for the game cycle can be found in the `initialiseGameCycle` method inside `GameBoard`.

What transpires now is a constant "observer cycle" for `KeyEvents` and "timer signaling". 
After intervals (`GraphicsConstants.DELAY`) a regular method is invoked of `Update`, `UpdateDefault`.
After `KeyEvents` a more general method of `Update`, `UpdateGeneral` is invoked which (re)parameterises the KeyEvent as an enumeration.
(What these invocations do will be discussed below).
After successful updates (of either timer or key sort) the `grid` on display in the `mainFrame` is updated (and so is the Score display).
Finally, each time a small `checkForTermination` is invoked which disposes the `mainFrame` and creates the `terminationFrame`.

Now to hark back to the `Update` invocations. 
There are three main appertaining updating methods inside `Update`:
1. `updateCluster()`: is responsible for ensuring the presence of a "floating" object. In the entire application at (almost all times) there is a SINGLE "floating" block cluster (exceptions include the beginning of the game and successful invocation of `amalgamate` in `Container`). It is called in the other two update methods owing to the fact that they prompt the `Container` to update its internal state (their placements reflect `amalgamation`, thoroughness, and a dependency with `GameBoard`). Entails a dependency with the `Generator` class.
2. `updateDefault()`: invokes `updateCluster()` both at the beginning and the end of the method. Is responsible for CHECKING and thereafter moving the "floating" `blockCluster` down. Finally, prompts the Container to update its state, `checkAndUpdateGrid`.
3. `updateGeneral()`: same as above but performs checks taking into account the added user-specified action.

`Container's` `checkAndUpdateGrid` is essentially the application's main "Tetris" future. The concept of amalgamation herein refers to a `blockCluster` losing its 
identity as one. First the method checks if amalgamation must be done (the criteria are very instinctive: reaches the bottom or a previously occupied block).
Thereafter, amalgamation is to be done, `amalgamation()` is invoked alongside `horizontalClear()`, which empties a row if it is full.


---
### Extensibility

Points on extensibility of the application. 

#### \> COLOR!
Consider the class `./ui/gui/GraphicCell`. You will find the updating and setting of its Color is done manually (since I defaulted to using black and white). If you use a Random Number Generator (and either utilise the already built-in constant `BLOCK_COLOR` in `./ui/gui/GraphicsConstants` or) introduce a new member variable you can have colorful TETRIS! 

#### \> New Forms (still 3 by 3). 
A bit convoluted of an insertion since this entails high coupling. Appropriate updating to the enumeration, `./classes/gameObjects/compoundConstructs/Forms`, class, `./classes/gameObjects/compoundConstructs/BlockCluster`, and `./ui/managers/Generator` classes should allow you to insert new Shapes (still of a three-by-three dimensional nature since the underlying data structure for a `BlockCluster` continues to be a nine-element Array).

#### \> New Events
Not quite as convoluted as above, but appropriate refactoring to the Enumeration, `./ui/managers/PossibleUserInput` and the class `./ui/gui/GameBoard` (specifically the event listener code), and `./ui/managers/Updater` (the update General) can allow you to listen for more keys (and maybe even use the navigational up-down-left-right buttons, like a normal (joke) person would).

#### \> New Forms (varying dimensions). 
Most convoluted extension and instead of just giving you the solution I pose (or perhaps regurgitate it) a problem (and then give you the solution ofc): upon initially making a class should you ALWAYS intend for inheritance? If yes, should you plan for it bottoms-up or bottoms-down (would the re-tinkering, as I have dubbed it, be focused on introducing a super-class or a sub-class)?  What could be done here quite a bit of re-tinkering: all of `BlockCluster's` code could be excised into a new super-class (the Initial `BlockCluster` can/could/should remain as is
since it is fully functional). With a bit of repurposing of the member variable `blocks` to be an array of indeterminate size and the inclusion of overrides for rotations (in the now subclass `BlockCluster` which I will refer to henceforth as `BlockCluster9`), you should now be able to have `BlockClusters` of all types conceivable (but allowable). Remember to update `Generator` and `Forms`!

---

### Footnotes

Here you find further information on topics such as extensibility, optimisation etc. 

#### Justification for the Singleton Pattern for classes `Updater`, `Generator`, and `GameBoard`.
The justification is succinct. All information pertinent to their construction was known statically (that's not to say singleton cannot be done with dynamic information it's more convoluted) and I really wished to avoid the problems associate with debugging (accidentally introducing a new `Container` etc.).

#### Why I did not use factory pattern,
One may look at my implementation of `BlockCluster` and think `FactoryPattern`. I like to keep things as simple as possible but not simpler:
my decision to a nine-element array for ALL clusters and availing myself of the fact that a `Block` may be null merely rendered the entire process obsolete.
(Though as an afterthought it would've looked immensely gratifying but then again I didn't need different `BlockClusters` subtypes but then again...).

#### Optimisation 
There may be several points of optimisation in this rendition of Tetris. But one I wish to confute is that is the application of linked lists instead of nested arrays in `Container` 
The method `horizontalClear()` would've been much simpler, insofar as removal is concerned but it wouldn't matter since it outweighs the cost of instantiating new `CellSpatialUnit` and linking them to `GraphicCell`. 

#### Why Erudition (or well at least a smattering of apropos know-how) pays off
Disclaimer: no pretensions to erudition. 
I have knowledge as to threads and `Swing's` `Event Dispatch Thread` (and its unsafeness.) Initially I couldn't figure out why I couldn't display the `Score` in the `scoreDisplayArea` in `GameBoard`. Took me a while but the answer lies in the thread-unsafeness mentioned before; I use a global variable `SCORE` (inside) `GameConstants` for the `scoreDisplayArea`; in essence, (this is my hypothesis) the Swing thread and Main thread are nascent(ed) such that even the initialisation is never "recognised by the thread". A simple `volatile` for `Score` did the trick :). 

---
