//package it.polimi.ingsw.model.cards;
//
//import it.polimi.ingsw.utilities.exceptions.GeneralError;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class MeteorShowerTest {
//
//    private MeteorShower card;
//    ArrayList<int[]> direction;
//
//    @BeforeEach
//    void setUp() {
//        direction = new ArrayList<int[]>();
//        direction.add(new int[]{0, 1});
//        card=new MeteorShower(false,direction,1,"","");
//    }
//
//    @Test
//    void accept() {
//    }
//
//    @Test
//    void testAccept() {
//    }
//
//    @Test
//    void getDirections() {
//        assertEquals(direction,card.getMeteors());
//    }
//
//    @Test
//    void getCurrentMeteor() {
//        try {
//            assertNotNull(card.getCurrentMeteor());
//        } catch (GeneralError e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    void rollDice() {
//
//    }
//
//    @Test
//    void setAnswers() {
//    }
//
//    @Test
//    void cardEffect() {
//    }
//}