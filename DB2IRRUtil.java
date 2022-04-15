import COM.ibm.db2.app.*;
import java.math.BigDecimal;
import java.util.CopyOnWriteArrayList;
import java.util.List;
import java.lang.*;         // for String class
import java.io.*; 
import java.util.Calendar;
import java.security.MessageDigest;
import java.util.concurrent.CopyOnWriteArrayList;

public class DB2IRRUtil extends UDF{
public static long diffdays(Calendar a,Calendar b) throws Exception
{
		//System.out.println(Math.abs((a.getTime().getTime() - b.getTime().getTime())/ (24 * 60 * 60 * 1000)));
		return Math.abs((a.getTime().getTime() - b.getTime().getTime())/ (24 * 60 * 60 * 1000));
	
}
public static long diffdays30(Calendar a,Calendar b) throws Exception
{
		
		return Math.abs((a.get(Calendar.YEAR)-b.get(Calendar.YEAR))*360+(a.get(Calendar.MONTH)-b.get(Calendar.MONTH))*30+a.get(Calendar.DAY_OF_MONTH)-b.get(Calendar.DAY_OF_MONTH));
}
 public static double irr(double[] income) throws Exception {
        return irr(income, 0.00001D);
    }
 ////@param  dealamount ���Ž��
 ////@param repayment ÿ�����ʱ�ڵĻ�����
 /////��ͬ������double rate
 ////@param Repaymenttimes ��ͬ����ȫ���������
////@param frequency ÿ��Ļ������
////@param repaymentmethod �����
//////expirydate  ������
////////int cycle ��������
////////maturitydate������
///////dealdate ������
 ///////3�̶����ڸ�Ϣ���ڻ���
 ///////5��Ϣ���¾���
 ////@return double �ֽ���������
 ////loanirr����
 public static double loanirr(int dealdate,int maturitydate,int cycle_m,int Repaymenttimes,int expirydate, double dealamount,double rate,int repaymentmethod) throws Exception
 {
	 try {
		double r=0;
		if (repaymentmethod==5)
		{
			r=Xirr(cashflow301(dealdate,maturitydate,cycle_m,Repaymenttimes,expirydate,dealamount,rate));
		}
		else
		{
			r=Xirr(cashflow302(dealdate,maturitydate,cycle_m,Repaymenttimes,expirydate,dealamount,rate));
		}
		return r;
	 } 
	 catch (Exception e) {
		 ///e.printStackTrace();
		 return 505;
	 }
 }
 //loanXirr03 ���� @ dealdate ������,@ maturitydate ������,@ cycle_m  ��Ϣ����,@ expirydate  ��Ϣ��, @ dealamount  ���Ž��   @ rate ����
    public static double loanXirr3(int dealdate,int maturitydate,int cycle_m,int expirydate, double dealamount,double rate)
	{
		
		try{
			List<business> list=new CopyOnWriteArrayList<business>();
			list=cashflow(dealdate, maturitydate,cycle_m,expirydate,dealamount,rate);
			return Xirr(list);
		}
		catch (Exception e) {
				return 505;
			}		
    }
 
 
 public static List<business> cashflow(Integer paybackdates[],Double paybackcashs[])
 {
	 try {
		 List<business> list=new CopyOnWriteArrayList<business>();
		 int j=paybackdates.length;
		 if(j!=paybackcashs.length) return null;
		 for(int i=0;i<j;i++)
		 {
			 list.add(new business(paybackdates[i],paybackcashs[i]));
		 }
		 
		 return list;
	} 
	 catch (Exception e) {
		 ///e.printStackTrace();
		 return null;
	 }
 }
 
