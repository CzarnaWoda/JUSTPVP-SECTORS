package pl.blackwater.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtil {
	public static String convertListToString(List<String> list){
		StringBuilder s = new StringBuilder();
		for(String string : list){
			s.append(string).append(";");
		}
		return s.toString();
	}
	public static List<String> convertStringToList(String s){
		String [] array = s.split(";");
		List<String> list = new ArrayList<>();
		Collections.addAll(list, array);
		return list;
	}
	public static List<Integer> convertStringToIntegerList(String s){
		List<Integer> integers = new ArrayList<>();

		String string = s.replace("[" , "").replace("]" ,"").replace(" ", "");
		if(string.equals("")){
			return integers;
		}
		for(String s1 : string.split(",")){
			integers.add(Integer.parseInt(s1));
		}
		return integers;
	}
	public static String convertIntegerListToString(List<Integer> list){
		List<String> stringList = new ArrayList<>();
		list.forEach(integer -> stringList.add(integer.toString()));
		return stringList.toString().replace("[" , "").replace("]","");
	}
}
