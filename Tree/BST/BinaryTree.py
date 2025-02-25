"""
1. define node class, left, item, right
2. define class BST , and __init__ to hold the root node ref
    2.a : create insert method to store/add new item 
    2.b : search  method to traverse tree and find element or return NONE
    2.c : inoreder
    2.c : preorder
    2.c : postorder

"""


class Node:
    def __init__(self, item, left=None, right=None):
        self.item = item
        self.left = left
        self.right = right


class BST:
    def __init__(self):
        self.root = None
        self.item_count = 0

        # rinsert does the operation, insert just makes tree

    def insert(self, data):
        # we create method to call other function to make tree off trees['rinsert']
        self.root = self.rinsert(self.root, data)  # will be called recursively

        #  performs operations, left and right

    def rinsert(self, root, data):  # root --> point subtree root, after first recursion
        if root is None:
            return Node(data)  # new node as leaf node, so no left and right

        if data < root.item:  # item is attribute not method, avoid  `item()`
            # left subtree
            root.left = self.rinsert(root.left, data)

        elif data > root.item:
            root.right = self.rinsert(root.right, data)
        # elif data == root.item():

        return root

    def search(self, data):
        return self.rsearch(self.root, data)
        # return self.root

    def rsearch(self, root, data):  # recursive function to atraverse and search, delete, getData, etc
        if (root is None) or (root.item == data):
            return root

        if data < root.item:
            return self.rsearch(root.left, data)
        elif data > root.item:
            return self.rsearch(root.right, data)

    def inorder(self):
        result = []  # empty list to store the traverse data
        self.rinorder(self.root, result)
        return result

    def rinorder(self, root, result):
        if root:  # continue until finds none
            self.rinorder(root.left, result)
            result.append(root.item)
            self.rinorder(root.right, result)

    def preorder(self):
        result = []  # empty list to store the traverse data
        self.rpreorder(self.root, result)
        return result

    def rpreorder(self, root, result):
        if root:  # continue until finds none
            result.append(root.item)  # changes here
            self.rpreorder(root.left, result)
            self.rpreorder(root.right, result)

    def postorder(self):
        result = []  # empty list to store the traverse data
        self.rpostorder(self.root, result)
        return result

    def rpostorder(self, root, result):
        if root:  # continue until finds none
            self.rpostorder(root.left, result)
            self.rpostorder(root.right, result)
            result.append(root.item)  # changes here

    # deletion functionality
    """
    1. find min value item node
    2. find max value item node
    3. delete method to delete node  --> we will replace the parent with predecesor/succesor
    4. size method to return tree size
    """

    def minValue(self, temp):  # temp pointing the root node
        current = temp
        while current.left is not None:
            current = current.left
        return current.item

    def maxValue(self, temp):
        current = temp
        while current.right is not None:
            current = current.right
        return current.item

    def delete(self, data):
        self.root = self.rdelete(self.root, data)

    def rdelete(self, root, data):
        if root is None:
            return root

        if data < root.item:
            root.left = self.rdelete(root.left, data)

        elif data > root.item:
            root.right = self.rdelete(root.right, data)

        else:
            if root.left is None:  # single child
                return root.right

            elif root.right is None:  # single child
                return root.left

            min_value = self.minValue(root.right)  # successor
            root.item = min_value
            root.right = self.rdelete(root.right, min_value)

        return root

    def size(self):
            return len(self.inorder())

    # will test the above cases
    """
    1. create instance of BST
    2. create test data for bst 
    3. Test the methods, 
        insert
        delete
        search
        inorder
        preorder 
        postorder
        size
    4. 
    """


def main():

    bst = BST()

    test_data = [10, 40, 45, 85, 90, 52, 98, 32, 54, 72, 29, 62, 99]

    # insert the test data
    for item in test_data:
        bst.insert(item)

    print("Inorder Traversal:", bst.inorder())  # Fix
    print("Preorder Traversal:", bst.preorder())  # Fix
    print("Postorder Traversal:", bst.postorder())  # Fix


    # Test search
    search_val = 45
    found_node = bst.search(search_val)
    print(f"Search for {search_val}:", "Found" if found_node else "Not found")

    search_val = 100
    found_node = bst.search(search_val)
    print(f"Search for {search_val}:", "Found" if found_node else "Not found")

    # Test delete
    delete_val = 85
    bst.delete(delete_val)
    print(f"Inorder Traversal after deleting {delete_val}:", bst.inorder())

    # Test size
    print("Size of BST:", bst.size())


if __name__ == "__main__":
    main()
