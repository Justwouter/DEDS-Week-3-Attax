import math
import random
import time
from NoListsVersion import get_char, is_game_over, is_valid_move, make_move
from RandomMoves import random_move


class Node:
    def __init__(self, board, parent, current_player):
        self.board = board
        self.parent = parent
        self.current_player = current_player
        self.children = []
        self.visits = 0
        self.wins = 0
        
    def expand(self):
        valid_moves = []
        for i in range(7):
            for j in range(7):
                if get_char(self.board, i, j) == self.current_player:
                    for x in range(max(i-2, 0), min(i+3, 7)):
                        for y in range(max(j-2, 0), min(j+3, 7)):
                            if is_valid_move(self.board, i, j, x, y):
                                new_board = make_move(self.board, i, j, x, y, self.current_player)
                                child = Node(new_board, self, 'B' if self.current_player == 'H' else 'H')
                                self.children.append(child)
        return self.children
        
    def select(self):
        c = 1.0 / math.sqrt(2.0)
        max_ucb = -float('inf')
        selected_child = None
        for child in self.children:
            if child.visits == 0:
                return child
            ucb = (child.wins / child.visits) + c * math.sqrt(math.log(self.visits) / child.visits)
            if ucb > max_ucb:
                max_ucb = ucb
                selected_child = child
        return selected_child
        
    def update(self, result):
        self.visits += 1
        self.wins += result
        
    def is_terminal(self):
        return not self.children and is_game_over(self.board)
    
def uct_search(board, current_player, max_time=10):
    root = Node(board, None, current_player)
    start_time = time.time()
    while time.time() - start_time < max_time:
        node = root
        while node.children:
            node = node.select()
        if not node.visits == 0:
            node.expand()
            node = random.choice(node.children)
        result = simulate(node.board, node.current_player)
        while node is not None:
            node.update(result)
            node = node.parent
    return max(root.children, key=lambda c: c.visits)

def mcts_move(board, current_player, max_time=10):
    if random.random() < 0.1:
        return random_move(board, current_player)
    node = uct_search(board, current_player, max_time)
    best_child = max(node.children, key=lambda c: c.visits)
    make_move(board, best_child.parent_i, best_child.parent_j, best_child.child_i, best_child.child_j, current_player)
    return True

def simulate(board, current_player):
    while not is_game_over(board):
        valid_moves = []
        for i in range(7):
            for j in range(7):
                if get_char(board, i, j) == current_player:
                    for x in range(max(i-2, 0), min(i+3, 7)):
                        for y in range(max(j-2, 0), min(j+3, 7)):
                            if is_valid_move(board, i, j, x, y):
                                valid_moves.append((i, j, x, y))
        if not valid_moves:
            break
        move = random
        
        
