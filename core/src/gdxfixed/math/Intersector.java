package gdxfixed.math;

import com.badlogic.gdx.math.Polygon;

import static com.badlogic.gdx.math.Intersector.pointLineSide;

/**
 * Libgdx has a bug in version 1.9.9
 * The bug fix hasn't been released officially yet.
 * That's why the fix is added here.
 * https://github.com/libgdx/libgdx/pull/5048/commits/43f9f644977b02d85569773ead60334037193c49
 * - Ken Schosinky
 */
public class Intersector {


    /** Check whether specified counter-clockwise wound convex polygons overlap.
     *
     * @param p1 The first polygon.
     * @param p2 The second polygon.
     * @return Whether polygons overlap. */
    public static boolean overlapConvexPolygons (Polygon p1, Polygon p2) {
        return overlapConvexPolygons(p1, p2, null);
    }

    /** Check whether specified counter-clockwise wound convex polygons overlap. If they do, optionally obtain a Minimum
     * Translation Vector indicating the minimum magnitude vector required to push the polygon p1 out of collision with polygon p2.
     *
     * @param p1 The first polygon.
     * @param p2 The second polygon.
     * @param mtv A Minimum Translation Vector to fill in the case of a collision, or null (optional).
     * @return Whether polygons overlap. */
    public static boolean overlapConvexPolygons (Polygon p1, Polygon p2, com.badlogic.gdx.math.Intersector.MinimumTranslationVector mtv) {
        return overlapConvexPolygons(p1.getTransformedVertices(), p2.getTransformedVertices(), mtv);
    }

    /** @see #overlapConvexPolygons(float[], int, int, float[], int, int, com.badlogic.gdx.math.Intersector.MinimumTranslationVector) */
    public static boolean overlapConvexPolygons (float[] verts1, float[] verts2, com.badlogic.gdx.math.Intersector.MinimumTranslationVector mtv) {
        return overlapConvexPolygons(verts1, 0, verts1.length, verts2, 0, verts2.length, mtv);
    }

