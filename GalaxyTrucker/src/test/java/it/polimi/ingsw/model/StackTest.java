package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Epidemic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

    private Stack stack1;
    private Stack stack2;


    @BeforeEach
    void setUp() {
        stack1=new Stack(1);
        stack2=new Stack(2);
    }

    @Test
    void addCardAndGetStack() {
        Card card1=new AbandonedShip(false,2,2,1,2,"","",0);
        Card card2=new Epidemic(false,0,"","",2);
        stack1.addCardInStack(card1);
        stack1.addCardInStack(card2);
        ArrayList<Card> list=new ArrayList<>();
        list.add(card1);
        list.add(card2);
        assertEquals(list.size(),stack1.getStack().size());
        boolean equals=true;
        for(int i=0;i<list.size();i++){
            if(!list.get(i).equals(stack1.getStack().get(i))){
                equals=false;
            }
        }
        assertTrue(equals);
    }

    @Test
    void dropStackSetSelectedAndIsSelected() {
        stack1.setSelected();
        assertTrue(stack1.isSelected());
        stack1.dropStack();
        assertFalse(stack1.isSelected());
    }

    @Test
    void getIndexOfThis() {
        assertEquals(1,stack1.getIndexOfThis());
    }

}