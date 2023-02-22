package com.shared;


/***
 Custom implementation of the Stack data type.
 Contains the first item in the stack as {@code topItem} and uses the methods {@code push, pop} to interact with the stack.
***/
public class Stack<T> {
    private StackItem<T> topItem;

    public StackItem<T> getTopItem() {
        return topItem;
    }

    /***
     Removes first item from the stack and returns it to the caller.
     Throws {@code StackOverflowError} when the stack is empty.
     ***/
    public void push(T data){
        topItem = new StackItem<T>(data, topItem);
    }
    
    /***
     Removes first item from the stack and returns it to the caller.
     Throws {@code StackOverflowError} when the stack is empty.
    ***/
    public StackItem<T> pop(){
        if(topItem == null){
            throw new StackOverflowError("Stack Empty!");
        }
        StackItem<T> tempitem = topItem;
        topItem = topItem.PreviousPointer;
        return tempitem;
    }

    /***
     Contains Data and a pointer to the previous StackItem in its stack.
     When first in the stack, the pointer will be {@code null}
     ***/
    public class StackItem<I> {

        private I Data;
        private StackItem<I> PreviousPointer;
    
        public StackItem(I Data, StackItem<I> PreviousPointer){
            this.Data = Data;
            this.PreviousPointer = PreviousPointer;
    
        }

        public I getData() {
            return Data;
        }
        public void setData(I data) {
            Data = data;
        }

        public StackItem<I> getPreviousPointer() {
            return PreviousPointer;
        }
    }
}
