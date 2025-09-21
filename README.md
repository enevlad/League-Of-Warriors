# README

**Author: Ene Vlad-Mihnea**
**Group: 321CC**

--

## Project Overview

The overall difficulty of the project was medium. The main difficulty in implementing this project was at first understading how JAVA Swing works, mainly how ot switch UI pages depending on the game state. It took approximately 4 days (3-4 hours/day).

## Implementation Challanges

- **Battle and Ability Selection Pages**
Designing the pages required for the functioning of the game, such as main page and fighting and ability selection page needed attention so that they could work properly between eachother. One of the thoughest parts was espacially making sure that selecting an ability from the selection panel would work properly in updating the information on the battle page and not break the loop.

- **Map Update/Reset and Level Transition**
Making the map panel render on every level and update properly depeding on where the character was moving and what it was encountering was espacially difficult.

## GUI Components

- **GameUI**
Handles most of the GUI of the game. This is an UI page which renders diffrent panels such as login, map and character selection.

- **BattlePage**
This window displays the battle between the player and the enemy which are represented in the form of two cards (images). There are two types of attack normal and spell. When attacking with a spell an ability selection panel shows up. The player and enemy info (health and mana) are always upadated after an atack.

- **FinalPage**
Displays the final page which contains the status of the character at the end of the game or level. This page shows if the player dies or when he finishes a level. If the later option happens then, he is prompeted two button one for the next level and one to exit the game. If he died instead of the next level button he will have the play again button to restart.

## Game Flow

- **Loop**
The game is continous which means it does not end on death the player can choose to play again. It only closes when the player explicitly chooses to exit the game.
