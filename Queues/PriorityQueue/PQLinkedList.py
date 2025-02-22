#  based on the preferences defined 
# element processing is depended on the priority assigned !

'''
1. assign the numbers to pq
2. the lower the number the higher priority

--> push(), pop(), size() , is_empty()

will implemebt using : 

1, Linked List
2. Heap
3. List
'''

# //Using LinkedList
class Node : 
    
    def __init__(self, item=None, priority = None ,next=None):
        self.item = item 
        self.priority = priority
        self.next = next 
        
class priorityQueue : 
    
    def __init__(self):
        self.start = None
        self.item_count = 0 
        
    def push(self,data, priority):
        n = Node(data, priority):
            