    /** Check whether polygons defined by the given counter-clockwise wound vertex arrays overlap. If they do, optionally obtain a
     * Minimum Translation Vector indicating the minimum magnitude vector required to push the polygon defined by verts1 out of the
     * collision with the polygon defined by verts2.
     *
     * @param verts1 Vertices of the first polygon.
     * @param verts2 Vertices of the second polygon.
     * @param mtv A Minimum Translation Vector to fill in the case of a collision, or null (optional).
     * @return Whether polygons overlap. */
    public static boolean overlapConvexPolygons (float[] verts1, int offset1, int count1, float[] verts2, int offset2, int count2,
                                                 com.badlogic.gdx.math.Intersector.MinimumTranslationVector mtv) {
        float overlap = Float.MAX_VALUE;
        float smallestAxisX = 0;
        float smallestAxisY = 0;

        int end1 = offset1 + count1;
        int end2 = offset2 + count2;

        float min1X = Float.MAX_VALUE, min1Y = Float.MAX_VALUE, min2X = Float.MAX_VALUE, min2Y = Float.MAX_VALUE;
        float max1X = Float.MIN_VALUE, max1Y = Float.MIN_VALUE, max2X = Float.MIN_VALUE, max2Y = Float.MIN_VALUE;

        // Get polygon1 axes
        for (int i = offset1; i < end1; i += 2) {
            float x1 = verts1[i];
            float y1 = verts1[i + 1];
            float x2 = verts1[(i + 2) % count1];
            float y2 = verts1[(i + 3) % count1];

            if (x1 < min1X) {
                min1X = x1;
            }
            if (x1 > max1X) {
                max1X = x1;
            }
            if (y1 < min1Y) {
                min1Y = y1;
            }
            if (y1 > max1Y) {
                max1Y = y1;
            }

            float axisX = y1 - y2;
            float axisY = -(x1 - x2);

            final float length = (float)Math.sqrt(axisX * axisX + axisY * axisY);
            axisX /= length;
            axisY /= length;

            // -- Begin check for separation on this axis --//

            // Project polygon1 onto this axis
            float min1 = axisX * verts1[0] + axisY * verts1[1];
            float max1 = min1;
            for (int j = offset1; j < end1; j += 2) {
                float p = axisX * verts1[j] + axisY * verts1[j + 1];
                if (p < min1) {
                    min1 = p;
                } else if (p > max1) {
                    max1 = p;
                }
            }

            // Project polygon2 onto this axis
            float min2 = axisX * verts2[0] + axisY * verts2[1];
            float max2 = min2;
            for (int j = offset2; j < end2; j += 2) {
                // Counts the number of points that are within the projected area.
                float p = axisX * verts2[j] + axisY * verts2[j + 1];
                if (p < min2) {
                    min2 = p;
                } else if (p > max2) {
                    max2 = p;
                }
            }

            if (!(min1 <= min2 && max1 >= min2 || min2 <= min1 && max2 >= min1)) {
                return false;
            } else {
                float o = Math.min(max1, max2) - Math.max(min1, min2);
                if (min1 < min2 && max1 > max2 || min2 < min1 && max2 > max1) {
                    float mins = Math.abs(min1 - min2);
                    float maxs = Math.abs(max1 - max2);
                    if (mins < maxs) {
                        o += mins;
                    } else {
                        o += maxs;
                    }
                }
                if (o < overlap) {
                    overlap = o;
                    smallestAxisX = axisX;
                    smallestAxisY = axisY;
                }
            }
            // -- End check for separation on this axis --//
        }

        // Get polygon2 axes
        for (int i = offset2; i < end2; i += 2) {
            float x1 = verts2[i];
            float y1 = verts2[i + 1];
            float x2 = verts2[(i + 2) % count2];
            float y2 = verts2[(i + 3) % count2];

            if (x1 < min2X) {
                min2X = x1;
            }
            if (x1 > max2X) {
                max2X = x1;
            }
            if (y1 < min2Y) {
                min2Y = y1;
            }
            if (y1 > max2Y) {
                max2Y = y1;
            }

            float axisX = y1 - y2;
            float axisY = -(x1 - x2);

            final float length = (float)Math.sqrt(axisX * axisX + axisY * axisY);
            axisX /= length;
            axisY /= length;

            // -- Begin check for separation on this axis --//

            // Project polygon1 onto this axis
            float min1 = axisX * verts1[0] + axisY * verts1[1];
            float max1 = min1;
            for (int j = offset1; j < end1; j += 2) {
                float p = axisX * verts1[j] + axisY * verts1[j + 1];
                // Counts the number of points that are within the projected area.
                if (p < min1) {
                    min1 = p;
                } else if (p > max1) {
                    max1 = p;
                }
            }

            // Project polygon2 onto this axis
            float min2 = axisX * verts2[0] + axisY * verts2[1];
            float max2 = min2;
            for (int j = offset2; j < end2; j += 2) {
                float p = axisX * verts2[j] + axisY * verts2[j + 1];
                if (p < min2) {
                    min2 = p;
                } else if (p > max2) {
                    max2 = p;
                }
            }

            if (!(min1 <= min2 && max1 >= min2 || min2 <= min1 && max2 >= min1)) {
                return false;
            } else {
                float o = Math.min(max1, max2) - Math.max(min1, min2);

                if (min1 < min2 && max1 > max2 || min2 < min1 && max2 > max1) {
                    float mins = Math.abs(min1 - min2);
                    float maxs = Math.abs(max1 - max2);
                    if (mins < maxs) {
                        o += mins;
                    } else {
                        o += maxs;
                    }
                }

                if (o < overlap) {
                    overlap = o;
                    smallestAxisX = axisX;
                    smallestAxisY = axisY;
                }
            }
            // -- End check for separation on this axis --//
        }
        if (mtv != null) {
            // Adjusts the direction based on the direction of verts1 to verts2
            float center1X = min1X + 0.5f * (max1X - min1X);
            float center1Y = min1Y + 0.5f * (max1Y - min1Y);
            float center2X = min2X + 0.5f * (max2X - min2X);
            float center2Y = min2Y + 0.5f * (max2Y - min2Y);

            float dirX = center2X - center1X;
            float dirY = center2Y - center1Y;

            float dot = dirX * smallestAxisX + dirY * smallestAxisY;
            if (dot > 0) {
                smallestAxisX = -smallestAxisX;
                smallestAxisY = -smallestAxisY;
            }

            mtv.normal.set(smallestAxisX, smallestAxisY);
            mtv.depth = overlap;
        }
        return true;
    }

}
