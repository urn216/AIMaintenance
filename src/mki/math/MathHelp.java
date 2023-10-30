package mki.math;

import java.util.function.BiConsumer;

import mki.math.vector.Vector2;
import mki.math.vector.Vector3;

/**
 * class for help with maths stuff
 * 
 * @author William Kilty
 */
public class MathHelp {

  /**
   * A constant for the square-root of two ~= {@code 1.414}
   */
  public static final double ROOT_TWO = Math.sqrt(2);
  /**
   * A constant for one over the square-root of two ~= {@code 0.707}
   */
  public static final double INVERSE_ROOT_TWO = 1/ROOT_TWO;

  /**
  * Clamps a value to one between an upper and lower bound.
  *
  * @param val The value to clamp.
  * @param l The lower bound.
  * @param u The upper bound.
  *
  * @return The clamped value.
  */
  public static final int clamp(int val, int l, int u) {
    return Math.min(Math.max(val, l), u);
  }

  /**
  * Clamps a value to one between an upper and lower bound.
  *
  * @param val The value to clamp.
  * @param l The lower bound.
  * @param u The upper bound.
  *
  * @return The clamped value.
  */
  public static final long clamp(long val, long l, long u) {
    return Math.min(Math.max(val, l), u);
  }

  /**
  * Clamps a value to one between an upper and lower bound.
  *
  * @param val The value to clamp.
  * @param l The lower bound.
  * @param u The upper bound.
  *
  * @return The clamped value.
  */
  public static final float clamp(float val, float l, float u) {
    return Math.min(Math.max(val, l), u);
  }

  /**
  * Clamps a value to one between an upper and lower bound.
  *
  * @param val The value to clamp.
  * @param l The lower bound.
  * @param u The upper bound.
  *
  * @return The clamped value.
  */
  public static final double clamp(double val, double l, double u) {
    return Math.min(Math.max(val, l), u);
  }

  /**
  * Gives a value a certain percentage of the way from one point to another.
  *
  * @param start The starting value.
  * @param end The ending value.
  * @param p The percentage of the way through the transition.
  *
  * @return The lerped value.
  */
  public static final double lerp(double start, double end, double p) {
    return start + (end-start)*p;
  }

  /**
   * Converts a {@code boolean} into a single bit.
   * 
   * @param b the {@code boolean} to represent as a bit
   * 
   * @return {@code 1} if input is {@code true}; {@code 0} if input is {@code false}
   */
  public static int booleanToInt(boolean b) {return b ? 1 : 0;}

  /**
   * Converts an {@code int} into a {@code boolean}.
   * 
   * @param i the {@code int} to represent as a {@code boolean}
   * 
   * @return {@code false} if input is {@code 0}; {@code true} if input is anything else
   */
  public static boolean intToBoolean(int i) {return i == 0 ? false : true;}

  /**
   * Toggles a specified bit within a number without altering any other bits within the number
   * <p>
   * If the existing bit is {@code 1}, the output will contain {@code 0} at that location.
   * <p>
   * If the existing bit is {@code 0}, the output will contain {@code 1} at that location.
   * 
   * @param num the input number to alter.
   * @param b the index of the bit to toggle.
   * 
   * @return the input number with the bit at location {@code b} toggled
   */
  public static int toggleBit(int num, int b) {
    return (num & (1 << b)) == 0 ? num | 1 << b : num & ~(1 << b);
  }

  /**
   * Finds the index through an array which points to the largest value.
   * 
   * @param vals the list of values to find the argmax of
   * 
   * @return the index of the largest element in the supplied set of values
   */
  public static int argMax(double... vals) {
    double max = Double.NEGATIVE_INFINITY;
    int argMax = -1;
    for (int i = 0; i < vals.length; i++) {
      if (vals[i] >= max) {
        max = vals[i];
        argMax = i;
      }
    }
    return argMax;
  }

