package app;

import algorithm.KruskalMST;
import algorithm.PrimMST;
import model.Graph;
import util.GraphLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import model.Edge;

public class Main {
    public static void main(String[] args) {
        File inputDir = new File("input");
        File outputDir = new File("output");
        outputDir.mkdirs();

        if (!inputDir.exists() || !inputDir.isDirectory()) {
            System.err.println("❌ Папка 'input' не найдена!");
            return;
        }

        File[] inputFiles = inputDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (inputFiles == null || inputFiles.length == 0) {
            System.err.println("❌ В папке 'input' нет JSON-файлов.");
            return;
        }

        Arrays.sort(inputFiles);

        for (File inputFile : inputFiles) {
            String inputFileName = inputFile.getName();
            String baseName = inputFileName.substring(0, inputFileName.lastIndexOf('.'));
            String outputPath = "output/" + baseName + "_output.json";

            System.out.println("⚙️  Обработка: " + inputFileName);

            try {
                Graph graph = GraphLoader.loadGraphFromJson(inputFile.getAbsolutePath());
                int V = graph.getV();
                int E = graph.getEdges().size();

                PrimMST prim = new PrimMST(graph);
                KruskalMST kruskal = new KruskalMST(graph);

                if (Math.abs(prim.getTotalWeight() - kruskal.getTotalWeight()) > 1e-6) {
                    System.err.println("⚠️  ВНИМАНИЕ: стоимость MST различается для " + inputFileName);
                }

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode result = mapper.createObjectNode();
                result.put("vertices", V);
                result.put("edges", E);

                ObjectNode primNode = buildAlgorithmResult(mapper, prim);
                result.set("Prim", primNode);

                ObjectNode kruskalNode = buildAlgorithmResult(mapper, kruskal);
                result.set("Kruskal", kruskalNode);

                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(new File(outputPath), result);

                System.out.println("✅ Успешно сохранено: " + outputPath);

            } catch (IOException e) {
                System.err.println("❌ Ошибка ввода/вывода для " + inputFileName + ": " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Ошибка данных в " + inputFileName + ": " + e.getMessage());
            } catch (Exception e) {
                System.err.println("❌ Неожиданная ошибка при обработке " + inputFileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("\n🎉 Все файлы обработаны. Результаты сохранены в папку 'output/'.");
    }

    private static ObjectNode buildAlgorithmResult(ObjectMapper mapper, Object algorithm) {
        ObjectNode node = mapper.createObjectNode();
        double totalCost = 0;
        long time = 0;
        int ops = 0;
        List<Edge> edges = null;

        if (algorithm instanceof PrimMST prim) {
            totalCost = prim.getTotalWeight();
            time = prim.getExecutionTimeMs();
            ops = prim.getOperationCount();
            edges = prim.getMstEdges();
        } else if (algorithm instanceof KruskalMST kruskal) {
            totalCost = kruskal.getTotalWeight();
            time = kruskal.getExecutionTimeMs();
            ops = kruskal.getOperationCount();
            edges = kruskal.getMstEdges();
        }

        node.put("totalCost", totalCost);
        node.put("executionTimeMs", time);
        node.put("operationCount", ops);

        ArrayNode edgeArray = mapper.createArrayNode();
        for (Edge e : edges) {
            ObjectNode edge = mapper.createObjectNode();
            edge.put("u", e.getU());
            edge.put("v", e.getV());
            edge.put("weight", e.getWeight());
            edgeArray.add(edge);
        }
        node.set("mstEdges", edgeArray);

        return node;
    }
}