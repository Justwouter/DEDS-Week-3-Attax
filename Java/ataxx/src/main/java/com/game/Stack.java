package com.game;

public class Stack<T> {
    public StackItem<T> firstItem;

    public void push(T data){
        StackItem<T>tempitem = firstItem;
        firstItem = new StackItem<T>(data, tempitem);
    }
    
    public StackItem<T> pop(){
        StackItem<T> tempitem = firstItem;
        firstItem = firstItem.PreviousPointer;
        return tempitem;
    }


    public class StackItem<I> {

        private I Data;
        public StackItem<I> PreviousPointer;
    
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
    }
}
