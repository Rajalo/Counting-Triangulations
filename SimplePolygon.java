import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class SimplePolygon {
    ArrayList<Vertex> vertices;
    ArrayList<Edge> edges;
    boolean clockwise;
    int[][] triangulations;
    int[][] coordsArray;

    /**
     * Constructs a SimplePolygon from an array of vertices
     * @param vertices the vertices of the polygon
     */
    public SimplePolygon(Vertex[] vertices)
    {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        Collections.addAll(this.vertices, vertices);
        if (vertices.length==0)
        {
            return;
        }
        for (int i = 0; i < this.vertices.size(); i++)
        {
            edges.add(Edge.polygonalEdge(this.vertices.get(i),this.vertices.get((i+1)%this.vertices.size())));
        }
        Vertex lowest = this.vertices.get(0);
        for (Vertex vertex:this.vertices)
        {
            if (vertex.getY()<lowest.getY()||(vertex.getY()==lowest.getY()&&vertex.getX()<lowest.getX()))
                lowest = vertex;
        }
        clockwise = left(lowest.getCoordsArr(),lowest.getPrev().getCoordsArr(),lowest.getNext().getCoordsArr());
    }
    /**
     * Constructs a SimplePolygon from a list of vertices
     * @param vertices the vertices of the polygon
     */
    public SimplePolygon(ArrayList<Vertex> vertices)
    {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>();
        for (int i = 0; i < this.vertices.size(); i++)
        {
            edges.add(Edge.polygonalEdge(this.vertices.get(i),this.vertices.get((i+1)%this.vertices.size())));
        }
        Vertex lowest = this.vertices.get(0);
        for (Vertex vertex:this.vertices)
        {
            if (vertex.getX()<lowest.getX())
                lowest = vertex;
        }
        clockwise = left(lowest.getCoordsArr(),lowest.getPrev().getCoordsArr(),lowest.getNext().getCoordsArr());
    }

    /**
     * Draws the polygon
     * @param g the graphics object being used to draw
     */
    public void paint(Graphics g)
    {
        g.setColor(new Color(150,160,250));
        g.fillPolygon(getCoordsArray()[0],getCoordsArray()[1],vertices.size());
        g.setColor(Color.BLACK);
        g.drawPolygon(getCoordsArray()[0],getCoordsArray()[1],vertices.size());
        g.setColor(new Color(250,100,100));
        int i = 0;
        for (Vertex vertex : vertices)
        {
            g.fillOval(vertex.getX()-5,vertex.getY()-5,10,10);
            g.drawString(""+i++, vertex.getX()+5, vertex.getY()+5);
        }
    }

    /**
     * Draws a table representing the triangulations left of an edge.
     * @param g the graphics object being used to draw
     */
    public void paintTable(Graphics g)
    {
        int spacing = 30;
        g.drawString("Triangulations left of edge (i,j), 0 if not used in calculation",20,80);
        for (int i = 0; i < vertices.size(); i++)
        {
            g.drawString(""+i ,spacing*(i+2),120);
            g.drawString(""+i ,spacing,120+spacing*(i+1));
        }
        if (triangulations.length!=vertices.size())
            return;
        for (int i = 0; i < vertices.size(); i++)
        {
            for (int j = 0; j < vertices.size(); j++)
            {
                g.drawString(""+triangulations[j][i], spacing*(i+2), 120+spacing*(j+1));
            }
        }
    }

    /**
     * Generates a coordinate array of the vertices compliant with g.drawPolygon()'s specifications
     * @return coordinate array
     */
    public int[][] getCoordsArray() {
        if (coordsArray != null)
            return coordsArray;
        coordsArray = new int[2][vertices.size()];
        int i = 0;
        for (Vertex vertex : vertices)
        {
            coordsArray[0][i] = vertex.getX();
            coordsArray[1][i++] = vertex.getY();
        }
        return coordsArray;
    }

    /**
     * Counts the triangulations of the polygon
     * @return integer number of triangulations of given polygon
     */
    public int triangulations()
    {
        if (vertices.size()==0)
            return 0;
        if (triangulations == null)
            triangulations = new int[vertices.size()][vertices.size()];
        if (clockwise)
        {
            for (Edge edge: edges) {
                edge.invert();
            }
            Collections.reverse(vertices);
            Collections.reverse(edges);
            clockwise = false;
        }
        int i = 0;
        for (; i< edges.size();i++)
        {
            int count = 0;
            for (int j = 0;j< vertices.size();j++)
            {
                if (left(vertices.get(i).getCoordsArr(),vertices.get((i+1)% vertices.size()).getCoordsArr(),vertices.get(j).getCoordsArr()))
                    count++;
            }
            if (count == vertices.size()-2)
                break;
        }
        return triangulationsCounterClockwise(i,(i+1)% vertices.size());
    }

    /**
     * Counts the triangulations to the left of the edge (i,j) for a counterclockwise polygon
     * @param i index of first vertex
     * @param j index of second vertex
     * @return the number of triangulations left of (i,j)
     */
    public int triangulationsCounterClockwise(int i, int j)
    {
        i %= vertices.size();
        j %= vertices.size();
        if (i == j)
            return 0;
        if (triangulations[i][j]!=0)
            return triangulations[i][j];
        if ((j-i+vertices.size())% vertices.size()== vertices.size()-1)
        {
            return triangulations[i][j]=1;
        }
        int count = 0;
        for (int k = 0; k < vertices.size();k++)
        {
            if (left(vertices.get(i).getCoordsArr(),vertices.get(j).getCoordsArr(),vertices.get(k).getCoordsArr())
                &&diagonalOrEdge(vertices.get(i),vertices.get(k))&&diagonalOrEdge(vertices.get(k),vertices.get(j)))
                count += triangulationsCounterClockwise(i,k)*triangulationsCounterClockwise(k,j);
        }
        if (count == 0)
            return triangulations[i][j]=1;
        return triangulations[i][j]=count;
    }

    /**
     * Checks if two vertices share either a diagonal or an edge
     * @param v0 first vertex
     * @param v1 second vertex
     * @return true if the vertices share either a diagonal or an edge
     */
    public boolean diagonalOrEdge(Vertex v0, Vertex v1)
    {
        if (diagonal(v0,v1))
            return true;
        for (Edge edge: edges)
        {
            if (edge.contains(v0)&&edge.contains(v1))
                return true;
        }
        return false;
    }

    /**
     * Determines if two vertices have a diagonal
     * @param v0 first vertex
     * @param v1 second vertex
     * @return true if they have a diagonal
     */
    public boolean diagonal(Vertex v0, Vertex v1)
    {
        return inCone(v0,v1)&&inCone(v1,v0)&&diagonalie(v0,v1);
    }

    /**
     * Determines if two vertices can see each other
     * @param v0 first vertex
     * @param v1 second vertex
     * @return true if no edges overlap with line between vertices, false otherwise.
     */
    public boolean diagonalie(Vertex v0, Vertex v1)
    {
        for (Edge edge : edges)
        {
            if (edge.contains(v0)||edge.contains(v1))
            {
                continue;
            }
            if (intersectsProp(edge.getStart().getCoordsArr(),edge.getEnd().getCoordsArr(),v0.getCoordsArr(), v1.getCoordsArr()))
            {
                return false;
            }
        }
        return true;
    }
    /**
     * Determines whether a vertex is in the "open cone" of another vertex.
     * @param v0 vertex who's in the cone is analyzed
     * @param v1 vertex who may or may not be in the cone
     * @return true if vertex is in the cone, false otherwise
     */
    public boolean inCone(Vertex v0, Vertex v1)
    {
        Vertex next = vertices.get((vertices.indexOf(v0)+1)% vertices.size());
        Vertex prev = vertices.get((vertices.indexOf(v0)-1+vertices.size())% vertices.size());
        if (leftOn(v0.getCoordsArr(),next.getCoordsArr(),prev.getCoordsArr()))
        {
            return left(v0.getCoordsArr(),v1.getCoordsArr(),prev.getCoordsArr())&&left(v1.getCoordsArr(),v0.getCoordsArr(),next.getCoordsArr());
        }
        return !(leftOn(v0.getCoordsArr(),v1.getCoordsArr(),next.getCoordsArr())&&leftOn(v1.getCoordsArr(),v0.getCoordsArr(),prev.getCoordsArr()));
    }
    
    /**
     * Determines if two line segments intersect
     * @param a first endpoint of first line segment
     * @param b second endpoint of first line segment
     * @param c first endpoint of second line segment
     * @param d first endpoint of second line segment
     * @return true if they intersect, false otherwise
     */
    public static boolean intersectsProp(int[] a, int[] b, int[] c, int[] d)
    {
        if (collinear(a,b,c) && collinear(a,b,d))
        {
            return (between(a,b,c)||between(a,b,d));
        }
        if ((between(a,b,c)||between(a,b,d)||between(c,d,a)||between(c,d,b)))
        {
            return true;
        }
        if (collinear(a,b,c) || collinear(a,b,d) || collinear (c, d, a) || collinear(c,d,b))
        {
            return false;
        }
        return (left(a,b,c) != left(a,b,d)) && (left(c,d,a) != left(c,d,b));
    }
    /**
     * Determines if a point is between two other points (all collinear)
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point between two other points
     * @return true if c is between a and b, false elsewise.
     */
    public static boolean between(int[]a, int[] b, int[] c)
    {
        if (!collinear(a,b,c))
            return false;
        if (a[0] != b[0])
            return ((a[0] <= c[0]) && (c[0] <=b[0])) ||
                    ((a[0] >= c[0]) && (c[0] >= b[0]));
        return ((a[1] <= c[1]) && (c[1] <= b[1])) ||
                ((a[1] >= c[1]) && (c[1] >= b[1]));
    }
    /**
     * Determines if a point is to the left of two other points
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point that may or may not be to the left
     * @return true if c is to the left of a and b, false elsewise.
     */
    public static boolean left(int[]a, int[] b, int[] c) {
        return crossProduct(a,b,a,c) > 0;
    }
    /**
     * Determines if a point is to the left of or inline with two other points
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point that may or may not be to the left
     * @return true if c is to the left of or collinear with a and b, false elsewise.
     */
    public static boolean leftOn(int[]a, int[] b, int[] c) {
        return crossProduct(a,b,a,c) >= 0;
    }
    /**
     * Determines if a point is to the collinear with two other points
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point that may or may not be collinear.
     * @return true if c is to the left of a and b, false elsewise.
     */
    public static boolean collinear(int[]a, int[] b, int[] c) {
        return crossProduct(a,b,a,c) == 0;
    }
    /**
     * Determines signed area of the parallelogram with points a,b,c,d
     * @param a coordinates of point a
     * @param b coordinates of point b
     * @param c coordinates of point c
     * @param d coordinates of point d
     * @return signed area of the parallelogram with points a,b,c,d
     */
    public static int crossProduct(int[] a, int[] b, int[] c, int[]d)
    {
        return (b[0] - a[0]) * (d[1] - c[1]) - (b[1] - a[1]) * (d[0] - c[0]);
    }
}
