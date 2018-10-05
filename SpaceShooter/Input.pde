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

boolean inputTextA = false;
boolean inputTextB = false;

void keyPressed()
{
	if(inputTextA){
		//print("Starting input process\n");
		if(java.lang.Character.isLetter(key)){
			//print("Character was a letter\n");
			if(nameA.length() < 11)
				nameA += key;
		}
		else{
			//print("Character was not a ltter\n");
			if((int)key == 8){
				if(nameA.length() > 0){
					String tmp = nameA.substring(0, nameA.length()-1);
					nameA = tmp;
				}
			}
			else if((int)key == 9){
				inputTextA = false;
				inputTextB = true;
			}
			//print((int)key + "\n");
		}
	}
	else if(inputTextB){
		if(java.lang.Character.isLetter(key)){
			if(nameB.length() < 11)
				nameB += key;
		}
		else{
			if((int)key == 8){
				if(nameB.length() > 0){
					String tmp = nameB.substring(0, nameB.length()-1);
					nameB = tmp;
				}
			}
			else if((int)key == 9){
				inputTextA = true;
				inputTextB = false;
			}
			//print((int)key + "\n");
		}
	}
	if((int)key == 10){
		saveHighscore();
		gameState = GameState.Highscore;
	}

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
		else if(keyCode == ENTER || keyCode == RETURN)
		{
			inputTextA = false;
			inputTextB = false;
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
			highscoreAnim = bottomBoundary;
	    gameState = GameState.Highscore;
	  }
		else if(exitButton.Clicked(mouseX, mouseY)){
	    exit();
	  }
	}
	else if(gameState == GameState.GameOver){
		if(mainMenuButton.Clicked(mouseX, mouseY)){
			ResetGame();
	    gameState = GameState.MainMenu;
	  }
		if(submitButton.Clicked(mouseX, mouseY)){
			saveHighscore();
			gameState = GameState.Highscore;
		}
		if(textAreaA.Clicked(mouseX, mouseY)){
			//print("Clicked text area A \n");
			inputTextA = true;
			inputTextB = false;
		}
		if(textAreaB.Clicked(mouseX, mouseY)){
			//print("Clicked text area B \n");
			inputTextA = false;
			inputTextB = true;
		}
	}
	else if(gameState == GameState.Highscore){
		if(mainMenuButton.Clicked(mouseX, mouseY)){
			ResetGame();
	    gameState = GameState.MainMenu;
	  }
		if(highscoreMultiplayButton.Clicked(mouseX, mouseY)){
			multiplaying = !multiplaying;
		}
	}
}
