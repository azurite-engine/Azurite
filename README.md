<p align="center">
  <h1 align="center">Azurite</h1>

  <p align="center">
    Azurite is a 2D Java game engine built by the Games with Gabe <a href="https://discord.gg/dhyV3BXkRZ">Discord</a>/<a href="https://www.youtube.com/channel/UCQP4qSCj1eHMHisDDR4iPzw">YouTube</a> community.
    <br />
    <a href="https://azurite-engine.github.io/Azurite-Docs/"></a>
    <a href="https://www.youtube.com/watch?v=FABUP0q9tHY"></a>
    <br />
    <a href="https://azurite-engine.github.io/Azurite-Docs/">Explore the docs</a>
    ··
    <a href="https://www.youtube.com/watch?v=FABUP0q9tHY">Watch the introduction video</a>
    ··
    <a href="https://github.com/azurite-engine/Azurite/projects/2">Check the Project Board</a>
  </p>
</p>

<img src="https://azurite-engine.github.io/images/platformerSS.png" />

<p>
    <img src="https://img.shields.io/badge/Made%20using-Java-red">
    <img src="https://img.shields.io/badge/Made%20Using-LWJGL%20-yellow">
    <img src="https://camo.githubusercontent.com/0fa78702c674a5e13004de53a25ae80ed1ce281f92c0e5d6bd5aa7701b3ab483/68747470733a2f2f696d672e736869656c64732e696f2f6769746875622f6c6963656e73652f61746861756e2f454f532e737667">
    <img src="https://github.com/azurite-engine/Azurite/actions/workflows/gradle.yml/badge.svg">
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
* [Issues](https://github.com/Games-With-Gabe-Community/Azurite/issues)
* [License](#license)
* [Contact](#contact)

# Features

* Modern OpenGL through the LWJGL 3 library for fast GPU rendering.
* Entity Component System

| Name | Support | Render Context |
|------|:------:|:--------------:|
| Windows 		| Working 	 | OpenGL 330 |
| macOS 		| Working 	 | OpenGL 3.3 Core |
| Debian / Ubuntu 	| Working	 | OpenGL 330 |
| Arch / Manjaro 	| Working 	 | OpenGL 330 |
| Other Distros 	| Planned 	 | OpenGL 330 |
| FreeBSD		| Planned	 | - |
| Android	 	| Planned 	 | OpenGL ES |
| IOS		 	| Not Planned 	 | - |


### Built With
* [LWJGL 3](https://www.lwjgl.org/)

### Code Samples
#### BoilerPlate Code:
```java
public class Main extends Scene {
	public static void main(String[] args) {
		Engine.init(1920, 1080, "Azurite Engine Demo In Comment", 1.0f);
		Engine.scenes().switchScene(new Main());
		Engine.showWindow();
	}

	public void awake() {
		Graphics.setDefaultBackground(Color.BLACK);
		camera = new Camera();
		...
	}

	public void update() {
		...
	}
}
```

#### Simple example with sprites:
```java
public class Main extends Scene {
	GameObject player;
	Sprite s;

	public static void main(String[] args) {
		Engine.init(1920, 1080, "Azurite Engine Demo In Comment", 1.0f);
		Engine.scenes().switchScene(new Main());
		Engine.showWindow();
	}

	public void awake() {
		Graphics.setDefaultBackground(Color.BLACK);
		camera = new Camera();

		player = new GameObject();
		s = new Sprite("src/assets/sprite.png");
		player.addComponent(new SpriteRenderer(s, new Vector2f(100)));
	}

	public void update() {
		if (Keyboard.getKeyDown(GLFW.GLFW_KEY_SPACE))
			player.transform.add(new Vector2f(1, 0));
	}
}
```
# Getting Started

### Prerequisites
* OpenGL capable graphics card (minimum `core 330`)
* OpenGL capable graphics driver
* Java 11
  
### Project Setup
To begin contributing, create a fork of this repository.
Using intellj, import this project from existing sources as a gradle project.
Build gradle, then run the Main scene (located in the scenes package).
Follow this [link](https://azurite-engine.github.io/Azurite-Docs/tutorials/set-a-project.html) for tutorials on cloning and importing to either Intellj or Eclipse.

### Documentation
* [Documentatation](https://azurite-engine.github.io/Azurite-Docs/) (Heavy WIP)
* [Javadocs](https://azurite-engine.github.io/azurite-javadocs/)
* [Contributing guidelines](https://azurite-engine.github.io/Azurite-Docs/docs/contributing.html)
* [Azurite Code style](https://azurite-engine.github.io/Azurite-Docs/docs/azurite-style.html)

### License
Copyright (c) 2021 MIT License

### Contact
[Discord Server](https://discord.gg/dhyV3BXkRZ) in the #azurite-development channel
