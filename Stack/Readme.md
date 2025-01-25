### Stack Data Structure and Algorithm (DSA)

#### **What is a Stack?**
A stack is a **linear data structure** that follows the **Last In, First Out (LIFO)** principle, meaning the last element added to the stack is the first one to be removed.

---

### **Key Properties of a Stack**
1. **LIFO Principle**: The last element inserted is the first to be removed.
2. **Operations**:
   - **Push**: Add an element to the top of the stack.
   - **Pop**: Remove and return the top element of the stack.
   - **Peek/Top**: Retrieve the top element without removing it.
   - **IsEmpty**: Check if the stack is empty.
   - **IsFull** (optional): Check if the stack is full (for fixed-sized stacks).
3. **Applications**:
   - Expression evaluation (e.g., postfix, prefix).
   - Balanced parentheses checking.
   - Undo functionality in text editors.
   - Backtracking algorithms (e.g., maze solving, DFS).
   - Function call stack in programming.

---

### **Step-by-Step Explanation of Stack**

#### **1. Stack Representation**
A stack can be implemented using:
   - **Array**: Fixed size and efficient.
   - **Linked List**: Dynamic size, requires more memory.

---

#### **2. Stack Operations**

##### **Push Operation**
   - Add an element to the top of the stack.
   - **Steps**:
     1. Check if the stack is full (optional in dynamic implementation).
     2. Increment the top pointer.
     3. Insert the element at the top index.
   - **Complexity**: \(O(1)\).

##### **Pop Operation**
   - Remove the top element from the stack.
   - **Steps**:
     1. Check if the stack is empty.
     2. Retrieve the top element.
     3. Decrement the top pointer.
     4. Return the element.
   - **Complexity**: \(O(1)\).

##### **Peek/Top Operation**
   - Retrieve the top element without removing it.
   - **Steps**:
     1. Check if the stack is empty.
     2. Return the element at the top pointer.
   - **Complexity**: \(O(1)\).

##### **IsEmpty Operation**
   - Check if the stack has any elements.
   - **Steps**:
     1. Compare if the top pointer is -1 (or null).
   - **Complexity**: \(O(1)\).

##### **IsFull Operation** (Optional)
   - Check if the stack is full (applicable for fixed-size stacks).
   - **Steps**:
     1. Compare if the top pointer equals the maximum capacity.
   - **Complexity**: \(O(1)\).

---

### **3. Implementation**

#### **Using Array**
```python
class Stack:
    def __init__(self, size):
        self.size = size
        self.stack = [None] * size
        self.top = -1

    def push(self, value):
        if self.top == self.size - 1:
            print("Stack Overflow")
        else:
            self.top += 1
            self.stack[self.top] = value

    def pop(self):
        if self.top == -1:
            print("Stack Underflow")
            return None
        else:
            value = self.stack[self.top]
            self.top -= 1
            return value

    def peek(self):
        if self.top == -1:
            print("Stack is empty")
            return None
        return self.stack[self.top]

    def is_empty(self):
        return self.top == -1

# Example usage
s = Stack(5)
s.push(10)
s.push(20)
print(s.peek())  # Output: 20
print(s.pop())   # Output: 20
print(s.is_empty())  # Output: False
```

#### **Using Linked List**
```python
class Node:
    def __init__(self, value):
        self.value = value
        self.next = None

class Stack:
    def __init__(self):
        self.top = None

    def push(self, value):
        new_node = Node(value)
        new_node.next = self.top
        self.top = new_node

    def pop(self):
        if self.top is None:
            print("Stack Underflow")
            return None
        value = self.top.value
        self.top = self.top.next
        return value

    def peek(self):
        if self.top is None:
            print("Stack is empty")
            return None
        return self.top.value

    def is_empty(self):
        return self.top is None

# Example usage
s = Stack()
s.push(10)
s.push(20)
print(s.peek())  # Output: 20
print(s.pop())   # Output: 20
print(s.is_empty())  # Output: False
```

---

### **4. Stack Applications in DSA**

#### **a. Balancing Parentheses**
- Use a stack to match opening and closing brackets.
- Push opening brackets onto the stack.
- Pop and match with closing brackets.

#### **b. Reverse a String**
- Push each character of the string onto the stack.
- Pop characters to create a reversed string.

#### **c. Evaluate Postfix Expressions**
- Scan the postfix expression.
- Push numbers onto the stack.
- When an operator is encountered, pop two numbers, perform the operation, and push the result.

#### **d. Implement Undo/Redo**
- Use two stacks:
  - One for the current state.
  - One for the undo state.

---

### **5. Complexity Analysis**
| Operation | Time Complexity | Space Complexity |
|-----------|-----------------|------------------|
| Push      | \(O(1)\)         | \(O(n)\) (array or linked list) |
| Pop       | \(O(1)\)         | \(O(n)\)                      |
| Peek      | \(O(1)\)         | \(O(n)\)                      |
| IsEmpty   | \(O(1)\)         | \(O(1)\)                      |

---

### **6. Key Takeaways**
- **Stack is versatile** in solving recursive problems and handling expressions.
- **Implementation** can vary based on constraints (fixed or dynamic size).
- **Efficiency**: Most stack operations run in \(O(1)\) time.

Would you like help with more advanced stack algorithms or their applications?