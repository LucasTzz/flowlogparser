# Flow Log Parser
## Overview
This Java project parses a file containing flow log data and maps each row to a tag based on a lookup table, and writes 
tag counts and port/protocol combination counts to output.

It has 3 classes:

- **LookupTable**: loads (port/protocol, tag) mapping from a file and provides get interface
- **FlowLogParser**: parses flow log records based on lookup table, then writes results to output
- **Main**: entry point of the program, uses LookupTable and FlowLogParser to finish the whole process

## Assumptions
1. Each flow log record has 14 fields, which are delimited by space. For example:   
`2 123456789010 eni-1235b8ca123456789 172.31.16.139 172.31.16.21 20641 22 6 20 4249 1418530010 1418530070 ACCEPT OK
`
2. The dstPort is 7th field, protocol is 8th field.
3. The flow log records being parsed contain 4 kinds of protocols: ICMP, IPv4, TCP, UDP.  
See the protocol numbers [here](https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml)

## Compile and Run
### Compile
In terminal, cd to the project root directory, then run
```shell
javac -d build *.java
```
### Run
```shell
java -cp build Main <lookup_path> <flow_log_path> <output_path>
```
<lookup_path> is lookup table file path, <flow_log_path> is flow log file path, <output_path> is the output file path

## Tests
1. Invalid file paths 
2. Wrong file formats, including wrong number of fields and extra whitespaces.
3. More than one port, protocol combinations map to a tag
4. Flow logs have port, protocol combinations that don't exist in lookup table

## Analysis
1. Since the flow log file size can be up to 10 MB, the program reads one line and processes one line to save memories. 
It's also ok to store the file and run the program on a local machine because it's not too large.
2. Since the lookup file can have up to 10000 mappings, it's ok to store all mappings in a Hashmap. And Hashmap has its
own optimized implementation, like using red-black trees when there are many hash conflicts, so the performance wouldn't 
be affected much.
3. Assume there are N flow log records, M lookup mappings(N >> M) and P different port/protocol combinations, then the time complexity is O(N), the space complexity 
is O(2M + P).
4. The program also adds an exception-handling mechanism at the top level of class structure.