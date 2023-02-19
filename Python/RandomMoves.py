import random

from NoListsVersion import get_char, is_valid_move, make_move

def random_move(board, current_player):
    valid_moves = []
    for i in range(7):
        for j in range(7):
            if get_char(board, i, j) == current_player:
                for x in range(max(i-2, 0), min(i+3, 7)):
                    for y in range(max(j-2, 0), min(j+3, 7)):
                        if is_valid_move(board, i, j, x, y):
                            valid_moves.append((i, j, x, y))
    if not valid_moves:
        return False
    move = random.choice(valid_moves)
    make_move(board, move[0], move[1], move[2], move[3], current_player)
    return True
