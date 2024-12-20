package game.functions.graph;

import java.util.ArrayList;
import java.util.List;

import annotations.Hide;
import game.types.board.BasisType;
import game.types.board.ShapeType;
import game.types.board.SiteType;
import game.util.graph.Graph;
import main.math.MathRoutines;
import other.BaseLudeme;
import other.context.Context;

/**
 * Default implementations of graph functions - override where necessary.
 *
 * @author cambolbro
 */
@Hide
public abstract class BaseGraphFunction extends BaseLudeme implements GraphFunction {
    protected static final double unit = 1.0;

    //-------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;
    protected final double tolerance = 0.001;
    protected BasisType basis = null;
    protected ShapeType shape = null;
    protected int[] dim;

    //-------------------------------------------------------------------------

    /**
     * Only applies to graph with uniform edge lengths.
     *
     * @param vertexList The list of the vertices.
     * @param u          The unit.
     * @param basisIn    The basis.
     * @param shapeIn    The shape.
     * @return Graph generated by this vertex list.
     */
    public static Graph createGraphFromVertexList
    (
            final List<double[]> vertexList,
            final double u,
            final BasisType basisIn,
            final ShapeType shapeIn
    ) {
        // Create edges
        final List<int[]> edgeList = new ArrayList<int[]>();
        for (int aid = 0; aid < vertexList.size(); aid++) {
            final double[] ptA = vertexList.get(aid);

            final double ax = ptA[0];
            final double ay = ptA[1];

            // Check for edges 1 unit away
            for (int bid = aid + 1; bid < vertexList.size(); bid++) {
                final double[] ptB = vertexList.get(bid);
                final double dist = MathRoutines.distance(ptB[0], ptB[1], ax, ay);
                if (Math.abs(dist - u) < 0.01)
                    edgeList.add(new int[]{aid, bid});
            }
        }

        final Graph graph = game.util.graph.Graph.createFromLists(vertexList, edgeList);
        graph.setBasisAndShape(basisIn, shapeIn);

//		System.out.println("result has " + result.vertices().size() + " vertices, " +
//							result.edges().size() + " edges and " +
//							result.faces().size() + " faces.");
        return graph;
    }

    /**
     * Only applies to graph with uniform edge lengths.
     *
     * @param vertexList The list of the vertices.
     * @param u          The unit.
     * @param basisIn    The basis.
     * @param shapeIn    The shape.
     * @return Graph generated by this vertex list (3D version).
     */
    public static Graph createGraphFromVertexList3D
    (
            final List<double[]> vertexList,
            final double u,
            final BasisType basisIn,
            final ShapeType shapeIn
    ) {
        // Create edges
        final List<int[]> edgeList = new ArrayList<int[]>();
        for (int aid = 0; aid < vertexList.size(); aid++) {
            final double[] ptA = vertexList.get(aid);

            final double ax = ptA[0];
            final double ay = ptA[1];
            final double az = (ptA.length > 2) ? ptA[2] : 0;

            // Check for edges 1 unit away
            for (int bid = aid + 1; bid < vertexList.size(); bid++) {
                final double[] ptB = vertexList.get(bid);

                final double bx = ptB[0];
                final double by = ptB[1];
                final double bz = (ptB.length > 2) ? ptB[2] : 0;

                final double dist = MathRoutines.distance(ax, ay, az, bx, by, bz);
                if (Math.abs(dist - u) < 0.01) {
                    edgeList.add(new int[]{aid, bid});
                }
            }
        }

        final Graph graph = game.util.graph.Graph.createFromLists(vertexList, edgeList);
        graph.setBasisAndShape(basisIn, shapeIn);

//		System.out.println("result has " + result.vertices().size() + " vertices, " +
//							result.edges().size() + " edges and " +
//							result.faces().size() + " faces.");
        return graph;
    }

//	public void setBasisAndShape(final BasisType bt, final ShapeType st)
//	{
//		basis = bt;
//		shape = st;
//	}

    /**
     * @return Known tiling, else null.
     */
    public BasisType basis() {
        return basis;
    }

    /**
     * @return Known shape, else null.
     */
    public ShapeType shape() {
        return shape;
    }

    /**
     * Reset the basis.
     */
    public void resetBasis() {
        basis = BasisType.NoBasis;
    }

    /**
     * Reset the shape.
     */
    public void resetShape() {
        shape = ShapeType.NoShape;
    }

    //-------------------------------------------------------------------------

    //public abstract Point2D xy(final int row, final int col);

    //-------------------------------------------------------------------------

    /**
     * @return The dimensions of the shape.
     */
    @Override
    public int[] dim() {
        return dim;
    }

    //-------------------------------------------------------------------------

    /**
     * @return Maximum dimension of this shape.
     */
    public int maxDim() {
        int max = 0;
        for (int d = 0; d < dim.length; d++)
            if (dim[d] > max)
                max = dim[d];
        return max;
    }

    @Override
    public Graph eval(final Context context, final SiteType siteType) {
        System.out.println("BaseGraphFunction.eval(): Should not be called directly; call subclass e.g. RectangleOnSquare.eval().");
        return null;
    }

    //-------------------------------------------------------------------------

    @Override
    public boolean isStatic() {
        return true;
    }

    //-------------------------------------------------------------------------

}
