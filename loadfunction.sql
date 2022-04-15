-----------------------------------------------------------------------------
-- (c) Copyright IBM Corp. 2007 All rights reserved.
-- 
-- The following sample of source code ("Sample") is owned by International 
-- Business Machines Corporation or one of its subsidiaries ("IBM") and is 
-- copyrighted and licensed, not sold. You may use, copy, modify, and 
-- distribute the Sample in any form without payment to IBM, for the purpose of 
-- assisting you in the development of your applications.
-- 
-- The Sample code is provided to you on an "AS IS" basis, without warranty of 
-- any kind. IBM HEREBY EXPRESSLY DISCLAIMS ALL WARRANTIES, EITHER EXPRESS OR 
-- IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
-- MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. Some jurisdictions do 
-- not allow for the exclusion or limitation of implied warranties, so the above 
-- limitations or exclusions may not apply to you. IBM shall not be liable for 
-- any damages you suffer as a result of using, copying, modifying or 
-- distributing the Sample, even if IBM has been advised of the possibility of 
-- such damages.
-----------------------------------------------------------------------------
--
-- SOURCE FILE NAME: UDFjDrop.db2
--    
-- SAMPLE: How to uncatalog the Java UDFs contained in UDFjsrv.java 
--
-- To run this script from the CLP issue the below command:
--            "db2 -td@ -vf <script-name>"
--    where <script-name> represents the name of this script
----------------------------------------------------------------------------

connect to tapas user pas using TaiAn2014@
DROP FUNCTION loanirr@
DROP PROCEDURE loanXirr@
DROP FUNCTION loanXirr3@
DROP FUNCTION loanXirr03@
DROP PROCEDURE loanXirr03@
DROP PROCEDURE loanXirr4@
DROP TYPE INTARRAY@
DROP TYPE DOUBLEARRAY@
CREATE  TYPE INTARRAY AS INTEGER ARRAY[601]@
CREATE  TYPE DOUBLEARRAY AS double ARRAY[601]@

CREATE OR REPLACE FUNCTION loanirr(dealamount double, repayment double,repaymenttimes  Integer,frequency  Integer,repaymentmethod  Integer)
RETURNS DOUBLE
  LANGUAGE java
  PARAMETER STYLE java
  NO SQL
  FENCED THREADSAFE
  DETERMINISTIC
  RETURNS NULL ON NULL INPUT
  NO EXTERNAL ACTION
  EXTERNAL NAME 'DB2IRRUtil.loanirr'@
  
CREATE OR REPLACE FUNCTION loanXirr3(dealdate Integer,maturitydate Integer,cycle_m Integer,expirydate Integer, dealamount double,rate double)
RETURNS DOUBLE
  LANGUAGE java
  PARAMETER STYLE java
  NO SQL
  FENCED THREADSAFE
  DETERMINISTIC
  RETURNS NULL ON NULL INPUT
  NO EXTERNAL ACTION
  EXTERNAL NAME 'DB2IRRUtil.loanXirr3'@

CREATE OR REPLACE PROCEDURE loanXirr(in dealdate  Integer,in dealamount double,in inputpaybackdates INTARRAY,in inputpaybackcashs DOUBLEARRAY,out XIRR double)
  LANGUAGE java
  PARAMETER STYLE java
  NO SQL
  FENCED THREADSAFE
  DETERMINISTIC
  NO EXTERNAL ACTION
  EXTERNAL NAME 'DB2IRRUtil.loanXirr'@

CREATE PROCEDURE loanXirr4(in inputpaybackdates INTARRAY,in inputpaybackcashs DOUBLEARRAY,out XIRR double)
  LANGUAGE java
  PARAMETER STYLE java
  NO SQL
  FENCED THREADSAFE
  DETERMINISTIC
  NO EXTERNAL ACTION
  EXTERNAL NAME 'DB2IRRUtil.loanXirr4'@
  
CREATE OR REPLACE PROCEDURE test(out r double) 
BEGIN
 DECLARE d INTARRAY;
 DECLARE c DOUBLEARRAY;
	set d=array[	20151210,
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
	        		20201008];
	set c=array[	-41550000.00,
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
	        		4696590.00];
-----call IA(d,r);
---call loanXirr(20151210,-41550000.00,d,c,r) ;
call loanXirr4(d,c,r);
return;
END@
CREATE OR REPLACE FUNCTION md5 (str varchar(1000))
RETURNS CHAR(16) FOR BIT DATA
  LANGUAGE java
  PARAMETER STYLE java
  NO SQL
  FENCED THREADSAFE
  DETERMINISTIC
  RETURNS NULL ON NULL INPUT
  NO EXTERNAL ACTION
  EXTERNAL NAME 'DB2IRRUtil.md5'@
  
connect reset@