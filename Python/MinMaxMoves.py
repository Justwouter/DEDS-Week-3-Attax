from NoListsVersion import get_char, is_valid_move, make_move


def minimax(board, depth, alpha, beta, is_maximizing_player, current_player):
    if depth == 0:
        return evaluate_board(board, current_player)
    if is_maximizing_player:
        max_eval = float('-inf')
        for i in range(7):
            for j in range(7):
                if get_char(board, i, j) == current_player:
                    for x in range(max(i-2, 0), min(i+3, 7)):
                        for y in range(max(j-2, 0), min(j+3, 7)):
                            if is_valid_move(board, i, j, x, y):
                                new_board = make_move(
                                    board, i, j, x, y, current_player)
                                eval = minimax(
                                    new_board, depth-1, alpha, beta, False, 'B' if current_player == 'H' else 'H')
                                max_eval = max(max_eval, eval)
                                alpha = max(alpha, eval)
                                if beta <= alpha:
                                    break
        return max_eval
    else:
        min_eval = float('inf')
        for i in range(7):
            for j in range(7):
                if get_char(board, i, j) == current_player:
                    for x in range(max(i-2, 0), min(i+3, 7)):
                        for y in range(max(j-2, 0), min(j+3, 7)):
                            if is_valid_move(board, i, j, x, y):
                                new_board = make_move(
                                    board, i, j, x, y, current_player)
                                eval = minimax(
                                    new_board, depth-1, alpha, beta, True, 'B' if current_player == 'H' else 'H')
                                min_eval = min(min_eval, eval)
                                beta = min(beta, eval)
                                if beta <= alpha:
                                    break
        return min_eval


def evaluate_board(board, current_player):
    h_count = 0
    b_count = 0
    for i in range(7):
        for j in range(7):
            if board[i][j] == 'H':
                h_count += 1
            elif board[i][j] == 'B':
                b_count += 1
    if current_player == 'H':
        return h_count - b_count
    else:
        return b_count - h_count


def minimax_move(board, current_player):
    valid_moves = []
    for i in range(7):
        for j in range(7):
            if get_char(board, i, j) == current_player:
                for x in range(max(i-2, 0), min(i+3, 7)):
                    for y in range(max(j-2, 0), min(j+3, 7)):
                        if is_valid_move(board, i, j, x, y):
                            valid_moves.append((i, j, x, y))

    if not valid_moves:
        return None

    best_move = valid_moves[0]
    max_player = current_player
    max_value = float('-inf')

    for move in valid_moves:
        new_board = make_move(board, move)
        value = minimax(new_board, 2, other_player(current_player), max_player, float('-inf'), float('inf'))
        if value > max_value:
            max_value = value
            best_move = move

    return best_move
