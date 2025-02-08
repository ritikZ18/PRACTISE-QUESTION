'''
1. find the min elements 
2. compare with the leftmost elements, 
3. leftmost i++ (every iteration), wehere i = 0 
4. continue till arr[i] == leftmost
'''
def selectionSort(data_list):
    n = len(data_list)
    for i in range(n):
        min_item_index = i 

        for j in range(i+1, n):
            if ( data_list[j] < data_list[min_item_index]):
                min_item_index =  j                         #leftmost 
                
        data_list[i] , data_list[min_item_index] = data_list[min_item_index] , data_list[i]

l1 = [21,42,53,-2]
selectionSort(l1)
print('Selection Sort : ' , l1)
                
            
         
        