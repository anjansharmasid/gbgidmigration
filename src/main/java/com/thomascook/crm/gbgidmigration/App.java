package com.thomascook.crm.gbgidmigration;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        StringBuffer  cust_ref_num = new StringBuffer();
        ArrayList<Long> cust_ref_nums = new ArrayList<Long>();
        //cust_ref_nums.add(2323232340L);
        //cust_ref_nums.add(2323232341L);
        //cust_ref_nums.add(2323232342L);
        //cust_ref_nums.add(2323232343L);
        int coma = cust_ref_nums.size(); 
        for(long l: cust_ref_nums ){
        	cust_ref_num.append(l);
        	if(coma > 1){
        		cust_ref_num.append(", ");
        		coma = coma -1;
        	}
        }
        System.out.println(cust_ref_num.toString());
        
    }
}
