del D:\db2_JAVA_IRRXIRR\business.class
del D:\db2_JAVA_IRRXIRR\DB2IRRUtil.class
echo "��ʼ����business.java"
D:\TaiAnPas\jdk1.6\jdk1.6.0_10\bin\javac D:\db2_JAVA_IRRXIRR\business.java
echo "��ʼ����DB2IRRUtil.java"
D:\TaiAnPas\jdk1.6\jdk1.6.0_10\bin\javac -cp C:\PROGRA~1\IBM\SQLLIB\java\db2jcc.jar;D:\db2_JAVA_IRRXIRR D:\db2_JAVA_IRRXIRR\DB2IRRUtil.java
copy D:\db2_JAVA_IRRXIRR\business.class C:\PROGRA~1\IBM\SQLLIB\FUNCTION\business.class
copy D:\db2_JAVA_IRRXIRR\DB2IRRUtil.class C:\PROGRA~1\IBM\SQLLIB\FUNCTION\DB2IRRUtil.class
echo "��ʼ��DB2װ��java����"
db2 -td@ -vf D:\db2_JAVA_IRRXIRR\loadfunction.sql