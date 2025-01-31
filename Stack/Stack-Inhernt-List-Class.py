'''
Stack Extending List 

1. Define class stack by extendinh the list 
2. define is_empty() 
2. define push ()
3. define pop()
4. define peek()
5. in stack class, define size() --> to return the size of stack tha is  number of items 
6. implememnt a way to restrict the use of insert() 

'''


class Stack(list):
    def is_empty(self):
        return len(self)==0
    
    def push(self, data):
        self.append(data)
        
    def pop(self):
        if not self.is_empty() :
           return  super().pop()   #why super: because to call parent class
        
        else : 
            raise IndexError("Empty Stack")

    def peek(self):
        if not self.is_empty():
            return self[-1]       #stack object is considered as list object 
        
        else : 
            raise IndexError("Empty Stack")

  
    def size(self):
        if not self.is_empty():
            return len(self)
        else:
            print('Empty List ')

    # reestrciting insert() method , by "overriding" the method 

    def insert(self,index, data):
        raise AttributeError("Not such function ('insert') found !!")
    
s1 = Stack()
# s1.insert(1,12)
s1.push(12)
s1.push(11)
s1.push(10)
s1.push(678)
s1.push(132)
print(s1.peek())
print(s1.pop())
