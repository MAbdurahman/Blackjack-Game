
package game;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Cursor.HAND_CURSOR;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The GamePanel Class
 * 
 * @author:  MAbdurrahman
 * @date:  17 June 2017
 * @version:  1.0.0
 */
public class GamePanel extends JPanel {
    //Instance Variables for the GamePanel
    private final CardPanel cardPanel;
    private final JButton hitButton;
    private final JButton standButton;
    private final JButton newGameButton;
    private final JTextField wagerField;
    
    /**
     * GamePanel Constructor - Creates an instance of the GamePanel
     */
    public GamePanel() {
        setBackground(new Color(156, 95, 42));
        setLayout(new BorderLayout(3, 3));
        
        cardPanel = new CardPanel();
        add(cardPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(156, 95, 42));
        add(buttonPanel, BorderLayout.SOUTH);
        
        hitButton = new JButton("   Hit   ");
        hitButton.setBackground(Color.red);
        hitButton.setSize(60, 25);
        hitButton.setFont(new Font("Montserrat Alternates", Font.PLAIN, 18));
        hitButton.setToolTipText("Add another card");
        hitButton.addActionListener(cardPanel);
        buttonPanel.add(hitButton);
        
        standButton = new JButton("Stand");
        standButton.setBackground(Color.yellow);
        standButton.setSize(60, 25);
        standButton.setFont(new Font("Montserrat Alternates", Font.PLAIN, 18));
        standButton.setToolTipText("Add no more cards");
        standButton.addActionListener(cardPanel);
        buttonPanel.add(standButton);
        
        newGameButton = new JButton("New Game");
        newGameButton.setBackground(Color.green);
        newGameButton.setSize(60, 25);
        newGameButton.setFont(new Font("Montserrat Alternates", Font.PLAIN, 18));
        newGameButton.setToolTipText("Start a new game");
        newGameButton.addActionListener(cardPanel);
        buttonPanel.add(newGameButton);
        
        JLabel wagerLabel = new JLabel("    Wager: $");
        wagerLabel.setFont(new Font("Montserrat Alternates", Font.PLAIN, 17));
        wagerField = new JTextField("25", 3);
        wagerField.setFont(new Font("Montserrat Alternates", Font.PLAIN, 18));
        wagerField.setBorder(BorderFactory.createLineBorder(Color.decode("#9C5F2A"),1 , false));
        wagerField.setBackground(Color.decode("#9C5F2A"));
        wagerField.setToolTipText("Click to change wager");
        
        buttonPanel.add(wagerLabel);
        buttonPanel.add(wagerField);
        
        setBorder(BorderFactory.createStrokeBorder(new BasicStroke(13), Color.decode("#9C5F2A")));
        
    }//end of the GamePanel Constructor
    /**
     * CardPanel Class is a nested class, displays the cards, maintains the status of the
     * game, and responds to the actionEvents of the game.
     */
    private class CardPanel extends JPanel implements ActionListener {
        //Instance Variables
        Deck deck;//The deck of cards for the game
        BlackjackHand dealerHand;
        BlackjackHand playerHand;
        String message;
        Font gameFont;//The font used to display the messages of the game
        Image cardImages;
        boolean gameInProgress;//Monitors the status of the game
        String moneyString;
        int playerMoney;
        int wager;
        /**
         * CardPanel Constructor - Creates an instance of the CardPanel
         */
        private CardPanel() {
            loadImages();
            setBackground(new Color(65, 110, 12));
            setForeground(new Color(237, 237, 69));
            
            gameFont = new Font("Montserrat Alternates", Font.PLAIN, 28);
            message = "Click \"New Game\" to start a new game.";
            playerMoney = 1000;
            moneyString = "Your money $" + getPlayerMoney();
            new WelcomeDialog(null, true).setVisible(true);
            
        }//end of the CardPanel Constructor
        /**
         * loadImages Method - Loads the card images from the cardDeck file
         * @param Void
         */
        private void loadImages() {
            ClassLoader classLoader = this.getClass().getClassLoader();
            URL imageURL = classLoader.getResource("\\img\\cardDeck.png");
            
            if (imageURL != null) {
                cardImages = Toolkit.getDefaultToolkit().createImage(imageURL);
                
            } else {
                Toolkit.getDefaultToolkit().beep();
                String msg = "Card images not found!";
                JOptionPane.showConfirmDialog(this, msg);
            }
        }//end of the loadImages Method
        /**
         * setGameStatus Method - Sets the status of the game.  In addition to setting the game
         * status, it enables and disables the hit, stand, and new game buttons, and the textfield
         * to reflect the state of the game.
         * @param Boolean - the new value of the game status
         */
        private void setGameStatus(boolean status) {
            gameInProgress = status;
            if (gameInProgress) {
                hitButton.setEnabled(true);
                standButton.setEnabled(true);
                newGameButton.setEnabled(false);
                wagerField.setEditable(false);
                
            } else {
                hitButton.setEnabled(false);
                standButton.setEnabled(false);
                newGameButton.setEnabled(true);
                wagerField.setEditable(true);
                
            }
        }//end of the setGameStatus Method
        /**
         * checkWager Method - Determines whether or not the wager is valid.  It reads the wager
         * from the wagerField.  If an error occurs, the message in the panel is changed to inform
         * the player of the error.
         * @param Void
         * @return Boolean - Returns true, if the wager is read without error; otherwise, it returns
         * false.
         */
        private boolean checkWager() {
            int amount;
            try {
                amount = Integer.parseInt(wagerField.getText());
                
            } catch (NumberFormatException nfe) {
                message = "The wager must be a integer greater than zero.";
                Toolkit.getDefaultToolkit().beep();
                repaint();
                return false;
            }
            if (amount <= 0) {
                message = "The wager must be a integer greater than zero.";
                Toolkit.getDefaultToolkit().beep();
                repaint();
                return false;
                
            }
            if (amount > playerMoney) {
                message = "YOU CANNOT WAGER MORE MONEY THAN YOU HAVE!";
                Toolkit.getDefaultToolkit().beep();
                repaint();
                return false;
                
            }
            wager = amount;
            return true;
            
        }//end of the checkWager Method
        /**
         * getPlayerMoney Method -
         * @param Void
         * @return Int - Returns the playerMoney in the form of an integer
         */
        private int getPlayerMoney() {
            return playerMoney;
            
        }//end of the getPlayerMoney Method
        /**
         * doNewGame Method - Starts a new game.  This method is called in the CardPanel constructor,
         * and called in the actionPerformed method.  If the new game is started, each player is
         * dealt two cards.  If one of the players has blackjack (21), the game ends.  Otherwise,
         * gameInProgress is set to true and the game continues.
         * @param Void
         */
        @SuppressWarnings("UnnecessaryReturnStatement")
        public void doNewGame() {
            if (gameInProgress) {
                //If the current game is not over, it is an error to try to start a new game
                message = "The game is ongoing.  You have to finish this game!";
                Toolkit.getDefaultToolkit().beep();
                repaint();
                return;
            }
            if (playerMoney == 0) {
                //The player has run out of money.  Give the player another $1000
                playerMoney = 1000;
                
            }
            if (!checkWager()) {
                //If the player was not legal, an error message has been given
                return;
            }
            
            deck = new Deck();
            deck.shuffle();
            
            playerHand = new BlackjackHand();
            dealerHand = new BlackjackHand();
          
            playerHand.addCard(deck.dealCard());
            dealerHand.addCard(deck.dealCard());
            playerHand.addCard(deck.dealCard());
            dealerHand.addCard(deck.dealCard());
          
            if (dealerHand.getBlackjackValue() == 21) {
                message = "Sorry, you lose.  Dealer has Blackjack!";
                playerMoney -= wager;
                setGameStatus(false);
                
            } else if (playerHand.getBlackjackValue() == 21) {
                message = "You win!  You have Blackjack";
                playerMoney += wager;
                setGameStatus(false);
                
            } else {
                message = "You have " + playerHand.getBlackjackValue() + ".  Hit or Stand?";
                setGameStatus(true);
                
            }
            repaint();
            
        }//end of the doNewGame Method
        /**
         * doHit Method - Adds another card to the blackjack hand, when the actionPerformed 
         * method calls this method. 
         * @param Void
         */
        @SuppressWarnings("UnnecessaryReturnStatement")
        public void doHit() {
            if (gameInProgress == false) {//First check whether or not the game is in progress
                message = "Click \"New Game\" to start a new game.";
                Toolkit.getDefaultToolkit().beep();
                repaint();
                return;
                
            }
            playerHand.addCard(deck.dealCard());
            if (playerHand.getBlackjackValue() > 21) {
                message = "Sorry, you lose.  You've busted!";
                playerMoney -= wager;
                setGameStatus(false);
                
            } else if (playerHand.getCardCount() == 5) {
                message = "You win by taking 5 cards without going over 21.";
                playerMoney += wager;
                setGameStatus(false);
                
            } else {
                message = "You have " + playerHand.getBlackjackValue() + ".  Hit or Stand?";
                
            }
            repaint();
            
        }//end of the doHit Method
        /**
         * doStand Method - Stops adding cards to the blackjack hand, when the actionPerformed
         * method calls this method
         * @param Void
         */
        @SuppressWarnings("UnnecessaryReturnStatement")
        public void doStand() {
            if (gameInProgress == false) {
                message = "Click \"New Game\" to start a new game.";
                Toolkit.getDefaultToolkit().beep();
                repaint();
                return;
                
            }
         
           setGameStatus(false);
            
            while ((dealerHand.getBlackjackValue() <= 16) && 
                                (dealerHand.getCardCount() < 5)) {
                dealerHand.addCard(deck.dealCard());
            }
            if (dealerHand.getBlackjackValue() > 21) {
                message = "You win!  Dealer has busted with " + dealerHand.getBlackjackValue() + ".";
                playerMoney += wager;
                
            } else if (dealerHand.getCardCount() == 5) {
                message = "Sorry, you lose!  Dealer took 5 card without going over 21.";
                playerMoney -= wager;
                
            } else if (dealerHand.getBlackjackValue() > playerHand.getBlackjackValue()) {
                message = "Sorry, you lose!  The dealer has " + dealerHand.getBlackjackValue()
                           + " and you have " + playerHand.getBlackjackValue() + ".";
                
                playerMoney -= wager;
                
            } else if (dealerHand.getBlackjackValue() == playerHand.getBlackjackValue()) {
                message = "Sorry, you lose!  Dealer wins on ties.  Dealer has "
                           + dealerHand.getBlackjackValue() + " and you have "
                           + playerHand.getBlackjackValue() + ".";
                
                playerMoney -= wager;
                
            } else {
                message = "You win!  You have " + playerHand.getBlackjackValue()
                            + " and dealer has " + dealerHand.getBlackjackValue() + ".";
                
                playerMoney += wager;
                
            }
            repaint();
            
        }//end of the doStand Method
        /**
         * paint Method - Overrides the paint Method of the Abstract JPanel Class, and respond to the
         * CardPanel Class.  It draws the dealt cards and game status messages on the panel.
         * @param Graphics - the graphic context
         */
        @Override
        @SuppressWarnings("UnnecessaryReturnStatement")
        public void paint(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                                    RenderingHints.VALUE_RENDER_QUALITY);
            
