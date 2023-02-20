package com.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;



public class TestStack{
    static Stack<Object> testStack = new Stack<>();

    @BeforeAll
    public static void init(){
        testStack = new Stack<>();
    }

    @Test
    public void testStackAssignSameTypeVars(){
        String one = "Hello!";
        String two = "Bye!";

        testStack.push(one);
        testStack.push(two);
        
        assertEquals(testStack.getFirstItem().getData(), two);
    }

    @Test
    public void testStackAssignDiffrentTypeVars(){
        String one = "Hello!";
        int two = 1;

        testStack.push(one);
        testStack.push(two);
        
        assertEquals(testStack.getFirstItem().getData(), two);
    }

    @Test
    public void testStackBigStackSameTypes(){
        int amount = 1000;
        for(int i =0;i<=amount;i++){
            testStack.push(i);
        }

        for(int i = amount;i>0;i--){
            System.out.println(i);
            assertEquals(testStack.pop().getData(), i);
        }
    }
}