package application;

import java.util.List;

import application.Round.Victor;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	public static final Image HOLE_CARD_IMAGE = new Image("file:resources/gray_back.png");
	private FlowPane[] handPanes = new FlowPane[2];
	private Label[] handLabels = new Label[2];
	private Label statsLabel = new Label();
	private Button btnHit = new Button();
	private Button btnStand = new Button();
	private Button btnDeal = new Button();
	private Round round = new Round();
	private int winnings = 0;
	private Spinner<Integer> betSpinner = new Spinner<Integer>(5, 100, 5, 5);
	private Label winningsLabel = new Label();
	
//	Stats Count
	int rounds = 0;
	int wins = 0;
	int losses = 0;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			round.deal();
			
			primaryStage.setTitle("BlackJack");
			BorderPane root = new BorderPane();
			addActionButtons(root);
			addGameDisplay(root);
			primaryStage.setScene(new Scene(root, 600, 500));
			primaryStage.show();
			displayInitialCards();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void displayInitialCards() {
		boolean holeCardAdded = false;
		for(Card card : round.getPlayerCards())	{
			dispalyCard(card.getImage(), true);
		}
		
		for(Card card : round.getDealerCards())	{
			if(!holeCardAdded)	{
				dispalyCard(HOLE_CARD_IMAGE,false);
				holeCardAdded = true;
			}
			else	{
				dispalyCard(card.getImage(), false);
			}
		}
	}
	
	private void displayHoleCard()	{
		if(handPanes[1].getChildren().get(0) instanceof ImageView)	{
			ImageView holeCardImgView = (ImageView)handPanes[1].getChildren().get(0);
			holeCardImgView.setImage(round.getDealerCards().get(0).getImage());
		}
		else	{
			throw new IllegalArgumentException("Something other than an ImageView node was found in the card display!");
		}
	}

	private void addGameDisplay(BorderPane root) {
		HBox gameDisplay = new HBox(2);
		BorderPane playerPane = new BorderPane();
		addSide(playerPane, true);
		BorderPane dealerPane = new BorderPane();
		addSide(dealerPane, false);
		HBox.setHgrow(playerPane, Priority.ALWAYS);
		HBox.setHgrow(dealerPane, Priority.ALWAYS);
		playerPane.setMaxWidth(Double.MAX_VALUE);
		dealerPane.setMaxWidth(Double.MAX_VALUE);
		gameDisplay.getChildren().addAll(playerPane, dealerPane);
		root.setCenter(gameDisplay);
	}

	private void addSide(BorderPane sidePane, boolean player) {
		handLabels[player ? 0 : 1] = new Label(player ?
				"Player Score: " + round.getPlayerScore() :
				"Dealer Score:");
		sidePane.setTop(handLabels[player ? 0 : 1]);
		if (player) {
			FlowPane betPane = new FlowPane();
			Label betLabel = new Label("Bet: ");
			betPane.getChildren().add(betLabel);
			betPane.getChildren().add(betSpinner);
			winningsLabel.setText("\tWinnings: " + winnings);
			betPane.getChildren().add(winningsLabel);
			sidePane.setBottom(betPane);
		}
		else	{
			statsLabel.setMinHeight(25);
			statsLabel.setText("Games Played: " + "\tWins: " + "\tLosses: ");
			sidePane.setBottom(statsLabel);
		}
		FlowPane handPane = new FlowPane();
		Background greenFelt = new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY));
		handPane.setBackground(greenFelt );
		handPanes[player ? 0 : 1] = handPane;
		sidePane.setCenter(handPane);
	}

	private void addActionButtons(BorderPane root) {
		btnHit.setText("Hit!");
		btnHit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dispalyCard(round.hit().getImage(), true);
				handLabels[0].setText("Player Score: " + round.getPlayerScore());
				postRoundProcessing();
			}
		});

		btnStand.setText("Stand");
		btnStand.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				displayHoleCard();
				List<Image> newCardImages = round.doDealerPlay();
				for(Image newCardImage : newCardImages)	{
					dispalyCard(newCardImage, false);
				}
				handLabels[1].setText("Dealer Score: " + round.getDealerScore());
				postRoundProcessing();
				btnStand.setDisable(true);
				btnHit.setDisable(true);
				btnDeal.setDisable(false);
			}
		});
		
		btnDeal.setText("Deal");
		btnDeal.setDisable(true);
		btnDeal.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				for(FlowPane handPanes : handPanes)	{
					handPanes.getChildren().clear();
				}
				round = new Round();
				round.deal();
				displayInitialCards();
				handLabels[0].setText("Player Score: " + round.getPlayerScore());
				handLabels[1].setText("Dealer Score: ");
				btnStand.setDisable(false);
				btnHit.setDisable(false);
				btnDeal.setDisable(true);
			}
		});

		HBox buttonRow = new HBox(3);
		root.setBottom(buttonRow);
		HBox.setHgrow(btnHit, Priority.ALWAYS);
		HBox.setHgrow(btnStand, Priority.ALWAYS);
		HBox.setHgrow(btnDeal, Priority.ALWAYS);
		btnHit.setMaxWidth(Double.MAX_VALUE);
		btnStand.setMaxWidth(Double.MAX_VALUE); 
		btnDeal.setMaxWidth(Double.MAX_VALUE); 
		buttonRow.getChildren().addAll(btnHit, btnStand, btnDeal);
	}

	private void dispalyCard(Image cardImage, boolean addingForPlayer) {
		ImageView imageView = new ImageView(cardImage);
		imageView.setFitHeight(100);
		imageView.setFitWidth(60);
		imageView.setPreserveRatio(true);
		handPanes[addingForPlayer ? 0 : 1].getChildren().add(imageView);
	}
	
	public void postRoundProcessing()	{
		Victor victor = round.checkVictor();
		if(!victor.equals(Victor.NONE))	{
			displayHoleCard();
			rounds++;
			handLabels[0].setText(handLabels[0].getText() + "\tGame Winner: " + victor);
			handLabels[1].setText("Dealer Score: " + round.getDealerScore() + "\tGame Winner: " + victor);
			if(victor.equals(Victor.PLAYER))	{
				wins++;
				winnings = winnings + betSpinner.getValue();
			}
			else	{
				losses++;
				winnings = winnings - betSpinner.getValue();
			}
			winningsLabel.setText("\tWinnings: " + winnings);

			statsLabel.setText("Games Played: " + rounds + "\tWins: " + wins + "\tLosses: " + losses);
			
			btnStand.setDisable(true);
			btnHit.setDisable(true);
			btnDeal.setDisable(false);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
