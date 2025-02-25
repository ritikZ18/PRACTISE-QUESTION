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
    
    def search(self,data):
        self.root = self.rsearch(self.root, data)
    
    def rsearch(self, root, data):
        if (root is None )or (root.item == data) : 
            return root
        
        if data < root.item : 
            return self.rsearch(root.left, data)
        elif data > root.item : 
            return self.rsearch(root.right, data)
    
    def inorder(self):
        result = [] #empty list to store the traverse data 
        self.rinorder(self.root, result)
        
    def rindorder(self, root, result) :
        if root : #continue until finds none
            self.inorder(root.left, result)
            result.append(root.item)
            self.inorder(root.right, result)
            
    def preorder(self):
        result = [] #empty list to store the traverse data 
        self.rpreorder(self.root, result)
        
    def rpredorder(self, root, result) :
        if root : #continue until finds none
            result.append(root.item)    #changes here
            self.preorder(root.left, result)
            self.preorder(root.right, result)
    
    def postorder(self):
        result = [] #empty list to store the traverse data 
        self.rpostorder(self.root, result)
        
    def rpostorder(self, root, result) :
        if root : #continue until finds none
            self.postorder(root.left, result)
            self.postorder(root.right, result)
            result.append(root.item) #changes here
            
    # deletion functionality 
    '''
    1. find min value item node
    2. find max value item node
    3. delete method to delete node 
    4. size method to return tree size
    '''
    
    