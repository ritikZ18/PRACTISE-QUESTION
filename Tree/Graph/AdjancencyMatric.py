'''
1. create class Graph to implement adjancecny matrix 
2. in Graph class, __init__ to init the vertex_count and adj_matrix
3. insert_edge
4. remove edge
5. has_edge() 
6. print_adj_matrix
'''

class Graph : 
    
    def __init__(self, v_count):    # v_count --> number of vertices 
        self.vertex_count = v_count
        self.adj_matrix = [[0]*v_count for e in range(v_count)]   
        
    def add_edge(self,start_vertex, end_vertex, weight=1) :
        

        
        