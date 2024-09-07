package pl.blackwaterapi.utils;

public class MathUtil
{
  public static int floor(double value)
  {
    int i = (int)value;
    return value < i ? i - 1 : i;
  }
  
  public static double round(double value, int decimals)
  {
    double p = Math.pow(10.0D, decimals);
    return Math.round(value * p) / p;
  }
}
