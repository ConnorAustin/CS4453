"""
Connor Austin
02/02/2018
"""

# search.py
# ---------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to
# http://inst.eecs.berkeley.edu/~cs188/pacman/pacman.html
#
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).

"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()

def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

"""
Generic implementation of all searches (dfs, bfs, ucs, a*)
"""
def starSearch(problem, data_structure, use_cost = False, heuristic = None):
    queue = data_structure()

    def push(node, cost):
        if use_cost:
            queue.push(node, cost)
        else:
            queue.push(node)

    push({'state': problem.getStartState(), 'path': []}, 0)

    visited = []
    while not queue.isEmpty():
        node = queue.pop()

        if node['state'] in visited:
            continue

        if problem.isGoalState(node['state']):
            return node['path']

        # Mark state as visited
        visited.append(node['state'])

        # Loop through all children
        for s in problem.getSuccessors(node['state']):
            # Get info from successor
            child_state = s[0]
            child_direction = s[1]

            if child_state not in visited:
                path_to_child = node['path'] + [child_direction]
                child = {'state': child_state, 'path': path_to_child}

                cost = problem.getCostOfActions(path_to_child)
                if not heuristic is None:
                    cost += heuristic(child_state, problem)
                push(child, cost)

        if queue.isEmpty():
            return node['path']

def depthFirstSearch(problem):
    return starSearch(problem, util.Stack)

def breadthFirstSearch(problem):
    return starSearch(problem, util.Queue)

def uniformCostSearch(problem):
    return starSearch(problem, util.PriorityQueue, True)

def aStarSearch(problem, heuristic=nullHeuristic):
    return starSearch(problem, util.PriorityQueue, True, heuristic)

def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
