// had to rework this 3-4 times because of changing formats
import java.io.*;
import java.util.*;

public class FileHandler {
    public static void loadApplicants(String filename) throws IOException 
    {
        List<List<String>> rows = readCsv(filename);
        DataStore.loadApplicantsFromData(rows);
    }
    //now loading flats too
    public static void loadFlats(String filename) throws IOException 
    {
        List<List<String>> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(Arrays.asList(line.split(",")));
            }
        }
    
        for (List<String> row : data) {
            if (row.size() >= 4) {
                String projectId = row.get(0);
                String flatId = row.get(1);
                String type = row.get(2);
                boolean isAvailable = Boolean.parseBoolean(row.get(3));
    
                BTOProject proj = DataStore.getProjectById(projectId);
                if (proj != null) {
                    Flat f = new Flat(flatId, type);
                    if (!isAvailable) f.bookFlat("NA", flatId);
                    proj.getFlats().add(f);
                }
            }
        }
    }
    public static void saveApplicants(String filename) throws IOException 
    {
        List<List<String>> data = DataStore.getApplicantDataForWrite();
        writeCsv(filename, data);
    }

    public static void loadOfficers(String filename) throws IOException 
    {
        List<List<String>> rows = readCsv(filename);
        DataStore.loadOfficersFromData(rows);
    }

    public static void saveOfficers(String filename) throws IOException 
    {
        List<List<String>> data = DataStore.getOfficerDataForWrite();
        writeCsv(filename, data);
    }

    public static void loadManagers(String filename) throws IOException 
    {
        List<List<String>> rows = readCsv(filename);
        DataStore.loadManagersFromData(rows);
    }

    public static void saveManagers(String filename) throws IOException 
    {
        List<List<String>> data = DataStore.getManagerDataForWrite();
        writeCsv(filename, data);
    }

    public static void loadProjects(String filename) throws IOException 
    {
        List<List<String>> rows = readCsv(filename);
        DataStore.loadProjectsFromData(rows);
    }

    public static void saveProjects(String filename) throws IOException 
    {
        List<List<String>> data = DataStore.getProjectDataForWrite();
        writeCsv(filename, data);
    }

    public static void loadApplications(String filename) throws IOException 
    {
        List<List<String>> rows = readCsv(filename);
        DataStore.loadApplicationsFromData(rows);
    }

    public static void saveApplications(String filename) throws IOException 
    {
        List<List<String>> data = DataStore.getApplicationDataForWrite();
        writeCsv(filename, data);
    }

    public static void loadEnquiries(String filename) throws IOException 
    {
        List<List<String>> rows = readCsv(filename);
        DataStore.loadEnquiriesFromData(rows);
    }

    public static void saveEnquiries(String filename) throws IOException 
    {
        List<List<String>> data = DataStore.getEnquiryDataForWrite();
        writeCsv(filename, data);
    }

    private static List<List<String>> readCsv(String filename) throws IOException 
    {
        List<List<String>> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(Arrays.asList(line.split("~")));
            }
        }
        return data;
    }

    private static void writeCsv(String filename, List<List<String>> data) throws IOException 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (List<String> row : data) {
                writer.write(String.join("~", row));
                writer.newLine();
            }
        }
    }
    // saving flats now
    public static void saveFlats(String filename) throws IOException {
        List<List<String>> data = DataStore.getAllFlatDataForWrite();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (List<String> row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
    }

}

