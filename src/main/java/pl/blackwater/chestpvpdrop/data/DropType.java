package pl.blackwater.chestpvpdrop.data;

public enum DropType
{
  CANCEL_DROP,  NORMAL_DROP,  RANDOM_DROP;
  
  public static DropType getFromName(String s)
  {
    DropType[] values;
    int length = (values = values()).length;
    for (int i = 0; i < length; i++)
    {
      DropType dt = values[i];
      if ((dt.name().replace("_", "").equalsIgnoreCase(s)) || (dt.name().equalsIgnoreCase(s))) {
        return dt;
      }
    }
    return null;
  }
}
