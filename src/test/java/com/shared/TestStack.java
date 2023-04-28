package com.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class TestStack{
    //TODO Better test plan
    Stack<Object> testStack = new Stack<>();

    @BeforeEach
    public void init(){
        this.testStack = new Stack<>();
    }

    @Test
    public void testStackAssignSameTypeVars(){
        String one = "Hello!";
        String two = "Bye!";

        testStack.push(one);
        testStack.push(two);
        
        assertEquals(testStack.getTopItem().getData(), two);
    }

    @Test
    public void testStackAssignDiffrentTypeVars(){
        String one = "Hello!";
        int two = 1;

        testStack.push(one);
        testStack.push(two);
        
        assertEquals(testStack.getTopItem().getData(), two);
    }

    @Test
    public void testStackBigStackSameTypes(){
        int amount = 1000;
        for(int i =0;i<=amount;i++){
            testStack.push(i);
        }

        for(int i = amount;i>0;i--){
            assertEquals(testStack.pop().getData(), i);
        }
    }

    @Test
    public void testStackLengthBig(){
        int amount = 1000;
        for(int i =0;i<amount;i++){
            testStack.push(i);
        }
        assertEquals(amount, testStack.length());

    }
}