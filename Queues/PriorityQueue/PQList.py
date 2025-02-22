'''
1. assign the numbers to pq
2. the lower the number the higher priority

--> push(), pop(), size() , is_empty()

will implemebt using : 

1, Linked List
2. Heap
3. List
'''


class PriorityQueue :
    
    def __init__(self):
        self.items = None
    
    def push(self, data, priority):
        index = 0 
        
        while index < len(self.items) and self.items[index][1]<=priority : 
            index +=1
            
