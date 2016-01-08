package com.zy.sualianwb.util;

import com.zy.sualianwb.module.ShowTime3;

import java.util.Comparator;

public class ShowTimeComparable implements Comparator<ShowTime3>{

	public int compare(ShowTime3 o1, ShowTime3 o2) {
		long id1 = o1.getId();
		long id2 = o2.getId();
		if(id1>id2){
			return 1;
		}else if(id1==id2){
			return 0;
		}else{
			return -1;
		}
		
	}

	

}