 public static List<business> cashflow(int dealdate,double dealamount,Integer paybackdates[],Double paybackcashs[])
 {
	 try {
		 List<business> list=new CopyOnWriteArrayList<business>();
		 business b=new business(dealdate,dealamount);
		 list.add(b);
		 int j=paybackdates.length;
		 if(j!=paybackcashs.length) return null;
		 for(int i=0;i<j;i++)
		 {
			 list.add(new business(paybackdates[i],paybackcashs[i]));
		 }
		 
		 return list;
	} 
	 catch (Exception e) {
		 ///e.printStackTrace();
		 return null;
	 }
 }
 public static List<business> cashflow(Integer dealdate,Integer maturitydate,Integer cycle,Integer expirydate, double dealamount,double rate)
 {
	 try {
		 List<business> list=new CopyOnWriteArrayList<business>();
		 business b=new business(dealdate,dealamount*-1);
		 list.add(b);
		 Calendar startdate = Calendar.getInstance();
		 Calendar enddate = Calendar.getInstance();
		 Calendar cycledate = Calendar.getInstance();
		 startdate.set(dealdate/10000,dealdate%10000/100-1,dealdate%100);
		 enddate.set(maturitydate/10000,maturitydate%10000/100-1,maturitydate%100);
		 cycledate.set(dealdate/10000,dealdate%10000/100-1,expirydate);
		 
		 Calendar tmpdate=startdate;
		 if(startdate.compareTo(cycledate)==1)cycledate.add(Calendar.MONTH,cycle);
		 rate=rate*0.01/365f;
		 
		 while((cycledate.compareTo(startdate)==1|cycledate.compareTo(startdate)==0)&cycledate.compareTo(enddate)==-1)
		 {
			 list.add(new business(cycledate.get(Calendar.YEAR)*10000+cycledate.get(Calendar.MONTH)*100+100+cycledate.get(Calendar.DAY_OF_MONTH),diffdays(cycledate,tmpdate)*rate*dealamount));
			 tmpdate.set(cycledate.get(Calendar.YEAR),cycledate.get(Calendar.MONTH),cycledate.get(Calendar.DAY_OF_MONTH));
			 cycledate.add(Calendar.MONTH,cycle);
		 }
		 business c=new business(maturitydate,dealamount+diffdays(enddate,tmpdate)*rate*dealamount);
		 list.add(c);
		 
		 return list;
	} 
	 catch (Exception e) {
		 ////////e.printStackTrace();
		 return null;
	 }
 }
 /////////////////�ú������ɵȶϢ���ֽ�����
 public static List<business> cashflow301(Integer dealdate,Integer maturitydate,Integer cycle,Integer Repaymenttimes,Integer expirydate, double dealamount,double rate)
 {
	 try {
		 List<business> list=new CopyOnWriteArrayList<business>();
		 business b=new business(dealdate,dealamount*-1);
		 list.add(b);
		 Calendar startdate = Calendar.getInstance();
		 Calendar enddate = Calendar.getInstance();
		 Calendar cycledate = Calendar.getInstance();
		 startdate.set(dealdate/10000,dealdate%10000/100-1,dealdate%100);
		 enddate.set(maturitydate/10000,maturitydate%10000/100-1,maturitydate%100);
		 cycledate.set(dealdate/10000,dealdate%10000/100-1,expirydate);
		 
		 Calendar tmpdate=startdate;
		 cycledate.add(Calendar.MONTH,cycle);///////30���Ϣ�Ĵ���ڷ����½�Ϣ
		 rate=rate*0.01/360f;
		 int i=1;
		 double everycyclemount=0;//////ÿ����Ӧ������
		 double submount=dealamount;/////////ÿ���ڻ������ʣ��ı���
		 while(cycledate.compareTo(startdate)==1&cycledate.compareTo(enddate)==-1)
		 {
			everycyclemount=(dealamount*rate*30*Math.pow(1+rate*30,i-1))/(Math.pow(1+rate*30,Repaymenttimes)-1);///////���㱾����Ӧ������
			list.add(new business(cycledate.get(Calendar.YEAR)*10000+cycledate.get(Calendar.MONTH)*100+100+cycledate.get(Calendar.DAY_OF_MONTH),
				(diffdays30(cycledate,tmpdate))*rate*submount+everycyclemount));
			 tmpdate.set(cycledate.get(Calendar.YEAR),cycledate.get(Calendar.MONTH),cycledate.get(Calendar.DAY_OF_MONTH));
			 cycledate.add(Calendar.MONTH,cycle);
			 submount=submount-everycyclemount;
			 i++;
		 }
		 business c=new business(maturitydate,submount+diffdays30(enddate,tmpdate)*rate*submount);
		 list.add(c);
		 
		 return list;
	} 
	 catch (Exception e) {
		 ////////e.printStackTrace();
		 return null;
	 }
 }
 /////////////////�ú������ɵȶ����ֽ�����
 public static List<business> cashflow302(Integer dealdate,Integer maturitydate,Integer cycle,Integer Repaymenttimes,Integer expirydate, double dealamount,double rate)
 {
	 try {
		 List<business> list=new CopyOnWriteArrayList<business>();
		 business b=new business(dealdate,dealamount*-1);
		 list.add(b);
		 Calendar startdate = Calendar.getInstance();
		 Calendar enddate = Calendar.getInstance();
		 Calendar cycledate = Calendar.getInstance();
		 startdate.set(dealdate/10000,dealdate%10000/100-1,dealdate%100);
		 enddate.set(maturitydate/10000,maturitydate%10000/100-1,maturitydate%100);
		 cycledate.set(dealdate/10000,dealdate%10000/100-1,expirydate);
		 
		 Calendar tmpdate=startdate;
		 cycledate.add(Calendar.MONTH,cycle);///////30���Ϣ�Ĵ���ڷ����½�Ϣ
		 rate=rate*0.01/360f;
		 int i=1;
		 double everycyclemount=dealamount/Repaymenttimes;//////ÿ����Ӧ������
		 double submount=dealamount;/////////ÿ���ڻ������ʣ��ı���
		 while(cycledate.compareTo(startdate)==1&cycledate.compareTo(enddate)==-1)
		 {
			list.add(new business(cycledate.get(Calendar.YEAR)*10000+cycledate.get(Calendar.MONTH)*100+100+cycledate.get(Calendar.DAY_OF_MONTH),
				(diffdays30(cycledate,tmpdate))*rate*submount+everycyclemount));
			 tmpdate.set(cycledate.get(Calendar.YEAR),cycledate.get(Calendar.MONTH),cycledate.get(Calendar.DAY_OF_MONTH));
			 cycledate.add(Calendar.MONTH,cycle);
			 submount=submount-everycyclemount;
			 i++;
		 }
		 business c=new business(maturitydate,submount+diffdays30(enddate,tmpdate)*rate*submount);
		 list.add(c);
		 
		 return list;
	} 
	 catch (Exception e) {
		 ////////e.printStackTrace();
		 return null;
	 }
 }
 	/**
	 *  @return 501 ����������listΪ�������ֽ���ʧ��,�ֽ������������������������
	 *  @return 502 ����һ���ֽ���
	 *  @return 503���������ֽ����ĵ�һ���ֽ�����¼��ʱ�䲻�������ʱ��
	 *  @return 504��һ���ֽ�����payment��ֵ��Ϊ��
	 *  @return 505���쳣ͨ���Ǵ���Ĳ��������˳�
	 */
    public static double irr(double[] values, double guess) throws Exception{
    	int maxIterationCount = 20;
		double absoluteAccuracy = 1.0E-007D;

		double x0 = guess;

		int i = 0;
		while (i < maxIterationCount) {
			double fValue = 0.0D;
			double fDerivative = 0.0D;
			for (int k = 0; k < values.length; k++) {
				fValue += values[k] / Math.pow(1.0D + x0, k);
				fDerivative += -k * values[k] / Math.pow(1.0D + x0, k + 1);
			}
			double x1 = x0 - fValue / fDerivative;
			if (Math.abs(x1 - x0) <= absoluteAccuracy) {
				return x1;
			}
			x0 = x1;
			i++;
		}
		return 505;
    }
    
