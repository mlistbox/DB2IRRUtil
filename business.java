

import java.util.Calendar;

public class business {
	public int year;
	public int month;
	public int day;
	public double payment;// 对应的现金
 
	public business(int year, int month, int day, double payment) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.payment = payment;
	}
 
	/**
	 * 
	 * 
	 * @param payment  对应的现金
	 */
	public business(Integer date, double payment) {
		try {			
			this.year=date/10000;
			this.month=date%10000/100;
			this.day=date%100;			
		} catch (Exception e) {
			// TODO: handle exception
		}
		this.payment=payment;
	}
	
	
 
	/**
	 * 使用默认构造方法的话，则对应的是当前的时间，即今天的时间
	 */
	
	

	public void setBusiness(Integer date, double payment) {
		try {			
			this.year=date/10000;
			this.month=date%10000/100;
			this.day=date%100;			
		} catch (Exception e) {
			// TODO: handle exception
		}
		this.payment=payment;
	}

	/**
	 * 获得1970年到指定日期的天数
	 * @return long
	 */
	public long getDaysFrom1970(){
		
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month - 1);
			c.set(Calendar.DAY_OF_MONTH, day);
			return (long) (c.getTimeInMillis() / 86400000);
		}		

	

}
