package app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SummaryGenerator {
    public static void main(String[] args) {
        generateSummaryCSV();
    }

    public static void generateSummaryCSV() {
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            System.err.println("❌ Папка 'output' не найдена.");
            return;
        }

        File[] resultFiles = outputDir.listFiles((dir, name) -> name.endsWith("_output.json"));
        if (resultFiles == null || resultFiles.length == 0) {
            System.err.println("❌ В папке 'output' нет файлов результатов.");
            return;
        }

        Arrays.sort(resultFiles);

        String csvPath = "output/summary.csv";
        try (FileWriter writer = new FileWriter(csvPath)) {
            // Заголовок
            writer.write("Dataset,Vertices,Edges,Prim Cost,Kruskal Cost,Prim Time (ms),Kruskal Time (ms),Prim Ops,Kruskal Ops\n");

            ObjectMapper mapper = new ObjectMapper();

            for (File file : resultFiles) {
                String fileName = file.getName();
                String datasetName = fileName.substring(0, fileName.indexOf("_output.json"));

                JsonNode root = mapper.readTree(file);

                int vertices = root.get("vertices").asInt();
                int edges = root.get("edges").asInt();

                JsonNode prim = root.get("Prim");
                JsonNode kruskal = root.get("Kruskal");

                double primCost = prim.get("totalCost").asDouble();
                double kruskalCost = kruskal.get("totalCost").asDouble();

                long primTime = prim.get("executionTimeMs").asLong();
                long kruskalTime = kruskal.get("executionTimeMs").asLong();

                int primOps = prim.get("operationCount").asInt();
                int kruskalOps = kruskal.get("operationCount").asInt();

                writer.write(String.format(
                        Locale.US,
                        "%s,%d,%d,%.2f,%.2f,%d,%d,%d,%d\n",
                        datasetName,
                        vertices,
                        edges,
                        primCost,
                        kruskalCost,
                        primTime,
                        kruskalTime,
                        primOps,
                        kruskalOps
                ));
            }

            System.out.println("✅ Файл summary.csv успешно создан: " + csvPath);

        } catch (IOException e) {
            System.err.println("❌ Ошибка при создании summary.csv: " + e.getMessage());
            e.printStackTrace();
        }
    }
}