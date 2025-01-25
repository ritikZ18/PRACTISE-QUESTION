class Node : 
    def __init__(self, item, prev=None, next= None):
        self.item = item
        self.prev= prev
        self.next = next

class CDLL : 
    def __init__(self, start=None):
        self.start = start
        
    def is_empty(self):
        return self.start == None 
        
    def insert_at_start(self, data):
        n= Node(data)
        temp = self.start
        n.next = temp
        if self.is_empty():
            n.next = n
            n.prev = n
            self.start = n
        else: 
            n.next = temp
            n.prev = temp.prev  
            temp.prev.next = n  #last node poitning to its next node as n(insert at first)
            temp.prev = n       #start ka prev contains new node
            self.start = n

    def insert_at_last(self,data):
        n= Node(data)
        temp = self.start
        if self.is_empty():
            n.next = n
            n.prev = n
            temp = n

        else : 
            n.next = temp
            n.prev = temp.prev      # temp.prev is last node ref towards first
            n.prev.next = n 
            temp.prev = n
            
    def search(self, data):
        temp =  self.start
        if temp == None:
            pass
        else : 
            if temp.item == data:
                return temp
            else : 
                temp = temp.next
                
            while temp is not self.start: 
                if temp.item ==data:
                    return temp
                temp = temp.next 
            return None

    def insert_after(self,temp,  data):
        if temp is not None : 
            n = Node(data)
            n.next = temp.next 
            n.prev = temp
            temp.next.prev = n
            temp.next = n
            

    def print(self):
        temp = self.start 
        if self.is_empty():
            return None
        elif temp is not None : 
            print(temp.item, end=' ')
            temp = temp.next
            while temp != self.start : 
                print(temp.item, end=' ')
                temp = temp.next 
                
    def delete_at_start(self):
        temp = self.start
        if temp is not None :
            if temp.next == temp :
                self.start == None
            else : 
                temp.next.prev  = self.start.prev
                self.start.prev.next = temp.next 
                self.start = temp.next 
                
    def delete_at_end(self):
        if self.start is not None:
            if self.start.next == self.start : 
                self.start = None
            else : 
                 self.start.prev.prev.next = self.start
                 self.start.prev = self.start.prev.prev
                 
    def delete_after(self,temp,  data):
        if self.start is not None: 
            if self.start.item == data :
                if self.start.next == self.start : 
                    self.start  = None
                else : 
                    temp = self.start 
                    if temp.item == data : 
                        self.delete_at_start()
                    else : 
                        temp =temp.next
                        while temp != self.start : 
                            if temp.item == data : 
                                temp.next.prev = temp.prev
                                temp.prev.next = temp.next 
                                


    def __iter__(self):
        return CDLLIterator(self.start)

    
class CDLLIterator : 
    def __init__(self,start):
        self.current = start
        self.count = 0 
        
    def ___iter__(self):
        return iter

    def __next__(self):
        if self.current is None:
            raise StopIteration
        else : 
            self.count = 1 
        data = self.current.item
        self.current  = self.current.next
            
                    
                
                
    

myList = CDLL()
myList.insert_at_start(10)
myList.insert_at_start(11)
myList.insert_at_start(12)
myList.insert_at_start(13)
myList.print()

            
                
            
        
                
        