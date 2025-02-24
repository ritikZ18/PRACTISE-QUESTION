'''
1. define node class, left, item, right
2. define class BST , and __init__ to hold the root node ref
    2.a : create insert method to store/add new item 
    2.b : search  method to traverse tree and find element or return NONE
    2.c : inoreder
    2.c : preorder
    2.c : postorder

'''


class Node:
    def __init__(self, left=None, item=None, right=None):
        self.item = item
        self.left = left 
        self.right = right 
        
class BST: 
    def __inti__(self):
        self.root = None
        
        # rinsert does the operation, insert just makes tree
    def insert(self, data):
        # we create method to call other function to make tree off trees['rinsert']
        self.root = self.rinsert(self.root, data) #will be called recursively 
         
        #  performs operations, left and right
    def rinsert(self, root , data) : #root --> point subtree root, after first recursion 
        if root is None :
            return Node(data) #new node as leaf node, so no left and right
        
        if data < root.item() : 
            #left subtree
            root.left = self.rinsert(root.left, data)
        elif data > root.item():
            root.right = self.rinsert(root.right, data)
        # elif data == root.item():
        return root
    
    
        