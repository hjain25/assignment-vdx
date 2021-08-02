# Timed Eviction Cache i.e. ttl cache 


The TTL (Time To Live) for the cache items are ~10 seconds.

– The cache uses generic keys and values

– TTL is configurable at cache level

– Expired Data writers are extendable

– Works on eager and get time eviction policy. In case gets is being done at key is expired it will also check and evict the key.

– Average method averages out the values of non expired keys in cache but I'd like to discuss the significance of this in meeting. 


To build, please use mvn clean install
To run and test, just execute two basic test cases in TimedCacheTest class