  /**
   * A formula giving the probability of any given point being the outcome of normally distributed data
   * 
   * @param x the value of which to give the probability
   * @param mu the mean value of the normal distribution
   * @param sigma the standard deviation of the normal distribution
   * 
   * @return the probability that any datapoint will be x on a normal distribution curve
   */
  public static final double normDistrib(double x, double mu, double sigma) {
    return (Math.exp(-0.5*Math.pow((x-mu)/sigma, 2)))/(sigma*Math.sqrt(2*Math.PI));
  }

  /**
   * A formula giving the possible outcomes of a given probability in a normal distribution curve
   * 
   * @param p the probability to give the result of
   * @param mu the mean value of the normal distribution
   * @param sigma the standard deviation of the normal distribution
   * 
   * @return the x values of a given probability showing up on a normal distribution curve
   */
  public static final double[] inverseNormDistrib(double p, double mu, double sigma) {
    double offset = sigma*Math.sqrt(-2*Math.log(p*sigma*Math.sqrt(2*Math.PI)));
    return new double[]{mu+offset, mu-offset};
  }

  /**
   * Fast absolute value calculation. Calculation is sped up by using bit manipulation.
   * 
   * @param val the value to return the absolute value of
   * 
   * @return the absolute value of the input {@code int}
   */
  public static int abs(int val) {
    int mask = val>>31;
    return (val^mask)-mask;
  }

  /**
   * Calculates the mean of a set of values.
   * 
   * @param vals the values to calculate the mean of
   * 
   * @return the mean of the given values
   */
  public static final double avg(double... vals) {
    double res = 0;
    for (double val : vals) res+=val;
    return res/vals.length;
  }

  /**
   * Based on Bresenham's Line Algorithm.
   * <p>
   * Performs an action at each {@code Integer} coordinate of a line segment.
   * <p>
   * Utilises only bitwise operators and addition/subtraction. Very fast.
   * 
   * @param x1 the first x coordinate (does not have to be less than x2)
   * @param y1 the first y coordinate (does not have to be less than y2)
   * @param x2 the second x coordinate
   * @param y2 the second y coordinate
   * @param c the action to perform at each {@code x} and {@code y} coordinate along the line
   */
  public static void line2DToInt(int x1, int y1, int x2, int y2, BiConsumer<Integer, Integer> c) {
    int dx = MathHelp.abs(x2-x1);
    int sx = (((x2-x1)>>31)<<1)+1; //normalises dx
    int dy = -MathHelp.abs(y2-y1);
    int sy = (((y2-y1)>>31)<<1)+1; //normalises dy

    int e = dx+dy; //average error
    
    while (true) {
      c.accept(x1, y1);
      if (x1==x2 && y1==y2) break;
      int e2 = 2*e; //double error, rather than seeing if e>0.5*limit, so no division
      if (e2>=dy) { //too much y error, do an x step
        if (x1 == x2) break; //if we've done all the x steps we need, we're done
        e+=dy; //lower the error by dy
        x1+=sx; //shifts one step in the correct direction
      }
      if (e2<=dx) { //too much x error, do a y step
        if (y1 == y2) break; //if we've done all the y steps we need, we're done
        e+=dx; //increase the error by dx
        y1+=sy; //shifts one step in the correct direction
      }
    }
  }

