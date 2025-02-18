class OddEven : 
    
    
    def getTheNumber(self, nums): 
        for i in nums:
            if((i & 1) == 1 ):
                print("Number is Odd : ", i)

            else : 
                print("Number is Even : ", i)
                
nums = [1, 4,3,6,3,63,74,534,63,42,32]
sol = OddEven()
sol.getTheNumber(nums)
                
        