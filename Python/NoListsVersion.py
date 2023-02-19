# Define the initial state of the game
board = ""
for i in range(7):
    for j in range(7):
        if i == 0 and j == 0:
            board += 'H'
        elif i == 6 and j == 6:
            board += 'B'
        else:
            board += 'O'

# Define a function to print the current state of the game board


def print_board(board):
    for i in range(7):
        for j in range(7):
            print(board[i*7+j], end=" ")
        print()

# Define a function to get the character at a specific position on the board


def get_char(board, i, j):
    return board[i*7+j]

# Define a function to set the character at a specific position on the board


def set_char(board, i, j, char):
    board = board[:i*7+j] + char + board[i*7+j+1:]
    return board

# Define a function to check if a given move is valid


def is_valid_move(board, x1, y1, x2, y2):
    if get_char(board, x1, y1) == 'O':
        return False
    if get_char(board, x2, y2) != 'O':
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
        board = set_char(board, x2, y2, get_char(board, x1, y1))
        for i in range(max(x2-1, 0), min(x2+2, 7)):
            for j in range(max(y2-1, 0), min(y2+2, 7)):
                if get_char(board, i, j) != current_player and get_char(board, i, j) != 'O':
                    board = set_char(board, i, j, current_player)
    else:
        board = set_char(board, x2, y2, get_char(board, x1, y1))
        board = set_char(board, x1, y1, 'O')
    return board

#Check for game end
def is_game_over(board):
    h_count = 0
    b_count = 0
    for i in range(7):
        for j in range(7):
            if board[i][j] == 'H':
                h_count += 1
            elif board[i][j] == 'B':
                b_count += 1
    return h_count == 0 or b_count == 0 or (not has_valid_moves(board, 'H') and not has_valid_moves(board, 'B'))

#Seems pretty clear right?
def has_valid_moves(board, player):
    for i in range(7):
        for j in range(7):
            if board[i][j] == player:
                for x in range(max(i-2, 0), min(i+3, 7)):
                    for y in range(max(j-2, 0), min(j+3, 7)):
                        if is_valid_move(board, i, j, x, y):
                            return True
    return False


# Play the game
current_player = 'H'
while True:
    print_board(board)
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