            Font font = new Font("Montserrat Alternates", Font.PLAIN, 16);
            g.setColor(new Color(237, 237, 69));//yellow-goldish color
            g.setFont(font);
            
            if (cardImages == null) {
                g.drawString("Error:  Cannot get card images!", 15, 30);
                Toolkit.getDefaultToolkit().beep();
                return;
            }
           
            //Draw the playerMoney status message
            if (playerMoney > 0) {
                g2d.drawString("You have $" + getPlayerMoney(), 15, 400);
                
            } else {
                g2d.drawString("YOU ARE BROKE! (The Cashier will loan you another $1000.)", 
                                15, 400);
                Toolkit.getDefaultToolkit().beep();
                
            }
            //Draw the game status message
            g.drawString(message, 15, 425);
            
            if (dealerHand == null) {
                //The first game has not started
                return;
            }
            //Draw the labels for the dealer cards, user cards, amount of user money, and status of game
            g.drawString("Dealer's Cards", 15, 25);
            g.drawString("Your Cards", 15, 205);
            
            //Draw the dealer's cards.  If the game is still is progress, draw the first card 
           if (gameInProgress) {
               //Draw the first face down
               drawCard(g, null, 15, 33);
               
           } else {
               //Draw the face down card or the first card face up 
               
                drawCard(g, dealerHand.getCard(0), 15, 33);
           }
           
