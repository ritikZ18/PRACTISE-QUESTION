'''
Steps for SLL

1. Create Node
2. Check fo rEmpty
3. insert_start  :
        self.start = None
        item = data
4. Insert_Last
        while temp.next not none
            temp = temp.next
        temp.next = n
5. Search 
        search by item
            if item == data
            return 
6. insert_after
        while temp == item/data
            temp.next 
        temp.next = n
7. iterator , will figure out 
'''


class Node : 
    
    def __init__(self,item=None,next=None):
        self.item = item
        self.next = next
        
class SLL  :
    
    def __init__(self, start=None):
        self.start = start 
        
    def is_empty(self):
        return self.start == None
    
    def insert_at_first(self, data):
        n = Node(data)
        self.start = n 
        
    def insert_at_last(self,data):
        n = Node(data)
        if not self.is_empty():
            temp = self.start
            while temp.next is not None:
                temp = temp.next
            temp.next = n
        else : 
            self.start = n  # empty list, first element 
            
    def search(self,data):
        temp = self.start #init the temp 
        while temp.next is not None:
            if temp.item == data:
                return temp
            temp = temp.next  #else continie go futher
        
        return None
    
    
    def delete_first(self,data):
        temp = self.start
        while self.is_empty : 
            temp = temp.next
        return None
    
    def delete_last(self,data):
        if self.start is None:
            pass
        elif self.start.next is None:
            self.start = None
        else:
            temp = self.start
            while temp.next is not None:
                temp = temp.next
            temp = None    
    
    def insert_after(self, temp, data):
        while temp is not None:
            n= Node(data, temp.next)
            temp.next = n
            
    def delete_after(self, data):
       if self.start is None : 
           pass
       
       elif self.start.next is None:
          if self.item == data: 
           self.start = None
        
       else:
           temp = self.start 
           if temp.item == data:
               temp = temp.next
           else:
               while temp.next is not None:
                    if temp.next.item == data:
                       temp.next = temp.next.next
                       break
                    temp = temp.next    

    def display(self):
        temp =self.start
        while temp is not None:
            print(temp.item, end=' ')
            temp =temp.next
             

myList = SLL()
myList.insert_at_first(10)
myList.display()