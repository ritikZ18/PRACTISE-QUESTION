class Solution(object):
    def isAnagram(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        # initial decision as FLAG --> True
        flag = True

        # length of both string checkup
        if len(s) != len(t):
            return False 

        letters = 'abcdefghijklmnopqrstuvwxyz'
        
        for letter in letters : 
        # if the letter in s_count array != letters in t_count array FLAG --> False, count (in-built)
            if s.count(letter)  != t.count(letter) :
                flag = False

        return flag


        