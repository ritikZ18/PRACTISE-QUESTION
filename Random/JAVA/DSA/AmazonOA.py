"""
    Given an encoded configuration string of the form:
        'ddddXXXXXXXXXX|ddddXXXXXXXXXX|...'
    Where:
      - dddd is a 4‑digit ordinal index from "0001" up to "9999" (leading zeros).
      - XXXXXXXXXX is exactly 10 alphanumeric characters.
      - '|' separates multiple configuration segments.

    The rules are:
      1. All segments must be separated by '|'.
      2. No skipping or repeating indexes. If there are N segments, 
         the set of ordinal indexes must be exactly {1, 2, ..., N}.
      3. Ordinal index "0000" is invalid.
      4. Each configuration value must be exactly 10 alphanumeric characters.
      5. If anything is invalid, return ["Invalid configuration"].
      6. If valid, return the configuration values in ascending order 
         by their ordinal index.
"""

"""
1. validate and reorder the encoded strn=ing
2. validate all segment except some format
3. ensure the indexes are consecutive ( no skip or duplocation)
4. reorder the configuration vlaues based on on thier index
5. return ordeed value if value or Invalud config

"""


def ordered_configuration(configuration: str):

    # get the part of the config divided by | in input

    parts = configuration.split("|")

    #  ['0002f7c22e7904', '000176a3a4d214', '000305d29f4a4b']

    if not parts:
        return ["Invalid COPnfiguration"]

    config_map = {}  # will sotre the index for each valid string

    for part in parts:
        # check for invalid config, if part < 14 or > 14

        if len(part) != 14:
            return ["Invalid COnfigutration"]

        # now extract the ordinal index and config value

        ordinal_index = part[:4]  # 0 -> 3  [ first fours char]  , is ordinal string
        config_value = part[4:]  # 3 -> end [ extracts last 10 chars, after first 4]

        # checking for otdinality constraints
        if (not ordinal_index.isdigit()) or (ordinal_index == "0000"):
            return ["INVALID CONFIGURATION"]

        # convert to integer
        ordinal_int = int(ordinal_index)
        
        # # checking for configuration constraints, is all 10 or must be alphanumeric( no special symbolls)
        if  len(config_value) != 10  or not config_value.isalnum():
            return ["INVALID CONFIGURATION"]
        
        # checking for duplicate ordinal 
        if ordinal_int in config_map :
            return ["INVALID CONFIGURATION"]
        config_map[ordinal_int] = config_value  #storing the config value with integer index as key in dict

    n = len(parts)
    required_indexes  = set(range(1, n+1))
    if set(config_map.keys()) != required_indexes:
        return ["INVALID CONFIGURATION"]

    result = [config_map[i] for i in sorted(config_map.keys())]
    return result 

        
        
        
        
        # TESTING  

if __name__ == "__main__":
    # A valid example (similar to the "happy path" in the prompt)
    # indexes = 2, 1, 3 but no skipping or repeats => reorder as #1, #2, #3
    cfg_str = "0002f7c22e7904|000176a3a4d214|000305d29f4a4b"
    print(ordered_configuration(cfg_str))
    # Expected: ['76a3a4d214', 'f7c22e7904', '05d29f4a4b']

    # An invalid example: duplicates "0002"
    cfg_str_invalid = "0002AAAAABBBBB|0002CCCCCDDDDD|0003EEEEEFFFFF"
    print(ordered_configuration(cfg_str_invalid))
    # Expected: ['Invalid configuration']

    # An invalid example: skip index 0002 is missing (we only have 0001, 0003)
    cfg_str_skip = "0001A1A1A1A1A1|0003B2B2B2B2B2"
    print(ordered_configuration(cfg_str_skip))
    # Expected: ['Invalid configuration']

    # Edge case: Single segment (must be "0001" exactly)
    cfg_str_single = "0001QWERTY1234"
    print(ordered_configuration(cfg_str_single))
    # Expected: ['QWERTY1234']

    # Edge case: ordinal "0000" is invalid
    cfg_str_zero = "0000AAAAABBBBB"
    print(ordered_configuration(cfg_str_zero))
    # Expected: ['Invalid configuration']
        
            
