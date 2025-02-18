# Cache Simulator with LRU and MRU Replacement Policy

class CacheSimulator:
    def __init__(self, cache_size, block_size, associativity):
        self.cache_size = cache_size  # in bytes
        self.block_size = block_size  # in bytes
        self.associativity = associativity  # number of sets

        # Cache parameters
        self.num_blocks = self.cache_size // self.block_size  # number of cache blocks
        self.num_sets = self.num_blocks // self.associativity  # number of sets
        self.cache = {i: [] for i in range(self.num_sets)}  # Cache initialization

        self.hits = 0
        self.misses = 0
        self.cache_accesses = 0

    def access_cache(self, address, replacement_policy):
        """
        Simulate accessing cache with LRU or MRU policy
        address - memory address being accessed
        replacement_policy - 'LRU' or 'MRU' for replacement strategy
        """
        self.cache_accesses += 1

        set_index = address % self.num_sets  # Calculate the set index
        block_tag = address // self.num_sets  # Use the block address (tag)

        # Check if address is in cache (hit or miss)
        set_cache = self.cache[set_index]
        
        if block_tag in set_cache:
            self.hits += 1
            if replacement_policy == 'LRU':
                # Move the accessed block to the most recent position (end)
                set_cache.remove(block_tag)
                set_cache.append(block_tag)
            elif replacement_policy == 'MRU':
                # Move the accessed block to the front (most recent)
                set_cache.remove(block_tag)
                set_cache.insert(0, block_tag)
            return "Hit"
        
        else:
            self.misses += 1
            if len(set_cache) >= self.associativity:
                # Cache is full, evict according to the replacement policy
                if replacement_policy == 'LRU':
                    evicted = set_cache.pop(0)  # Remove least recently used
                elif replacement_policy == 'MRU':
                    evicted = set_cache.pop()  # Remove most recently used
                
                print(f"Evicted Block: {evicted}")
            
            # Add the block to the set (either LRU or MRU)
            set_cache.append(block_tag)
            return "Miss"

    def get_miss_rate(self):
        return self.misses / self.cache_accesses * 100  # Miss rate as percentage

    def print_cache(self):
        print("Cache contents:")
        for i in range(self.num_sets):
            print(f"Set {i}: {self.cache[i]}")

# Simulate Cache access
address_sequence = [0, 2, 4, 6, 8, 10, 12, 14, 16, 0]  # given address sequence
cache_simulator_LRU = CacheSimulator(cache_size=64, block_size=32, associativity=2)
cache_simulator_MRU = CacheSimulator(cache_size=64, block_size=32, associativity=2)

print("Simulating LRU replacement policy:")
for address in address_sequence:
    print(f"Accessing address {address}: {cache_simulator_LRU.access_cache(address, 'LRU')}")
print(f"LRU Miss Rate: {cache_simulator_LRU.get_miss_rate()}%")

print("\nSimulating MRU replacement policy:")
# for address in address_sequence:
#     print(f"Accessing address {address}: {cache_simulator_MRU.access_cache(address, 'MRU')}")
# print(f"MRU Miss Rate: {cache_simulator_MRU.get_miss_rate()}%")
