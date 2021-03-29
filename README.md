<p>
    <br />
    <img src="https://img.shields.io/badge/Made%20using-Java-red">
    <img src="https://img.shields.io/badge/Made%20Using-LWJGL%20-yellow">
    <img src="https://camo.githubusercontent.com/0fa78702c674a5e13004de53a25ae80ed1ce281f92c0e5d6bd5aa7701b3ab483/68747470733a2f2f696d672e736869656c64732e696f2f6769746875622f6c6963656e73652f61746861756e2f454f532e737667">
    <img src="https://github.com/Games-With-Gabe-Community/Azurite/actions/workflows/gradle.yml/badge.svg">
</p>

<br />
<p align="center">
  <h2 align="center">Azurite</h2>

  <p align="center">
    Azurite it is a 2D Java game engine built by the Games with Gabe <a href="https://discord.gg/dhyV3BXkRZ">Discord</a>/<a href="https://www.youtube.com/channel/UCQP4qSCj1eHMHisDDR4iPzw">YouTube</a> community.
    <br />
    <a href="https://games-with-gabe-community.github.io/Azurite-Docs/"><strong>Explore the docs »</strong></a><br><br>
    <a href="https://www.youtube.com/watch?v=FABUP0q9tHY">Watch the introduction video »</a>
    <br />
    <br />
    <a href="https://github.com/othneildrew/Best-README-Template">Features</a>
    ·
    <a href="https://github.com/Games-With-Gabe-Community/Azurite/issues">Report Bug</a>
    ·
    <a href="https://trello.com/b/hfoYA8Gn/gwg-community-project">Check the Trello</a>
  </p>
</p>


<!-- TABLE OF CONTENTS -->
## Table of Contents

* [Features](#features)
  * [Built With](#built-with)
  * [Code samples](#code-samples)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Project Setup](#project-setup)
  * [Documentation](#documentation)
* [Issues](https://github.com/athaun/Gprocessing/issues)
* [License](#license)
* [Contact](#contact)

## Features

* Modern OpenGL through the LWJGL 3 library for fast GPU rendering.
* Entity Component System

## Code Samples
#### BoilerPlate Code:
```java
public class Main extends Scene {
	
	public void awake() {		
		setDefaultBackground(Color.BLACK);
		camera = new Camera();
	}

	public void update() {

	}
}
```

#### Simple example with sprites
```java
public class Main extends Scene {
	
	GameObject greenRectangle = new GameObject(new Transform(600, 230, 50, 50), 1);
	GameObject mario = new GameObject(new Transform(600, 200, 50, 50), 2);
	
	public void awake() {	
		setDefaultBackground(Color.BLACK);
		camera = new Camera();
		
		greenRectangle.addComponent(new SpriteRenderer(new Color(0, 255, 0, 255))); // Creates a new green sprite component
		mario.addComponent(new SpriteRenderer(new Sprite(Assets.getTexture("src/assets/images/marioSprite.png"))));	// Loads the image from the filesystem into a sprite component
	}

	public void update() {

	}
}
```

## Prerequisites
* OpenGL capable graphics card (minimum `core 330`)
* OpenGL capable graphics driver
  * Linux nouveau drivers for nvidia cards do not currently work, you will have to install proprietary drivers.
  * FOSS AMD Drivers for linux do work.
* Java 1.8
  
## Project Setup
To begin contributing, create a fork of this repository.
Using intellj, import this project from existing sources as a gradle project.
Build gradle, then run the Main scene (located in the scenes package).

## Documentation
* [Documentatation](https://games-with-gabe-community.github.io/Azurite-Docs/) (Done but we are still adding stuff to it)
* [Javadocs](https://games-with-gabe-community.github.io/azurite-javadocs/)
* [Contributing guidelines](https://github.com/Games-With-Gabe-Community/Azurite/blob/main/CONTRIBUTING.md)
* [Google Code style]( https://google.github.io/styleguide/javaguide.html) (Please adhere to this general style until we develop a simpler version)

### License
Copyright (c) 2021 MIT License

### Built With
* [LWJGL 3](https://www.lwjgl.org/)

### Contact
[Discord Server](https://discord.gg/dhyV3BXkRZ)

