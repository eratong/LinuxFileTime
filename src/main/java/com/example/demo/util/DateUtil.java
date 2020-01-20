package com.example.demo.util;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/** yyyy-MM-dd HH:mm:ss **/
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** yyyy-MM-dd **/
	private static final String DAY_FORMAT = "yyyy-MM-dd";
	
	public static Date parseDate(String date) {
		return parse(date, DEFAULT_FORMAT);
	}

	public static Date parseDateByDay(String date) {
		return parse(date, DAY_FORMAT);
	}
	
	public static Date parse(String date, String format) {
		if(StringUtils.isEmpty(date)) {
			return null;
		}
		
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String get4Ymd(Date date) {
		return format(date, DEFAULT_FORMAT);
	}
	
	public static String getDay(Date date) {
		return format(date, DAY_FORMAT);
	}

	public static String format(Date date, String format) {
		if(date == null) {
			return null;
		}
		
		try {
			return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date addDay(Date date, int i) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(Calendar.DATE, i);
		return calendar.getTime();
	}
	
	public static Date addMinuter(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(Calendar.MINUTE, i);
		return calendar.getTime();
	}

	public static Date addHour(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, i);
		return calendar.getTime();
	}

	public static Date addSecond(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, i);
		return calendar.getTime();
	}
	
	public static Date min(Date date, Date when) {
		return date.before(when)?date:when;
	}
	
	
	public static int subDay(Date date1, Date date2) {
		return (int) (getFirstSecOfDay(date1).getTime() - getFirstSecOfDay(date2).getTime())/(24*60*60*1000);
	}
	
	
	private static Date getFirstSecOfDay(Date date1) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date1);
		
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		
		return ca.getTime();
	}
	
	public static Date getLastSecOfDay(Date date1) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date1);
		
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		ca.set(Calendar.MILLISECOND, 0);
		
		return ca.getTime();
	}
	
	public static Date getFiristDayOfMonth(Date day) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(day);
		ca.set(Calendar.DATE, 1);
		
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		
		return ca.getTime();
	}
	
	public static Date getLastDayOfMonth(Date day) {
		Calendar ca2 = Calendar.getInstance();
		ca2.setTime(new Date());
		ca2.add(Calendar.MONTH, 1);
		
		
		ca2.set(Calendar.DATE, 1);
		ca2.set(Calendar.HOUR_OF_DAY, 0);
		ca2.set(Calendar.MINUTE, 0);
		ca2.set(Calendar.SECOND, 0);
		ca2.set(Calendar.MILLISECOND, 0);
		ca2.add(Calendar.SECOND, -1);
		return ca2.getTime();
	}
}
