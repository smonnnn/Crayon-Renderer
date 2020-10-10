package me.smonn.crayon.renderer.util;

import me.smonn.crayon.renderer.RenderData;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static void loadObjModel(String fileName, RenderData dataCollector, Matrix4f[] transforms, int renderCount){
        FileReader fr = null;
        try {
            fr = new FileReader("res/" + fileName + ".obj");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find file: " + fileName);
            e.printStackTrace();
        }
            BufferedReader reader = new BufferedReader(fr);
            String l;
            List<Vector3f> vertices = new ArrayList<>();
            List<Vector2f> texUVs = new ArrayList<>();
            List<Vector3f> normals = new ArrayList<>();
            List<Integer> indices = new ArrayList<>();
            float[] verticesArray = null;
            float[] texUVsArray = null;
            float[] normalsArray = null;
            int[] indicesArray = null;

            try {
                while(true){
                    l = reader.readLine();
                    String[] currentLine = l.split(" ");
                    if(l.startsWith("v ")){
                        Vector3f vec = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                        vertices.add(vec);

                    } else if(l.startsWith("vt ")){
                        Vector2f vec = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                        texUVs.add(vec);

                    } else if(l.startsWith("vn ")){
                        Vector3f vec = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                        normals.add(vec);

                    } else if(l.startsWith("f ")){
                        texUVsArray = new float[vertices.size() * 2];
                        normalsArray = new float[vertices.size() * 3];
                        break;
                    }
                }

                while(l != null){
                    if(!l.startsWith("f ")){
                        l = reader.readLine();
                        continue;
                    }
                    String[] currentline = l.split(" ");
                    String[] vertex1 = currentline[1].split("/");
                    String[] vertex2 = currentline[2].split("/");
                    String[] vertex3 = currentline[3].split("/");

                    processVertex(vertex1, indices, texUVs, normals, texUVsArray, normalsArray);
                    processVertex(vertex2, indices, texUVs, normals, texUVsArray, normalsArray);
                    processVertex(vertex3, indices, texUVs, normals, texUVsArray, normalsArray);
                    l = reader.readLine();
                }
                reader.close();

            } catch (Exception e){
            e.printStackTrace();
        }
            verticesArray = new float[vertices.size() * 3];
            indicesArray = new int[indices.size()];

            int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++){
            indicesArray[i] = indices.get(i);
        }
        dataCollector.addDrawCall(verticesArray, indicesArray, texUVsArray, normalsArray, transforms, renderCount);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> texUVs, List<Vector3f> normals, float[] texUVsArray, float[] normalsArray){
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

        Vector2f currentTex  = texUVs.get(Integer.parseInt(vertexData[1]) - 1);
        texUVsArray[currentVertexPointer * 2] = currentTex.x;
        texUVsArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;

        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNormal.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }
}
