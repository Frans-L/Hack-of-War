# Hack of War

## Description
A tower-rush mobile game made with Scala and libGDX. Inspired by Clash of Royale.

The player tries to destroy the base of the enemy while keeping own base secured. The player can summon units by dragging cards into to the field. The units move and fight automatically, so the player is only able to control when and which units should be summoned on the field. 

Screenshot:

<p align="center">
  <img src="https://github.com/Frans-L/hack-of-war/blob/master/concept/screenshot.png?raw=true" alt="Concept image"/>
</p>


## State

The project is still work in progress. 

The project hasn't been updated since June 2018. The plan is to finish the game on next holiday.
The game engine and the game mechanics are ready. The content of the game is still in progress. For instance, there should be more units, different levels, missions etc. In addition, graphics should be improved.

## Structure

The idea of this project has been to create a game from scratch totally. So, premade object, collision and physics engines are not used. The exception is the LibGDX's UI engine which is used to draw UI components and their animations.

A simplified version of the structure of the project: 

<p align="center">
  <img src="https://github.com/Frans-L/hack-of-war/blob/master/concept/structure.png?raw=true" alt="/concept/structure.png"/>
</p>


The source code can be found under the folder:  _./core/src/game/_


## Deployment

Requirements:
* JDK 7
* Scala 2.11.x
* Gradle 2.3.x
* Android Studio / IntelliJ Ultimate
* Xcode for iOS (not tested)


## Libraries

* **libGDX** - [libGDX](https://github.com/libgdx/libgdx)


## Authors

* **Frans L** - [Frans-L](https://github.com/Frans-L)


