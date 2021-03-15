package net.rypixel.hiveLobby;

import java.util.ArrayList;
import java.util.List;

public class Functions {
	
	public static <T> ArrayList<T> ArrayToListConversion(T array[])   
	{   
		ArrayList<T> list = new ArrayList<>();   
			for (T t : array)   
			{   
				list.add(t);   
			}   
		return list;   
	}
	
	public static String ListToCSV(ArrayList<String> list) {
		String output = "";
		for (String v : list) {
			if (output == "") {
				output = v;
			} else {
				output += v;
			}
		}
		return output;
	}
	
}
