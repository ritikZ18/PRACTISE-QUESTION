'''
1. Deine a function 
2. create list 
3. initiate counter 
4. check the i and i + 1 
5. perform checks for equality , greater and less than
6. swap the element function 
7. return swapper values in the new /list/array
8. optimise the code 

'''


def BubbleSort(data_list):       #dont use the List thing in pyton nameing issue
    for i in range(1, len(data_list)):
        for j in range(len(data_list) - i):
            if data_list[j] > data_list[j+1]:
                data_list[j], data_list[j+1] = data_list[j+1], data_list[j]
                
                
l1 = [21,42,53,-2]
BubbleSort(l1)
print('Bubble Sort : ' ,l1)