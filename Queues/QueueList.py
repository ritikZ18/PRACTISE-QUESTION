"""

1. Class Queue and init() queue
    front = 0 \
    rear =  -1 
    size = 0 
    
2. is_empty

3. enqueue 
4. dequeue

5. get_front  , get_rear

6. size()

"""


class Queue:
    def __init__(self):
        self.items = []
        self.front = 0
        self.rear = -1
        self.size = 0

    def is_empty(self):
        return self.size == 0

    def enqueue(self, item):
            self.items.append(item)
            self.rear += 1
            self.size += 1

        # else:
        #     raise Exception("Memory Might Overflow")

    def dequeue(self):
        if self.is_empty():
            raise Exception("Memory might Empty")
        else:
            item = self.items.pop(0)
            self.size -= 1
            #  after popping the list shifts to left
            #    return the rear , with update weight
            self.rear = self.count - 1 if self.count > 0 else -1   # if the list becomes empty the rear = -1 
            return item

    def get_front(self):
        if self.is_empty():
            raise Exception("Queue is Empty")
        else:
            return self.items[0]

    def get_rear(self):
        if self.is_empty():
            raise Exception("Queue is Empty")
        else:
            return self.items[self.rear]

    def capacity(self):
        return self.size

    def __str__(self):
        return str(self.items)


q1 = Queue()
q1.enqueue(19)
q1.enqueue(18)
q1.enqueue(17)
q1.enqueue(16)
q1.enqueue(15)
print(q1)
print("Queue Size: ", q1.capacity())
print("Front Element: ", q1.get_front())
print("Last Element: ", q1.get_rear())
