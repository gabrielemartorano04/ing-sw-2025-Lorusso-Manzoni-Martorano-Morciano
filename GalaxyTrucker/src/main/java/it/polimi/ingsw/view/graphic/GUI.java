package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.view.UI;

import javax.swing.*;
import java.awt.*;
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
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/it/polimi/ingsw/view/WelcomeImage.jpeg"));
        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();

        int scaledWidth = 700;
        int scaledHeight = (originalHeight * scaledWidth) / originalWidth;

        Image scaledImage = originalIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Finestra uguale all'immagine ridimensionata
        frame.setSize(scaledWidth, scaledHeight);
        frame.setResizable(false);

        // Pannello principale con layout a strati
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(scaledWidth, scaledHeight));

        // Etichetta con immagine di sfondo
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(0, 0, scaledWidth, scaledHeight);
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER); // sfondo

        // Bottone trasparente
        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Super Comic", Font.BOLD, 20));
        startButton.setForeground(new Color(255, 222, 105));
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);

        // Posizionato sotto i personaggi
        startButton.setBounds(scaledWidth / 2 - 100, scaledHeight - 75, 200, 40);
        startButton.addActionListener(e -> showInsertNumPlayers());

        layeredPane.add(startButton, JLayeredPane.PALETTE_LAYER); // sopra

        // Pannello container
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.add(layeredPane, BorderLayout.CENTER);

        mainPanel.add(welcomePanel, "WELCOME");
        cardLayout.show(mainPanel, "WELCOME");
    }

    @Override
    public void showInsertNumPlayers() {

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.init();
            gui.showWelcome();
        });
    }
}
