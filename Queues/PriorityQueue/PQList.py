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
        self.items = []
    
    def is_empty(self):
        return len(self.items) == 0
    
    def push(self, data, priority):
        index = 0 
        
        while index < len(self.items) and self.items[index][1]<=priority : 
            index +=1
            
        self.items.insert(index, ( data ,priority))
        
    def pop(self):
        if self.is_empty():
            raise IndexError("Priority Queue is Empty")
        
        return self.items.pop(0)[0]  #tuples first element must be data
    
    def size(self):
        return len(self.items)
    
pq = PriorityQueue()
pq.push(32,4)
pq.push(3,5)
pq.push("Ritik",1)
pq.push(2,6)
pq.push(-32,1)

while not pq.is_empty():
    print(pq.pop())

            
