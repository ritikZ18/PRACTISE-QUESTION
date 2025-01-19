# This the DLL Doubly LinkedList impl for the DSA

"""
1. Create Node
2. Define DLL method with __init__, to init the ref var
3. check for empty list
4. define functions like:
        i. insert
        ii. delete
        iii. search 
        iv. search
        v. etc...
        
5. implement iterator to access all elements in the list in sequence
"""


class Node:
    def __init__(self, prev=None, item=None, next=None):
        self.prev = prev
        self.item = item
        self.next = next


class DLL:

    # Inititate the start/head in the list
    def __init__(self, start=None):
        self.start = start

    def is_empty(self):
        return self.start == None

    def insert_at_start(self, data):
        n = Node(None, data, self.start)
        if not self.is_empty():
            self.start.prev = n
        self.start = n

    def insert_at_last(self, data):
        temp = self.start
        if self.start != None:
            while temp.next is not None:
                temp = temp.next

            n = Node(temp, data, None)
            if temp == None:
                temp = n    
            else:
                temp.next = n
            
            

    def search(self, data):
        temp = self.start
        while temp is not None:
            if temp.item == data:
                return temp  # ref no the temp.item its data
            temp =temp.next
        return None

    def insert_after(self, data, new_data):
        temp = self.start
        while temp is not None and temp.item != data:
            temp = temp.next

        if temp is None:
            return

        n = Node(temp, new_data, temp.next)
        if temp.next is not None:
            temp.next.prev = n
        temp.next = n

    def display(self):
        temp = self.start
        while temp is not None:
            print(temp.item, end=" ")
            temp = temp.next
        print()

    def delete_at_first(self):
        # temp = self.start
        if self.start is not None:
            self.start = self.start.next
            if self.start is not None:
                self.start.prev = None


    def delete_at_last(self):
        temp = self.start
        if temp.next is None:  # for single node in list
            temp = None
            return
        while temp.next.next is not None:
            temp = temp.next
        temp.next = None

    def delete_after(self, data):
        temp = self.start
        while temp.next is not None and temp.item != data : 
            temp = temp.next
            
        if temp is None or temp.next is None : 
            return
        
        temp.next = temp.next.next
        if temp.next is not None:
             temp.next.prev = temp


myList = DLL()
myList.insert_at_start(10)
myList.insert_at_start(11)
myList.insert_at_start(12)
myList.insert_at_start(13)
myList.insert_at_start(14)
myList.insert_after(12, 39)
myList.insert_after(13, 39)
myList.insert_at_last(69)
myList.display()

myList.delete_at_first()
myList.display()

myList.delete_at_last()
myList.display()

myList.delete_after(13)
myList.display()
