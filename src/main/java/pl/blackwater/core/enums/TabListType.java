package pl.blackwater.core.enums;

public enum TabListType {
    KILLS(1),DEATHS(2),BREAKSTONE(3);

    public int index;

    TabListType(int i) {
        index = i;
    }
    public static TabListType getByIndex(int index){
        for(TabListType t : values()){
            if(t.index == index){
                return t;
            }
        }
        return null;
    }
}
