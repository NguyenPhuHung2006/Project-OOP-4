package utils;

import object.GameObject;

public class IntersectUtils {
    public static boolean intersect(GameObject a, GameObject b) {
        return  a.getX() <= b.getX() + b.getWidth() &&   // A.left < B.right
                a.getX() + a.getWidth() >= b.getX() &&  // A.right > B.left
                a.getY() <= b.getY() + b.getHeight() && // A.top < B.bottom
                a.getY() + a.getHeight() >= b.getY();   // A.bottom > B.top
    }
}
