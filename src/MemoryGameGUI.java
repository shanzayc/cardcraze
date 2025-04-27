/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * This programs lets the user play a game to test their memory by finding sets of cards that match
 *
 * @author shanzay
 * @date 16-June-2023
 */
//Swing timer code from https://kodejava.org/how-do-i-set-swing-timer-initial-delay/ 
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Scanner;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Container;

/**
 * GUI for memory game to match pairs of cards
 * @author shanzay
 */
public class MemoryGameGUI extends JFrame implements ActionListener {

    private JButton board, playAgainButton;
    private JLabel title, highScoreText;
    private JPanel topP, bottomPanel;
    private String[] contents;
    private JButton[] buttons;
    private Card[] cards;
    private Card flippedCard;
    private int currentCardIndex, score, playAgainCount, highScore, numMatches;
    private boolean canClick;
    private Container c;
    private Timer timer;

    private Font f = new Font("Helvetica Bold", Font.BOLD, 36);
    private Font f2 = new Font("Helvetica Bold", Font.BOLD, 15);

    /**
     * Constructor method for class
     */
    public MemoryGameGUI() {
        setLayout(new BorderLayout());
        // Create a JPanel for the button panel with GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(3, 4));
        add(buttonPanel, BorderLayout.CENTER);
        //Set default close operation of the JFrame to exit the application
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Create a JPanel with FlowLayout and center alignment and put title on it
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title = new JLabel("Mind Match");
        title.setFont(f);
        topPanel.add(title);
        // Add the topPanel to the content pane of the JFrame, positioned at the top
        getContentPane().add(topPanel, BorderLayout.NORTH);

        //array of symbols to put on cards
        contents = new String[]{"\u2666", "\u2605", "\u270F", "\u263A", "\u25A0",
            "\u2663", "\u2666", "\u270F", "\u263A", "\u2605", "\u25A0", "\u2663"};
        shuffleContents();

        //creats cards and button array
        buttons = new JButton[12];
        cards = new Card[12];
        score = 0;

        //creates 12 buttons and adds them to the button panel with action listener 
        for (int i = 0; i < 12; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Times New Roman", Font.BOLD, 60));
            buttons[i].addActionListener(this);
            buttonPanel.add(buttons[i]);
        }

        //makes a button to play again and adds it to the bottom panel on the bottom with action listener 
        playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(this);
        bottomPanel = new JPanel();
        bottomPanel.add(playAgainButton);
        add(bottomPanel, BorderLayout.SOUTH);
        setSize(1200, 600);

        //reads highscore from text file and adds it to the bottom panel
        highScore = readHighScore();
        highScoreText = new JLabel("HighScore: " + Integer.toString(highScore));
        highScoreText.setFont(f2);
        bottomPanel.add(highScoreText);

        canClick = false;
        //calls nessecary methods to start playing
        makeCards();
        showCard(2000);
        initializeGame();
        setVisible(true);
    }

    //makes cards with card objects
    private void makeCards() {
        for (int i = 0; i < 12; i++) {
            cards[i] = new Card(contents[i]);
        }
    }

    //starts game by making all the buttons and hiding symbols with a "?"
    private void initializeGame() {
        for (int i = 0; i < 12; i++) {
            buttons[i].setEnabled(true);
            buttons[i].setText("?");
        }
    }

