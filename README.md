# Hack of War

## Description
A tower-rush mobile game made with ScalaX. Inspired by Clash of Royale.

The player tries to destroy the base of the enemy while keeping own base secured. The player can summon units by dragging cards into to the field. The units move and fight automatically, so players can focus on strategical decision. 

Gameplay:

<p align="center">
  <img src="https://github.com/Frans-L/hack-of-war/blob/master/concept/gameplay.gif?raw=true" alt="Gameplay gif"/>
</p>

## Features

The game has a own game engine. The engine has custom e.g collision and object engine and crowd pathfinding. Simply, the reason why the game doesn't use premade collision or object engines was just to learn more :)

Here is a clip from the crowd pathfinding:

<p align="center">
  <img src="https://github.com/Frans-L/hack-of-war/blob/master/concept/swarm.gif?raw=true" alt="Swarm gif"/>
</p>

The object engine is compltelty modular. So, even the AI of an unit is built modularly:

<p align="center">
  <img src="https://github.com/Frans-L/hack-of-war/blob/master/concept/modular.png?raw=true" alt=" Screenshot From Modularity"/>
</p>

## State

The project is still work in progress. 

The project hasn't been updated since June 2018. The game engine and the game mechanics are ready. The content of the game is still in progress. For instance, there should be more units, different levels, missions etc. In addition, graphics should be improved.

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


