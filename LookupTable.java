import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LookupTable {

    private final String UNTAGGED = "untagged";
    private final Map<String, String> lookupTable = new HashMap<>();

    public void loadFromFile(String filePath) throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        reader.readLine();
        while ((line = reader.readLine()) != null)
        {
            String[] fields = line.split(",");
            if (fields.length == 3)
            {
                String dstPort = fields[0].trim().toLowerCase();
                String protocol = fields[1].trim().toLowerCase();
                String tag = fields[2].trim();
                lookupTable.put(dstPort + "," + protocol, tag);
            } else {
                System.out.println("wrong format, ignoring line: " + line);
            }
        }
    }

    public String getTag(String dstPort, String protocol) {
        String comb = dstPort + "," + protocol;
        return lookupTable.getOrDefault(comb, UNTAGGED);
    }
}