    //shows cards at beginning of game to test memory 
    private void showCard(int duration) {
        currentCardIndex = 0;

        //creates a timer object with a certain time to handle time events
        // timer allows card to only be shown for a quick second
        timer = new Timer(duration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Disables every button so user can't cheat
                for (int i = 0; i < 12; i++) {
                    buttons[i].setEnabled(false);
                }

                // hides the previously shown card
                if (currentCardIndex > 0) {
                    buttons[currentCardIndex - 1].setText("?");
                    //buttons[currentIndex-1].setEnabled(false); //***!!!***
                }

                if (currentCardIndex < 12) {
                    // Show the current cards symbol and enable it
                    buttons[currentCardIndex].setText(contents[currentCardIndex]);
                    buttons[currentCardIndex].setEnabled(true);
                    currentCardIndex++; //increases for the next card up 
                } else {
                    // All the cards have been shown
                    // hide last card
                    timer.stop();
                    buttons[currentCardIndex - 1].setText("?"); //HERE
                    buttons[currentCardIndex - 1].setEnabled(false);

                    //reset cards for game to begin
                    resetCards();
                    canClick = true; //user can now click the buttons
                }
            }
        });

        // Set the delay and initial delay of the timer
        timer.setDelay(duration);
        timer.setInitialDelay(0);
        timer.start(); //starts timer
    }

    //resets all buttons to be enabled again after each card has been shown at the beginning of game
    private void resetCards() {
        for (int i = 0; i < 12; i++) {
            buttons[i].setEnabled(true);
        }
    }

    // resets game after every card has been matched 
    // cards instance variables are reset to default (false)
    private void resetGame() {
        for (int i = 0; i < 12; i++) {
            buttons[i].setText("?");
            cards[i].setIsFlipped(false);
            cards[i].setMatched(false);
            updateButtons(); //calls method to update buttons based on card variables
        }
    }

    // shuffles cards symbols so every game/round is unique 
    private void shuffleContents() {
        for (int i = contents.length - 1; i > 0; i--) {

            // line generates a random integer between 0 and i
            int j = (int) (Math.random() * (i + 1));

            // swaps elements 
            String temp = contents[i];
            contents[i] = contents[j];
            contents[j] = temp;
        }
    }

    /**
     * Performs the specified action when a GUI event is triggered.
     * @param e the event object which represents the GUI action
     */
    @Override
    // handles action events when they are triggered 
    public void actionPerformed(ActionEvent e) {
        if (!canClick) {
            return; // leaves this method since the user is currently not allowed 
            // to click another button so it ignores any button the user may be trying to click 
        }

        JButton clickedButton = (JButton) e.getSource(); //gets what has button has been clicked
        int clickedIndex = -1;
        for (int i = 0; i < 12; i++) {
            if (buttons[i] == clickedButton) {
                clickedIndex = i; // keeps track of what button has been clicked first
                break; // breaks when the buttons index has been found
            }
        }

        // if a button that has not been matched or flipped is clicked, its symbol will show
        if (clickedIndex != -1 && cards[clickedIndex].getMatched() == false && cards[clickedIndex].getIsFlipped() == false) {
            cards[clickedIndex].setIsFlipped(true); //updates cards flipping status 
            clickedButton.setText(cards[clickedIndex].getContent());

            if (flippedCard == null) { // if this is true, only one card has been clicked
                flippedCard = cards[clickedIndex]; // the card that has just been clicked becomes the flipped card  
            } // if 2 cards have been clicked, check if they are the same
            else {
                checkMatch();
            }
        }

        //checks if the play again button has been clicked 
        if (clickedButton.getText().equals("Play Again")) {

            int matches = 0; //to keep track of how many cards have been matched
            for (int i = 0; i < cards.length; i++) {
                if (cards[i].getMatched() == true) {
                    matches = matches + 1;
                }
            }

            //if all 12 cards have been matched, user can play a new game
            if (matches == 12) {
                //variable to keep track of how many times the user has clicked the play again button
                playAgainCount = playAgainCount + 1;

                //if the play again button is being clicked for the first time, the score has to be greater than the highscore which will be by default be 0 
                if (playAgainCount == 1) {
                    if (score > highScore) {
                        highScore = score;  // sets first time score to the highScore value
                        writeHighScore(highScore); //updates text file
                        highScoreText.setText("HighScore: " + Integer.toString(readHighScore())); //updates text on panel 
                    }
                } //if play again button has been clicked before, the score has to be less than the highscore 
                else if (playAgainCount > 1) {
                    if (score < highScore) {
                        highScore = score; //sets new high score value
                        writeHighScore(highScore);
                        highScoreText.setText("HighScore: " + Integer.toString(readHighScore())); //updates text on panel

                    }
                }

                //calls nessecary methods to start a new round and play another game 
                shuffleContents();
                resetGame();
                canClick = false;
                makeCards();
                showCard(2000);
                score = 0;
                initializeGame();
            }

        }

        //listens to window when it is closed ('x' button)
        //window listening code from https://www.tabnine.com/code/java/methods/java.awt.Window/addWindowListener 
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                writeHighScore(0); //resets high score to 0
                playAgainCount = 0; //resets count variable
                System.exit(0);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
        //end of used code 

        updateButtons(); //updates all other buttons accordingly to any conditions (matches or non-mathces) 
    }

    //reads highscore value from text file 
    private static int readHighScore() {
        try ( BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
        return 0; //default return value if there is an error 
    }

    //writes highscore value to text file 
    private static void writeHighScore(int score) {
        try ( FileWriter fw = new FileWriter(("highscore.txt"))) {
            fw.write(String.valueOf(score));
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }

    //updates buttons to their matching and flipping status 
    private void updateButtons() {
        for (int i = 0; i < buttons.length; i++) {
            Card card = cards[i];
            if (card.getMatched()) {
                buttons[i].setText(card.getContent());
            } else if (card.getIsFlipped()) {
                buttons[i].setText(card.getContent());
            } else {
                buttons[i].setText("?");
            }
        }
    }

    private void checkMatch() {
        //loops through every card (except flippedCard) that is flipped and not matched
        for (int i = 0; i < cards.length; i++) {
            int index = i;
            if (cards[i] != flippedCard && cards[i].getIsFlipped() && !cards[i].getMatched()) {
                if (cards[i].getContent().equals(flippedCard.getContent())) { //checks if they have same symbol 

                    //sets cards matched value to true 
                    cards[i].setMatched(true);
                    flippedCard.setMatched(true);

                    //disables the buttons so that they can't be clicked again
                    buttons[i].setEnabled(false);
                    buttons[getButtonIndex(flippedCard)].setEnabled(false);

                    flippedCard = null; //resets
                    numMatches += 2; //keeps track of the number of matches
                    score = score + 1;
                } //if matching pair is not found
                else {
                    canClick = false; //ensures the users clicks don't matter until the pair is flipped back around
                    //timer to delay a little betweeen once the user clicks the non-matching pair and flipping it back
                    Timer timer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            //clears text and enables both unmatched card
                            buttons[index].setText("?");
                            buttons[index].setEnabled(true);
                            buttons[index].setBackground(null);
                            buttons[getButtonIndex(flippedCard)].setText("?");
                            buttons[getButtonIndex(flippedCard)].setEnabled(true);

                            //rests flipped status to false 
                            flippedCard.setIsFlipped(false);
                            cards[index].setIsFlipped(false);
                            flippedCard = null; //resets
                            score = score + 1;
                            canClick = true; //player can click again to find other matches
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
                break;
            }
        }
    }

    //method used to get the index of the flippedCard
    private int getButtonIndex(Card card) {
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] == card) {
                return i;
            }
        }
        return -1;
    }

} //end of class
