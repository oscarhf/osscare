package gessi.plateoss.queso.test;

import java.sql.SQLException;

import gessi.plateoss.commons.json.PlateosBD;

public class TestOssecoBD {

	public static void main(String[] args) {
		
		PlateosBD objBd = new PlateosBD();
		//String listOssecos=  objBd.getOssecosList();
		//System.out.println(listOssecos);
		System.out.println(objBd.getOssecoInstance("Eclipse"));
		System.out.println("end");
		
		
		/*try {
			//String endPoint=  objBd.getOssecoPath("Eclipse");
			System.out.println("xxxxx");
			//System.out.println(endPoint);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		

	}
}
