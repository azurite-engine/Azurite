<p>
    <br />
    <img src="https://img.shields.io/badge/Made%20using-Java-red">
    <img src="https://img.shields.io/badge/Made%20Using-Open%20GL%20-yellow">
</p>

<br />
<p align="center">
  <h2 align="center">Azurite</h2>

  <p align="center">
    Azurite it is a 2D Java game engine built by the Games with Gabe <a href="https://discord.gg/dhyV3BXkRZ">Discord</a>/<a href="https://www.youtube.com/channel/UCQP4qSCj1eHMHisDDR4iPzw">YouTube</a> community.
  <br><em>Documentation is incomplete at the moment, we are working on it.</em>
    <br />
    <a href="https://games-with-gabe-community.github.io/azurite-docs/"><strong>Explore the docs »</strong></a>
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
* Dear ImGui bindings

## Code Samples
#### BoilerPlate Code:
```java
public class Main extends Scene {
	
	public void awake() {		
		camera = new Camera();
	}

	public void update() {
		background(255, 255, 255); // Sets the clear color
	}
}
```

#### Simple example with sprites
```java
public class Main extends Scene {
	
	GameObject greenRectangle = new GameObject(new Transform(600, 230, 50, 50), 1);
	GameObject mario = new GameObject(new Transform(600, 200, 50, 50), 2);
	
	public void awake() {		
		camera = new Camera();
		
		greenRectangle.addComponent(new SpriteRenderer(new Color(0, 255, 0, 255))); // Creates a new green sprite component
		mario.addComponent(new SpriteRenderer(new Sprite(Assets.getTexture("src/assets/images/marioSprite.png"))));	// Loads the image from the filesystem into a sprite component
	}

	public void update() {
		background(50, 50, 50); // Sets the clear color
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
tba

## Documentation
* [Documentatation](#) (Work in progress)
* [Javadocs](#) (Coming soon)

### License
Copyright (c) 2021 MIT License

### Built With
* [LWJGL 3](https://www.lwjgl.org/)

### Contact


### Credits