    private static double getOneValue(double payment, double rate, int dateDistance) throws Exception {
		return payment / ((Math.pow((1 + rate), dateDistance / 365f)));
	}
    
   
	private static double getXNPVByRate(double rate,List<business> list,long startDays) throws Exception {
		double result = 0;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			business date = list.get(i);
			result = result + getOneValue(date.payment, rate, (int)date.getDaysFrom1970()-(int)startDays);
		}
		return result;
	}
	////iso8859-1
    public static byte[] md5(String str) throws Exception
	{
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(str.getBytes("iso8859-1"));
		byte s[] = m.digest();
		return s;
	}
	/**
	 *  @return 501 ����������listΪ�������ֽ���ʧ��,�ֽ������������������������
	 *  @return 502 ����һ���ֽ���
	 *  @return 503���������ֽ����ĵ�һ���ֽ�����¼��ʱ�䲻�������ʱ��
	 *  @return 504��һ���ֽ�����payment��ֵ��Ϊ��
	 *  @return 505���쳣ͨ���Ǵ���Ĳ��������˳�
	 */
	public static double Xirr(List<business> list) throws Exception
	{
		double XIRR = 0;
    	long startDays = 0;/////�ֽ����ĵ�1����¼��1970��������
    	double Max_Rate=1.00;//���������
    	double Min_Rate=0.00000001;//��С������
    	double Critical=0.00000001;//��ȷֵ
    	int calculateCount = 50;/////��������
    	
    	if(list == null){
			return 501;
		}
		int count=list.size();
		if (count <= 1) {
			return 502;// ���ֻ��һ���ֽ����򷵻�Error_Less_Cash
		}
		
		if (list.get(0).payment > 0) {
			return 504;
		}
		try {
			startDays = list.get(0).getDaysFrom1970();
		} catch (Exception e) {
		}		
		for (int i = 0; i < count; i++) {
			if (list.get(i).getDaysFrom1970() < startDays) {
				return 503;// �����ֹһ���ֽ������жϵ�һ���ֽ����Ƿ�Ϊʱ������ģ�������ǵĻ��򷵻�ERROR_DATE
			}
			//////
			///System.out.println(String.valueOf(list.get(i).year)+'��'+String.valueOf(list.get(i).month)+'��'+String.valueOf(list.get(i).day));
			////System.out.println(list.get(i).payment);
		}
		boolean isEarn = getXNPVByRate(0,list,startDays) > 0;// ��¼��׬Ǯ�˻��ǿ�����
		
		double tempMax = 0;
		double tempMin = 0;
		if (isEarn) {
			tempMax = Max_Rate;
			tempMin = 0;
			while (calculateCount > 0) {
				XIRR = (tempMin + tempMax) / 2f;
				double xnvp = getXNPVByRate(XIRR,list,startDays);
				if (xnvp > 0) {
					tempMin = XIRR;
				} else {
					tempMax = XIRR;
				}
				if (Math.abs(XIRR) < Critical) {
					break;
				}
				calculateCount--;
			}
		} else {
			tempMax = 0;
			tempMin = Min_Rate;
			while (calculateCount > 0) {
				XIRR = (tempMin + tempMax) / 2f;
				double xnvp = getXNPVByRate(XIRR,list,startDays);
				if (xnvp > 0) {
 
					tempMin = XIRR;
				} else {
					tempMax = XIRR;
 
				}
				if (Math.abs(XIRR) < Critical) {
					break;
				}
				calculateCount--;
			}
		}
    	return XIRR;
	}
    /**loanXirr�洢����
	 *  @return 501 ����������listΪ�������ֽ���ʧ��,�ֽ������������������������
	 *  @return 502 ����һ���ֽ���
	 *  @return 503���������ֽ����ĵ�һ���ֽ�����¼��ʱ�䲻�������ʱ��
	 *  @return 504��һ���ֽ�����payment��ֵ��Ϊ��
	 *  @return 505���쳣ͨ���Ǵ���Ĳ��������˳� loanXirr
	 */
    
    public static void loanXirr(int dealdate,double dealamount,java.sql.Array inputpaybackdates,java.sql.Array inputpaybackcashs,double[] XIRR) throws Exception
	{
    	 XIRR[0] = 0;
		try{
			Integer[] paybackdates = (Integer [])inputpaybackdates.getArray();
			Double[] paybackcashs=(Double [])inputpaybackcashs.getArray();
			List<business> list=new CopyOnWriteArrayList<business>();
			list=cashflow(dealdate,dealamount,paybackdates, paybackcashs);
			XIRR[0] =Xirr(list);
			return;
		}
		 catch (Exception e) {
				XIRR[0] =505;
				return;
			}		
    }
	//loanXirr04 �洢���� @ inputpaybackdates �ֽ������Ľ��׷�����������,@ inputpaybackcashs �ֽ������Ľ��׷���������,@ XIRR ���ص��ֽ����ں�������
	public static void loanXirr4(java.sql.Array inputpaybackdates,java.sql.Array inputpaybackcashs,double[] XIRR) throws Exception
	{
    	 XIRR[0] = 0;
		try{
			Integer[] paybackdates = (Integer [])inputpaybackdates.getArray();
			Double[] paybackcashs=(Double [])inputpaybackcashs.getArray();
			List<business> list=new CopyOnWriteArrayList<business>();
			list=cashflow(paybackdates, paybackcashs);
			XIRR[0] =Xirr(list);
			return;
		}
		 catch (Exception e) {
				XIRR[0] =505;
				return;
			}		
    }
	
	/* 
	   public static void main(String[] args) throws Exception {
	        ////double[] income = {12,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1};
	        double ret = loanirr(280000,8645,2,4,3) ;
	        System.out.println(ret);
	        int date[]={
	        		20160115,
	        		20160415,
	        		20160715,
	        		20161015,
	        		20170115,
	        		20170415,
	        		20170715,
	        		20171015,
	        		20180115,
	        		20180415,
	        		20180715,
	        		20181015,
	        		20190115,
	        		20190415,
	        		20190715,
	        		20191015,
	        		20200115,
	        		20200415,
	        		20201008};
	        double cashs[]={
	        		197362.50,
	        		498888.54,
	        		5118888.54,
	        		5068289.17,
	        		392207.50,
	        		383681.25,
	        		5007944.38,
	        		4956125.83,
	        		280044.17,
	        		273956.25,
	        		4897000.21,
	        		4843962.50,
	        		167880.83,
	        		164231.25,
	        		4786056.04,
	        		4731799.17,
	        		55717.50,
	        		55111.88,
	        		4696590.00};
	         ret = loanXirr(20151210,-41550000.00,date,cashs) ;
			 
	         System.out.println(new BigDecimal(ret));
			 ret = loanXirr3(20190101,20200101,1,21,100000.00,4.55);
			 System.out.println(new BigDecimal(ret));
	   }
	   */
	    
}
 
	



