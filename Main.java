public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Main <lookup_path> <flow_log_path> <output_path>");
            return;
        }
        String lookup_path = args[0], flow_log_path = args[1], output_path = args[2];
        try {
            System.out.println("Loading lookup table");
            LookupTable lookupTable = new LookupTable();
            lookupTable.loadFromFile(lookup_path);
            System.out.println("Parsing flow log file");
            FlowLogParser flowLogParser = new FlowLogParser(lookupTable);
            flowLogParser.parse(flow_log_path);
            System.out.println("Writing results to file");
            flowLogParser.writeResultsToFile(output_path);
        } catch (Exception e) {
            System.out.println("Error running flow log parser: " + e.getMessage());
            return;
        }
        System.out.println("flow log parse finished");
    }
}
