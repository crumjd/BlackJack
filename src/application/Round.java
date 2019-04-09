package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import application.Card.Suit;
import javafx.scene.image.Image;

public class Round {
	public enum Victor {
		PLAYER,
		DEALER,
		PUSH,
		NONE
	}

	public static final int TOTAL_DECKS = 5;
	public static final int MAX_SCORE_IN_SUIT = 13;
	public static final int PLAYER_CHIPS = 100;
	private Stack<Card> undelt = new Stack<Card>();
	Hand playerHand = new Hand();
	Hand dealerHand = new Hand();
	boolean dealingComplete = false;
	
	public Round()	{
		List<Card> undeltTemp = new ArrayList<Card>();
		for(int i = 0; i < TOTAL_DECKS; i++)	{
			for(Suit suit : Suit.values())	{
				for(int score = 1; score <= MAX_SCORE_IN_SUIT; score++)	{
					undeltTemp.add(new Card(score, suit));
				}
			}
		}
		Collections.shuffle(undeltTemp);
		undelt.addAll(undeltTemp);
	}
	
	public void deal()	{
		for(int i = 0; i < 2; i++)	{
			playerHand.dealCard(undelt.pop());
			dealerHand.dealCard(undelt.pop());
		}
	}
	
	public Card hit()	{
		Card newCard = undelt.pop();
		playerHand.dealCard(newCard);
		return newCard;
	}

	public List<Card> getPlayerCards()	{
		return playerHand.getCards();
	}
	
	public List<Card> getDealerCards()	{
		return dealerHand.getCards();
	}

	public Card getLastPlayerCard() {
		return playerHand.getLastCard();		
	}

	public int getPlayerScore() {
		return playerHand.getHandScore();
	}
	
	public int getDealerScore() {
		return dealerHand.getHandScore();
	}
	 
	public List<Image> doDealerPlay()	{
		List<Image> newCardImages = new ArrayList<Image>();
		while(dealerHand.getHandScore() < 17 ||
				(dealerHand.getHandScore() == 17 && dealerHand.isSoft()))	{
			Card newCard = undelt.pop();
			dealerHand.dealCard(newCard);
			newCardImages.add(newCard.getImage());
		}
		dealingComplete = true;
		return newCardImages;
	}
	
	public Victor checkVictor()	{
		int playerScore = playerHand.getHandScore();
		int dealerScore = dealerHand.getHandScore();
		if(playerScore == 21 && dealerScore == 21)	{
			return Victor.PUSH;
		}
		if(playerScore == 21 || dealerScore > 21)	{
			return Victor.PLAYER;
		}
		if(dealerScore == 21 || playerScore > 21)	{
			return Victor.DEALER;
		}
		if(dealingComplete)	{
			if(playerScore == dealerScore)	{
				return Victor.PUSH;
			}
			else if(playerScore > dealerScore)	{
				return Victor.PLAYER;
			}
			else	{
				return Victor.DEALER;
			}
		}
		return Victor.NONE;
	}
}
