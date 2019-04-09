package application;

import javafx.scene.image.Image;

public class Card {
	public enum Suit {
		C,
		H,
		D,
		S
	}

	int faceValue;
	Suit suit;
	public Card(int cardScore, Suit cardSuit)	{
		faceValue = cardScore;
		suit = cardSuit;
	}
	
	public int getScore()	{
		if(faceValue > 10)	{
			return 10;
		}
		return faceValue;
	}
	
	public Suit getSuit()	{
		return suit;
	}
	
	public Image getImage()	{
		return new Image("file:resources/"+faceValue+suit+".png");
	}
}
