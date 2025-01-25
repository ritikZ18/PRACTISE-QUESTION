class Node : 
    def __init__(self, data, next):
        self.data = data
        self.next = next 
        
class CircularSLL():
    
    
    # Inititate the start/head in the list
    def __init__(self, last=None):
        self.last = last

    
    def is_empty(self):
        return self.last == None

    def insert_at_first(self,data):
        n = Node(data )
        if self.is_empty():
            n.next = n
            self.last = n
        else : 
           n.next = self.last.next
           n = self.last.next

    def insert_at_last(self, data):
        n = Node(data)
        if self.is_empty():
            n.next = n
            self.last = n
        else : 
            n.next = self.last.next
            self.last.next = n
            self.last = n   #now the last is the n [new Node]
    
    def search(self,data):
        if self.is_empty():
            return None
        else: 
            temp = self.last.next
            while temp.next is not self.last:
                if self.item == data:
                    return temp
                temp = temp.next

            if temp.item == data : 
                return temp
            else : 
                return None
    
    def display(self):
        temp = self.last.next
        while temp is not self.last : 
            temp = temp.next 
            print(self.item, end=' ')

    def insert_after(self, temp, data):
        if temp is not None : 
            n = Node(data, temp.next)
            temp.next  = n 
            if temp == self.last: 
                self.last = n
                


        
            
        

            
        
            
            
            
    