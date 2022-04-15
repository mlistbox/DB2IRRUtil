

import java.util.Calendar;

public class business {
	public int year;
	public int month;
	public int day;
	public double payment;// ��Ӧ���ֽ�
 
	public business(int year, int month, int day, double payment) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.payment = payment;
	}
 
	/**
	 * 
	 * 
	 * @param payment  ��Ӧ���ֽ�
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
	 * ʹ��Ĭ�Ϲ��췽���Ļ������Ӧ���ǵ�ǰ��ʱ�䣬�������ʱ��
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
	 * ���1970�굽ָ�����ڵ�����
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
