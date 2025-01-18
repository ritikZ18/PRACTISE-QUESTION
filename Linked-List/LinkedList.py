class Node:
     def __init__(self, item=None, next=None):
                self.item = item
                self.next = next 
                


class SLL:

    def __init__(self, start=None):
        self.start = start 
        
    
    # lets check first is the list empty
    def is_empty(self):
        return self.start == None
    
    def insert_at_start(self, data):
         n = Node(data,self.start) #new node object created
         self.start = n
         
    def insert_at_last(self, data):
        n = Node(data)      #next=None, as we havent defined the other value
        if not self.is_empty():
            temp = self.start   # created temp to store the next(ref's) to check whether which node is last
            while temp.next is not None:
                temp = temp.next
            temp.next = n
            
        else:
            self.start = n 
            
    def search(self,data):
        temp = self.start 
        while temp is not None:
            if temp.item == data:
                return temp
            temp = temp.next        #going further one by one Node
        return None
    
    
    def insert_after(self,temp, data):      
        if temp is not None:
            n = Node(data, temp.next)       
            temp.next = n 
            
    def print_list(self):
        temp = self.start 
        while temp is not None:
            print(temp.item, end=' ')   
            temp = temp.next
            
            
            
    def delete_at_start(self):
        if self.start is not None:
            self.start = self.start.next
            
        
        
    def delete_at_end(self, temp):
        if self.start is None:
            pass
        elif self.start.next is None:
            self.start = None
        else:
            temp = self.start
            while temp.next.next is not None:
                temp = temp.next
            temp.next = None
            
      
    def delete_after(self, data):
        if self.start is None:
            pass
        elif self.start.next is None :
            if self.start.item == data : 
                self.start = None
                
        else:
            temp = self.start
            if temp.item == data:
                self.start = temp.next  #next node ref into temp.next
            else:
                while temp.next is not None:
                    if temp.next.item == data:
                        temp.next = temp.next.next
                        break
                    temp = temp.next

                
            
        
    

myList = SLL()
myList.insert_at_start(10)
myList.insert_at_start(20)
myList.insert_at_start(5)
myList.insert_after(myList.search(20),90)
myList.delete_after(20)

myList.print_list()
             


        
        
