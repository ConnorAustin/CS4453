import sys, re

"""
Connor Austin
01/25/2018
AI
Homework 1
"""

"""
Helper function that reads input from stdin
Exits program on invalid input
Returns: (capacity, items)
"""
def read_input():
    capacity = None
    items = []
    for line in sys.stdin:
        line = line.strip()
        if len(line) == 0 or line[0] == '#':
            continue
        try:
            if not capacity:
                capacity = int(line)
            else:
                numbers = list(map(int, line.split()))
                assert len(numbers) == 2
                items.append({'w': numbers[0], 'v': numbers[1]})
        except:
            print('Warning: "' + line + '" is a bad line')

    return (capacity, items)

"""
Helper function to print result
"""
def print_knapsack(knapsack, items):
    print('* means the item was taken')
    print('')
    print('Knapsack:')
    price = 0
    weight = 0
    for item_index, item in enumerate(items):
        item = items[item_index]
        taken = ' '
        if item_index in knapsack:
            weight += item['w']
            price += item['v']
            taken = '*'
        print('\tItem {0}{1} (W: {2}, V: {3})'.format(taken, item_index, item['w'], item['v']))

    print('Total weight: ' + str(weight))
    print('Total value: ' + str(price))

"""
Performs the recursive DFS search
Returns a list of item indicies that are in the optimal knapsack
"""
def dfs(capacity, items, knapsack = [], total_value = 0):

    best_value = total_value
    best_knapsack = knapsack

    # For each item with index i
    for i, item in enumerate(items):
        # If the item is already included in our knapsack, we can't take it
        if i in knapsack:
            continue

        # Does this item fit in our knapsack?
        if capacity >= item['w']:
            # Add the new item
            new_knapsack = list(knapsack)
            new_knapsack.append(i)
            new_capacity = capacity - item['w']
            new_value = total_value + item['v']

            # DFS with the new conditions
            resulting_knapsack, resulting_value = dfs(new_capacity, items, new_knapsack, new_value)

            # See if this child is better than us
            if resulting_value > best_value:
                best_value = resulting_value
                best_knapsack = resulting_knapsack

    return (best_knapsack, best_value)

def main():
    capacity, items = read_input()
    knapsack, _ = dfs(capacity, items)
    print_knapsack(knapsack, items)

main()