  /**
   * Based on Bresenham's Line Algorithm.
   * <p>
   * Performs an action at each {@code Integer} coordinate of a line segment.
   * <p>
   * Utilises only bitwise operators and addition/subtraction. Very fast.
   * 
   * @param x1 the first x coordinate (does not have to be less than x2)
   * @param y1 the first y coordinate (does not have to be less than y2)
   * @param z1 the first z coordinate (does not have to be less than z2)
   * @param x2 the second x coordinate
   * @param y2 the second y coordinate
   * @param z2 the second z coordinate
   * @param c the action to perform at each {@code x} and {@code y} coordinate along the line
   */
  public static void line3DToInt(int x1, int y1, int z1, int x2, int y2, int z2, TriConsumer<Integer, Integer, Integer> c) {
    int dz = MathHelp.abs(z2-z1);
    int sz = (((z2-z1)>>31)<<1)+1; //normalises dz

    if (dz == 0) {
      line2DToInt(x1, y1, x2, y2, (x, y) -> c.accept(x, y, z2));
      return;
    }

    int dx = MathHelp.abs(x2-x1);
    int sx = (((x2-x1)>>31)<<1)+1; //normalises dx
    int dy = MathHelp.abs(y2-y1);
    int sy = (((y2-y1)>>31)<<1)+1; //normalises dy

    int e1 = 2*dx-dz;
    int e2 = 2*dy-dz;
    
    while (true) {
      c.accept(x1, y1, z1);
      if (y1==y2 && x1==x2) break;
      if (e1>=0) { //too much x error, do a y step 
        if (y1 == y2) break;
        y1+=sy;
        e1-=2*dz;
      }
      if (e2>=0) {//too much y error, do an x step
        if (x1 == x2) break;
        x1+=sx;
        e2-=2*dz;
      }
      e1 += 2*dy;
      e2 += 2*dx;
      z1 += sz;
    }
  }

  /**
   * Finds the intensity of light at a given distance from a point-source of light.
   * Simulates dispersion of light emitted spherically.
   * 
   * @param source the intensity of light at the point-source of light
   * @param distSquare the distance squared from the light source to find the resulting light intensity at
   * 
   * @return the intensity of light at a distance from a source.
   */
  public static double intensity(double source, double distSquare) {return source/(1+sphereSurfaceArea(distSquare));}

  /**
   * Finds the surface area of a sphere.
   * 
   * @param rSquare the radius of the sphere squared
   * 
   * @return the surface area of the given sphere.
   */
  public static double sphereSurfaceArea(double rSquare) {return Math.PI*4.0*rSquare;}

  /**
   * Returns UV co-ordinates for a sphere at a given normalised surface direction in 3D
   * 
   * @param sNormal a normal {@code Vector3} pointing out from the sphere in a direction
   * 
   * @return the corresponding UV co-ordinates in {@code Vector2} format.
   */
  public static Vector2 sphereUVPoint(Vector3 sNormal) {return new Vector2(0.5 + (Math.atan2(-sNormal.x, sNormal.z))/(2*Math.PI), 0.5 - (Math.asin(sNormal.y))/Math.PI);}

  /**
   * Returns UV co-ordinates for an inverted sphere at a given normalised surface direction in 3D
   * 
   * @param sNormal a normal {@code Vector3} pointing into the sphere in a direction
   * 
   * @return the corresponding UV co-ordinates in {@code Vector2} format.
   */
  public static Vector2 sphereUVPointInv(Vector3 sNormal) {return new Vector2(0.5 + (Math.atan2(sNormal.x, sNormal.z))/(2*Math.PI), 0.5 - (Math.asin(sNormal.y))/Math.PI);}

  /**
   * Swaps two elements in an array.
   * 
   * @param <T> the type of elements stored within the array
   * 
   * @param arr the array to swap elements of
   * @param i the index of the first object to swap
   * @param j the index of the second object to swap
   */
  public static <T> void swap(T[] arr, int i, int j) {
    T temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }

  /**
   * Rounds a supplied {@code double} value to the nearest whole multiple of the given step size.
   * <p>
   * For example, with a step size of {@code 1}, the value {@code 2.6} will round to {@code 3}.
   * 
   * @param unRounded the value to round
   * @param stepSize the step size to fit the unrounded value to
   * 
   * @return the input value rounded to the nearest step determined by {@code stepSize}
   */
  public static final double roundToNearestStep(double unRounded, double stepSize) {
    return Math.round(unRounded/stepSize)*stepSize;
  }
}