           //Draw the next cards face up
           for (int i = 1; i < dealerHand.getCardCount(); i++) {
                   drawCard(g, dealerHand.getCard(i), (15 + i * 110), 33);
                    
           }
           
           //Draw the player's cards all face up
           for (int i = 0; i < playerHand.getCardCount(); i++) {
               drawCard(g, playerHand.getCard(i), (15 + i * 110), 215);
               
           }
           repaint();
            
        }//end of the paint Method
        /**
         * drawCard Method - Draws a card in a rectangle with it upper left corner at a 
         * specified point (X, Y).  Drawing the card requires the cardDeck image file.
         * @param Graphics - The graphics context
         * @param Card - The card to be drawn.  If the value is null, then a face-down
         * card is drawn
         * @param Int - The X coordinate of the upper left corner of the card
         * @param Int - The Y coordinate of the upper left corner of the card
         */
        public void drawCard(Graphics g, Card card, int x, int y) {
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                              RenderingHints.VALUE_RENDER_QUALITY);
            int coordX;//the X coordinate of the upper left corner of the card inside the cardDeck
            int coordY;//the Y coordinate of the upper left corner of the card inside the cardDeck
            
            if (card == null) {
                //the coordinates for the face down card
                coordX = 2 * 74;
                coordY = 4 * 115;
                
            } else {
                coordX = (card.getRank() - 1) * 74;
                switch (card.getSuit()) {
                    case 0:
                        //the row of Clubs in the cardDeck image
                        coordY = 0;
                        break;
                    case 1:
                        //the row of Diamonds in the cardDeck image
                        coordY = 115;
                        break;
                    case 2:
                        //the row of Hearts in the cardDeck image
                        coordY = (2 * 115);
                        break;
                    default:
                        //the row of Spades in the cardDeck image
                        coordY = (3 * 115);
                        break;
                        
                }//end of the switch condition
            }
            g2d.drawImage(cardImages, x, y, (x + 100), (y + 148), coordX, coordY, (coordX + 73), 
                                (coordY + 115),(ImageObserver) this);
            
        }//end of the drawCard Method
        /**
         * actionPerformed Method - Overrides the actionPerformed Method of the ActionListener 
         * Interface, and responds to action events of the buttons
         * @param ActionEvent - the events of clicking the buttons
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            if (command.equalsIgnoreCase("   Hit   ")) {
                doHit();
                
            } else if (command.equalsIgnoreCase("Stand")) {
                doStand();
                
            } else if (command.equalsIgnoreCase("New Game")) {
                doNewGame();
                
            } 
            
        }//end of the actionPerformed Method
    }//end of the CardPanel Class
    /**
     * WelcomeDialog Class - Displays the welcome to Blackjack at the beginning of 
     * the game
     */
    class WelcomeDialog extends JDialog implements ActionListener {
        //Instance Variables for the WelcomeDialog Class
        private final JButton okayButton;
        //private final JTextArea textArea;
        private final JPanel buttonPanel;
        //private final JPanel imagePanel;
        private Image welcomeImage;
        
        /**
         * WelcomeDialog Constructor - Creates an instance of the WelcomeDialog
         * @param JFrame - the parent frame
         * @param String - the title
         * @param Boolean - the modal
         */
        @SuppressWarnings("LeakingThisInConstructor")
        public WelcomeDialog(JFrame frame, Boolean modal) {
            super(frame, modal);
            
            //The following 2 lines of code creates a null icon for the JDialog
            Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
            setIconImage(icon);
            
   
            try { 
                welcomeImage = ImageIO.read(WelcomeDialog.class.getResource("/img/welcomeImage.png"));
                JPanel imagePanel = new JPanel() {
                    /**
                    * paintComponent Method - Overrides the paintComponent Method of the Abstract JPanel
                    * Class, and redefines it add the image to the imagePanel
                    * @param Graphics - the graphics context
                    */  
                    @Override
                    public void paintComponent(Graphics g) {
                     
                        g.drawImage(welcomeImage, 0, 0, getWidth(), getHeight(), this);   
                    }
                };
                getContentPane().add(imagePanel, BorderLayout.CENTER);
             
            } catch (Exception ex) {
                String msg = ex.getMessage();
                JOptionPane.showMessageDialog(rootPane, msg);
            }

            setLocation(500, 300);
            setSize(640, 300);
   
            okayButton = new JButton(" OK ");
            okayButton.addActionListener(this);
            
            addWindowListener(new WindowAdapter() {
              /**
               * windowClosing Method - Overrides the windowClosing Method of the WindowAdapter
               * Interface, and responds to the closing of the WelcomeDialog
               * @param WindowEvent - the window event of closing the dialog
               */  
                @Override
                public void windowClosing(WindowEvent we) {
                    Window window = we.getWindow();
                    window.dispose();
                    
                }//end of the windowClosing Method for the WindowAdapter
            });//end of the Anonymous WindowAdapter
            
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(okayButton);
         
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);
            
        }//end of the WelcomeDialog Constructor
        /**
         * actionPerformed Method - Overrides the actionPerformed method of the ActionListener
         * Interface, and responds to okayButton
         * @param ActionEvent - the event of the button
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == okayButton) {
                this.dispose();
            }
        }//end of the actionPerformed Method
    }//end of the WelcomeDialog Class
    /**
     * main Method - Contains the command line arguments
     * @param String[] - the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | 
                 InstantiationException |  
                 IllegalAccessException | 
                 javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GamePanel.class.getName()).
                                    log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /** Create and display the Game */
        java.awt.EventQueue.invokeLater(() -> {
        JFrame frame = new JFrame("B l a c k j a c k");
        Image icon = Toolkit.getDefaultToolkit().getImage(GamePanel.class.
                                            getResource("/img/blackjack.png"));
        frame.setIconImage(icon);
        frame.setSize(800, 550);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setCursor(HAND_CURSOR);
        GamePanel gamePanel = new GamePanel();
        frame.setContentPane(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        });
    
    }//end of the main Method
}//end of the GamePanel Class