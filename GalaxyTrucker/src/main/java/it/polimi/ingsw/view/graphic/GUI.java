package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.view.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class GUI extends UI {

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout; // utile per gestire schermate diverse (menu, partita, ecc)

    public GUI() {

    }

    public void init() {
        frame = new JFrame("Galaxy Trucker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    @Override
    public void showWelcome() {
        // Carica immagine di sfondo dal classpath
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/it/polimi/ingsw/view/WelcomeImage.jpeg"));
        Image bgImage = bgIcon.getImage();

        int width = 700;
        int height = 500;

        frame.setSize(width, height);
        frame.setResizable(false);

        // Pannello principale con immagine di sfondo disegnata direttamente
        JPanel welcomePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        welcomePanel.setLayout(null); // layout assoluto per posizionare il bottone
        welcomePanel.setPreferredSize(new Dimension(width, height));

        // Bottone START visibile
        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Bangers", Font.BOLD, 20));
        startButton.setForeground(new Color(255, 255, 255));
        startButton.setBackground(new Color(255, 222, 105, 180));
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setBounds(width / 2 - 110, height - 90, 220, 35);
        startButton.addActionListener(e -> showInsertNumPlayers());

        welcomePanel.add(startButton);

        mainPanel.add(welcomePanel, "WELCOME");
        cardLayout.show(mainPanel, "WELCOME");
    }

    @Override
    public void showInsertNumPlayers() {
        // Carica immagine di sfondo dal classpath
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/it/polimi/ingsw/view/EmptyWallpaper.jpeg"));
        Image bgImage = bgIcon.getImage();

        // Pannello personalizzato con immagine di sfondo
        JPanel insertPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        insertPanel.setLayout(new BoxLayout(insertPanel, BoxLayout.Y_AXIS));
        insertPanel.setOpaque(false);

        insertPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));

        // Etichetta della domanda
        JLabel questionLabel = new JLabel("Insert the number of truckers:");
        questionLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        questionLabel.setFont(new Font("Bangers", Font.BOLD, 20));
        questionLabel.setForeground(new Color(255, 222, 105));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        // Campo di inserimento numero
        JTextField numberField = new JTextField(10);
        numberField.setMaximumSize(new Dimension(200, 30));
        numberField.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        numberField.setHorizontalAlignment(JTextField.CENTER);
        numberField.setFont(new Font("Bangers", Font.PLAIN, 18));

        // Pulsante INVIA
        JButton submitButton = new JButton("INVIA");
        submitButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        submitButton.setEnabled(false);
        submitButton.setOpaque(true);
        submitButton.setBackground(Color.GRAY);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setMaximumSize(new Dimension(150, 40));
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Abilita il bottone quando c'è del testo
        numberField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                boolean enabled = !numberField.getText().trim().isEmpty();
                submitButton.setEnabled(enabled);
                submitButton.setBackground(enabled ? new Color(255, 222, 105) : Color.GRAY);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });

        // Azione su INVIO da tastiera o bottone
        ActionListener confirmAction = e -> {
            String input = numberField.getText().trim();
            if (!input.isEmpty()) {
                System.out.println("Numero giocatori inserito: " + input);
                // TODO: invia input al controller
            }
        };

        submitButton.addActionListener(confirmAction);
        numberField.addActionListener(confirmAction);

        // Aggiungi tutto al pannello
        insertPanel.add(Box.createVerticalGlue());
        insertPanel.add(questionLabel);
        insertPanel.add(numberField);
        insertPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        insertPanel.add(submitButton);
        insertPanel.add(Box.createVerticalGlue());

        // Mostra il pannello
        mainPanel.add(insertPanel, "INSERT_NUM_PLAYERS");
        cardLayout.show(mainPanel, "INSERT_NUM_PLAYERS");
    }

    @Override
    public void showChooseColor(EnumSet<PlayerColors> availableColors) {

    }

    @Override
    public void showColorTaken() {

    }

    @Override
    public void showNicknameTaken() {

    }

    @Override
    public void showPlayerJoined(String nickname) {

    }

    @Override
    public void showWaitingForPlayers(int currentPlayers) {

    }

    @Override
    public void showStartCountdown(int secondsLeft) {

    }

    @Override
    public void showSetupComplete() {

    }

    @Override
    public void showBuildPhaseStart() {

    }

    @Override
    public void showAvailableComponent(AvailableComponents components) {

    }

    @Override
    public void showKeptComponents(List<Component> kept) {

    }

    @Override
    public void showSelectComponent() {

    }

    @Override
    public void showBuildOptions() {

    }

    @Override
    public void showDiscardComponent(Component component) {

    }

    @Override
    public void showComponentOptions() {

    }

    @Override
    public void showComponentPlaced(Component component, int x, int y) {

    }

    @Override
    public void showInvalidPlacement(String reason) {

    }

    @Override
    public void showHourglassTurned() {

    }

    @Override
    public void showTimeExpired() {

    }

    @Override
    public void showWaitForOthers() {

    }

    @Override
    public void showBuildComplete() {

    }

    @Override
    public void showFlightPhaseStart() {

    }

    @Override
    public void showCardDrawn(Card card) {

    }

    @Override
    public void showCardEffectStart(Card card) {

    }

    @Override
    public void showCardEffectEnd(Card card) {

    }

    @Override
    public void showTruckerMoved(String nickname, int newPosition) {

    }

    @Override
    public void showTruckerWithdrawn(String nickname) {

    }

    @Override
    public void showTruckerLostAllCrew(String nickname) {

    }

    @Override
    public void showGoodsGained(Goods goods) {

    }

    @Override
    public void showGameEnded() {

    }

    @Override
    public void showScores(Map<String, Integer> finalScores) {

    }

    @Override
    public void showWinner(String nickname) {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showConnectionLost() {

    }

    @Override
    public void showInvalidCommand(String expectedFormat) {

    }

    @Override
    public void askIndex() {

    }

    @Override
    public void askCoordinates() {

    }

    @Override
    public void showGoodsToRemove(List<Goods> goods) {

    }

    @Override
    public void askGoodsIndexesToRemove() {

    }

    @Override
    public void askPlacedComponent() {

    }

    @Override
    public void showPlayerShipStatus(int position, int batteryCount, int humanCount, int cannonPower, int enginePower, Map<String, Integer> goodsByColor) {

    }

    @Override
    public void showShipLayout(String nickname, Component[][] layout) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showTrack(Map<String, Integer> truckers) {

    }

    @Override
    public void showBooleanOption(String message) {

    }

    @Override
    public void updateHourglass(int secondsLeft) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.init();
            gui.showWelcome();
        });
    }
}
