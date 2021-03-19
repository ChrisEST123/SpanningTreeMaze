//Student ID: 1906001

package assignment2;

import java.util.*;

public class Maze {
    int height;
    int width;
    int array[][];
    MatrixGraph graph;

    Maze(int h, int w){
        height = h;
        width = w;
        graph = new MatrixGraph(height * width,Graph.UNDIRECTED_GRAPH);
        int firstVertix = 0;
        int secondVertix = 1;
        int delayedVertix = 0;
        for (int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                double weight = Math.random();
                if(j != width-1){
                    graph.addEdge(firstVertix, secondVertix, weight);
                }
                firstVertix++;
                secondVertix++;
            }
            secondVertix--;
            for(int j = 0; j < width; j++) {
                double weight = Math.random();
                if(delayedVertix == width * height - width){
                    break;
                }
                graph.addEdge(delayedVertix, secondVertix, weight);
                delayedVertix++;
                secondVertix++;
            }
            secondVertix -= (width - 1);
        }
    }

    void print(){
        Random rn = new Random();
        int starterPoint = rn.nextInt(height + 1);
        int endPoint = rn.nextInt(height+1);
        int firstVertix = 0;
        int secondVertix = 1;
        int delayedVertix = 0;
        boolean firstLoop = true;

        for (int i = 0; i < height; i++){
            StringBuilder line = new StringBuilder();
            StringBuilder line2 = new StringBuilder();
            if(!firstLoop){
                secondVertix--;
                for(int j = 0; j < width; j++) {
                    if (graph.isEdge(delayedVertix, secondVertix)) {
                        if(j == width-1){
                            line2.append("|");
                        } else {
                            line2.append("|   ");
                        }
                    } else {
                        line2.append("    ");
                    }
                    delayedVertix++;
                    secondVertix++;
                }
                System.out.println(line2);
                secondVertix -= (width-1);
            }
            for(int j = 0; j < width; j++){
                if(i == starterPoint){
                    if(j == 0){
                        line.append("* ");
                    }
                    else{
                        if(j == width-1){
                            line.append("+");
                        } else {
                            line.append("+ ");
                        }
                    }
                } else if (i == endPoint){
                    if(j == width - 1){
                        line.append("*");
                    } else {
                        if(j == width-1){
                            line.append("+");
                        } else {
                            line.append("+ ");
                        }
                    }
                } else if(i != starterPoint || i != endPoint) {
                    if(j == width-1){
                        line.append("+");
                    } else {
                        line.append("+ ");
                    }
                }
                if(graph.isEdge(firstVertix, secondVertix)){
                    line.append("- ");
                } else{
                    line.append("  ");
                }
                firstVertix++;
                secondVertix++;
            }
            if(firstLoop){
                firstLoop = false;
            }
            System.out.println(line);
        }
    }

    void initializeComponents(){
        array = new int[graph.numVertices()][graph.numVertices()];
        for(int i = 0; i < graph.numVertices(); i++){
            array[i][0] = i;
        }
    }

    void mergeComponents(int x, int y){
        int breaker = 0;
        for(int i = 0; i < array[x].length; i++){
            if(array[x][i] == 0) {
                if (i == 0) {
                    continue;
                } else {
                    breaker = i;
                    break;
                }
            }
        }
        int breaker2 = 0;
        for(int i = 0; i < array[y].length; i++){
            if(array[y][i] == 0) {
                if (i == 0) {
                    continue;
                } else {
                    breaker2 = i;
                    break;
                }
            }
        }

        int[] tempArray = new int[graph.numVertices()];

        tempArray = array[x];

        System.arraycopy(array[y], 0, array[x], breaker, breaker2);

        System.arraycopy(tempArray, 0, array[y], breaker2, breaker);
    }

    void spanningTree(){
        MatrixGraph newGraph = new MatrixGraph(graph.numVertices(), Graph.UNDIRECTED_GRAPH);
        initializeComponents();
        boolean loop = true;
        int counting = 0;
        while (loop == true) {
            boolean innerLoop = false;
            for (int i = 0; i < array.length; i++) {
                double weight = 0;
                int x = 0;
                int y = 0;
                int curX = 0;
                int curY = 0;
                int num = array[i][0];
                int[] firstNeighbours = graph.neighbours(num);
                for (int j = 0; j < firstNeighbours.length; j++) {
                    double versusWeight = 0;
                    int zeroCount = 0;
                    for (int t = 0; t < array[i].length; t++) {
                        int[] lastNeighbours = graph.neighbours(array[0][t]);
                        for (int w = 0; w < lastNeighbours.length; w++) {
                            if (array[i][t] == lastNeighbours[w]) {
                                continue;
                            }
                            if (newGraph.isEdge(array[i][t], lastNeighbours[w])) {
                                continue;
                            }
                            if (array[i][t] == 0) {
                                if (zeroCount > 0) {
                                    break;
                                } else {
                                    zeroCount++;
                                }
                            }
                            if (versusWeight <= 0) {
                                versusWeight = graph.weight(array[i][t], lastNeighbours[w]);
                                curX = array[i][t];
                                curY = lastNeighbours[w];
                            } else if (versusWeight > graph.weight(array[i][t], lastNeighbours[w])) {
                                versusWeight = graph.weight(array[i][t], lastNeighbours[w]);
                                curX = array[i][t];
                                curY = lastNeighbours[w];
                            }
                        }
                    }
                    if (num == firstNeighbours[j]) {
                        if (weight <= 0) {
                            weight = versusWeight;
                            x = curX;
                            y = curY;
                        } else if (weight > versusWeight) {
                            weight = versusWeight;
                            x = curX;
                            y = curY;
                        }
                        continue;
                    }
                    if (newGraph.isEdge(num, firstNeighbours[j])) {
                        if (weight <= 0) {
                            weight = versusWeight;
                            x = curX;
                            y = curY;
                        } else if (weight > versusWeight) {
                            weight = versusWeight;
                            x = curX;
                            y = curY;
                        }
                        continue;
                    }
                    if (weight <= 0) {
                        weight = graph.weight(num, firstNeighbours[j]);
                        x = num;
                        y = firstNeighbours[j];
                    } else if (weight > graph.weight(num, firstNeighbours[j])) {
                        weight = graph.weight(num, firstNeighbours[j]);
                        x = num;
                        y = firstNeighbours[j];
                    }
                    if (versusWeight > 0) {
                        if (weight > versusWeight) {
                            weight = versusWeight;
                            x = curX;
                            y = curY;
                        }
                    }
                }
                if (weight == 0.0) {
                    loop = false;
                }
                if(Double.isNaN(weight)){
                    innerLoop = true;
                }
                if (!newGraph.isEdge(x, y)) {
                    try {
                        mergeComponents(0, y);
                        newGraph.addEdge(x, y, weight);
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
            if(innerLoop == true){
                if(counting > 0){
                    loop = false;
                } else {
                    counting++;
                }
            }
        }
        graph = newGraph;
    }

    public static void main(String[] args) {
        Maze maze = new Maze(10, 10);
        maze.spanningTree();
        maze.print();
    }
}
