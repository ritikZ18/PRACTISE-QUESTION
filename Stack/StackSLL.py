'''
Stack using Linked List

--> LIFO (last in first out)
nodes, next, item. self.start, temp 

1. create class Node, degine init()
2. create class Stack : for the stacck thing 
2. define is_empoty()
3. push()
3. pop()
3. peek()
4. define size()
 
'''


class Node: 
    def __init__(self, item=None, next=None):
        self.item = item
        self.next= next
        
class Stack : 
    def __init__(self):
        self.start = None # or start == top
        self.item_count = 0
        
    def is_empty(self):
        return self.start == None

    def push(self,data):
        # if not self.is_empty():
            n=Node(data, self.start)       # next node pointing
            self.start = n
            self.item_count+=1
    
    def pop(self):
        if not self.is_empty():
            data = self.start.item
            self.start = self.start.next
            self.item_count-=1 
            return data 
        else : 
            raise IndexError("Empty Stack / underflow")
        
    def peek(self):
        if not self.is_empty():
            return self.start.item
        else:
            raise self.is_empty()
        
    def size(self):
        return self.item_count
    

# s1 = Stack()
# s1.push(32)
# s1.push(354)
# s1.push(223)
# s1.push(452)
# s1.push(12)
# s1.push(0)
# s1.push(642)
# s1.push(2)
# print('Total Elements: ',s1.size())
# print('Top element: ',s1.peek())