boolean moveLeft;
boolean moveRight;
boolean moveUp;
boolean moveDown;
boolean moveLeftP2;
boolean moveRightP2;
boolean moveUpP2;
boolean moveDownP2;
boolean space;
boolean spaceP2;
boolean pressedEscape;
boolean releasedEscape;
boolean pressedM;
boolean releasedM;

void keyPressed()
{

	//println(keyCode);

	//keyCodes

	if(key == CODED)
	{
		if(keyCode == RIGHT)
		{
			moveRightP2 = true;
		}
		else if(keyCode == LEFT)
		{
			moveLeftP2 = true;
		}
		if(keyCode == UP)
		{
			moveUpP2 = true;
		}
		else if(keyCode == DOWN)
		{
			moveDownP2 = true;
		}
	}

	//letters

	if(key == 'd' || key == 'D')
	{
		moveRight = true;
	}
	else if(key == 'a' || key == 'A')
	{
		moveLeft = true;
	}
	if(key == 'w' || key == 'W')
	{
		moveUp = true;
	}
	if(key == 's' || key == 'S')
	{
		moveDown = true;
	}

	if(key == ' ')
	{
		space = true;
	}
	if(key == '<')
	{
		spaceP2 = true;
	}

	if(key == 'p' || key == 'P')
	{
		pressedEscape = true;
	}
	if(key == 'm' || key == 'M')
	{
		pressedM = true;
	}
}

void keyReleased()
{

	//keyCodes

	if(key == CODED)
	{
		if(keyCode == RIGHT)
		{
			moveRightP2 = false;
		}
		else if(keyCode == LEFT)
		{
			moveLeftP2 = false;
		}
		if(keyCode == UP)
		{
			moveUpP2 = false;
		}
		else if(keyCode == DOWN)
		{
			moveDownP2 = false;
		}
	}

	//letters

	if(key == 'd' || key == 'D')
	{
		moveRight = false;
	}
	else if(key == 'a' || key == 'A')
	{
		moveLeft = false;
	}
	if(key == 'w' || key == 'W')
	{
		moveUp = false;
	}
	if(key == 's' || key == 'S')
	{
		moveDown = false;
	}

	if(key == ' ')
	{
		space = false;
	}
	if(key == '<')
	{
		spaceP2 = false;
	}

	if(key == 'p' || key == 'P')
	{
		releasedEscape = true;
	}
	if(key == 'm' || key == 'M')
	{
		releasedM = true;
	}
}

float getAxisRaw(String axis)
{
	if(axis == "Horizontal")
	{
		if(moveLeft)
		{
			return -1;
		}
		if(moveRight)
		{
			return 1;
		}
	}

	if(axis == "Vertical")
	{
		if(moveUp)
		{
			return -1;
		}
		if(moveDown)
		{
			return 1;
		}
	}

	return 0;
}

void mouseReleased(){
  //print(buttonA.Clicked(mouseX, mouseY));

	if(gameState == GameState.MainMenu){
		if(playButton.Clicked(mouseX, mouseY)){
	    gameState = GameState.Playing;
			multiplaying = false;
	  }
		else if(multiplayButton.Clicked(mouseX, mouseY)){
			multiplaying = true;
	    gameState = GameState.Playing;
	  }
	  else if(highscoreButton.Clicked(mouseX, mouseY)){
	    gameState = GameState.Highscore;
	  }
		else if(exitButton.Clicked(mouseX, mouseY)){
	    exit();
	  }
	}
	else if(gameState == GameState.Highscore){
		if(mainMenuButton.Clicked(mouseX, mouseY)){
	    gameState = GameState.MainMenu;
	  }
	}
}
