package application;

import java.util.ArrayList;
import java.util.List;

public class Hand {
	
	List<Card> cards = new ArrayList<Card>();
	
	public void dealCard(Card card)	{
		cards.add(card);
	}
	
	public int getHandScore()	{
		int handScore = 0;
		int aceCount = 0;
		for(Card card : cards)	{
			if(card.faceValue == 1)	{
				aceCount++;
			}
			else	{
				handScore = handScore + card.getScore();
			}
		}
		for(int i = 0; i< aceCount; i++)	{
			if(handScore + 11 <= 21)	{
				handScore = handScore + 11;
			}
			else	{
				handScore = handScore + 1;
			}
		}
		return handScore;
	}
	
	public List<Card> getCards()	{
		return cards;
	}

	public Card getLastCard() {
		return cards.get(cards.size() - 1);
	}
	
	public boolean isSoft()	{
		for(Card card : cards)	{
			if(card.faceValue == 1)	{
				return true;
			}
		}
		return false;
	}
}
