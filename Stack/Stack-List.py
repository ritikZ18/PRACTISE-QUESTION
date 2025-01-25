class Stack : 
    def __init__(self):
        self.items=[]
    
    def is_empty(self):
        return len(self.items)==0

    def push(self,data):
        self.items.append(data)
        
    def pop(self):
        if self.is_empty():
            raise IndexError("Stack Error")
        else:
            return self.items.pop()
        
    def peek(self):
        if self.is_empty():
             raise IndexError("Stack Error")
        else:
            return self.items[-1]
        
    def size(self):
        return len(self.items)

s1 = Stack()
s1.push(23)
s1.push(25)
s1.push(26)
s1.push(27)
s1.push(28)
s1.push(29)
s1.push(21)
print("First/Top Element ",s1.peek())
print("removed element",s1.pop())
