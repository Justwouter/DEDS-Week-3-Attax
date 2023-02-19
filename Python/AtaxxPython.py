#!/usr/bin/env python3



#TODO Write checks for winning conditions, clean up.

board = [['O' for _ in range(7)] for _ in range(7)]
board[0][0] = 'H'
board[6][6] = 'B'
board[2][2] = 'B'

# Define a function to print the current state of the game board
def print_board(board):
    
    numbers = ""
    borders = ""
    for _ in range(len(board)):
        borders+="_"
        numbers+=str(_)
    print(" _"+'\u0332 \u0332'.join(numbers)+"_")
    
    
    counter = 0
    for row in board:
        print(str(counter)+"|"+' '.join(row))
        counter +=1

# Define a function to check if a given move is valid
def is_valid_move(board, x1, y1, x2, y2):
    if board[x1][y1] == 'O':
        return False
    if board[x2][y2] != 'O':
        return False
    if abs(x1 - x2) > 2 or abs(y1 - y2) > 2:
        return False
    if abs(x1 - x2) == 2 and abs(y1 - y2) == 2:
        return False
    return True

# Define a function to make a move
def make_move(board, x1, y1, x2, y2, current_player):
    if not is_valid_move(board, x1, y1, x2, y2):
        return False
    if abs(x1 - x2) <= 1 and abs(y1 - y2) <= 1:
        board[x2][y2] = board[x1][y1]
        for i in range(max(x2-1, 0), min(x2+2, 7)):
            for j in range(max(y2-1, 0), min(y2+2, 7)):
                if board[i][j] != current_player and board[i][j] != 'O':
                    board[i][j] = current_player
    else:
        board[x2][y2] = board[x1][y1]
        board[x1][y1] = 'O'
    return True

# Play the game
current_player = 'H'
while True:
    print_board(board)
    
    #Check for winning condition.
    if( not "O" in board):
        continue
    
    print("It's player", current_player, "'s turn")
    move = input("Enter your move as x1 y1 x2 y2: ")
    x1, y1, x2, y2 = [int(coord) for coord in move.split()]
    if make_move(board, x1, y1, x2, y2, current_player):
        if current_player == 'H':
            current_player = 'B'
        else:
            current_player = 'H'
    else:
        print("Invalid move, try again")