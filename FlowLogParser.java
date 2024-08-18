import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FlowLogParser {

    private final Map<String, String> protocolTable = Map.of(
            "1", "ICMP",
            "4", "IPv4",
            "6", "TCP",
            "17", "UDP"
    );
    private final LookupTable lookupTable;
    private final Map<String, Integer> combCount;
    private final Map<String, Integer> tagCount;

    public FlowLogParser(LookupTable lookupTable) {
        this.lookupTable = lookupTable;
        this.combCount = new HashMap<>();
        this.tagCount = new HashMap<>();
    }

    public void parse(String flowLogPath) throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(flowLogPath));
        while ((line = reader.readLine()) != null)
        {
            String[] fields = line.split(" ");
            if (fields.length == 14)
            {
                String dstPort = fields[6].trim().toLowerCase();
                String protocol = protocolTable.getOrDefault(fields[7].trim(), "").toLowerCase();
                String comb = dstPort + "," + protocol;
                combCount.put(comb, combCount.getOrDefault(comb, 0) + 1);
                String tag = lookupTable.getTag(dstPort, protocol);
                tagCount.put(tag, tagCount.getOrDefault(tag, 0) + 1);
            } else {
                System.out.println("wrong format, ignoring line: " + line);
            }
        }
    }

    public void writeResultsToFile(String outputPath) throws IOException {
        File file = new File(outputPath);
        BufferedWriter bf  = new BufferedWriter(new FileWriter(file));

        // write tag counts
        bf.write("Tag Counts:\n");
        bf.write("Tag\t\t\tCount\n");
        for (Map.Entry<String, Integer> entry : tagCount.entrySet()) {
            bf.write(entry.getKey() + "\t\t" + entry.getValue());
            bf.newLine();
        }
        bf.flush();
        bf.newLine();

        // write port/protocol combination counts
        bf.write("Port/Protocol Combination Counts:\n");
        bf.write("Port\tProtocol\tCount\n");
        for (Map.Entry<String, Integer> entry : combCount.entrySet()) {
            String comb = entry.getKey();
            String dstPort = comb.split(",")[0], protocol = comb.split(",")[1];
            bf.write(dstPort + "\t\t" + protocol + "\t\t" + entry.getValue());
            bf.newLine();
        }
        bf.flush();
        bf.close();
    }
}