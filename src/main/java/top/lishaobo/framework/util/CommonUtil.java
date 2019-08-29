package top.lishaobo.framework.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

	public static boolean isZeroLengthTrimString(String value) {
		return value == null || value.trim().length() == 0;
	}

	public static boolean isNotZeroLengthTrimString(String value) {
		return !(isZeroLengthTrimString(value));
	}

	public static String getParamValue(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (isZeroLengthTrimString(value)) {
			return "";
		}
		return value;
	}

	public static Date getAddDate(String yyyy_MM_DD, int days) {
		if (yyyy_MM_DD == null) {
			return new Date();
		}
		String[] calendar = yyyy_MM_DD.split("-");
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Integer.parseInt(calendar[0]), Integer.parseInt(calendar[1]) - 1, Integer.parseInt(calendar[2]), 0, 0,
				0);
		gc.add(5, days);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return java.sql.Date.valueOf(sdf.format(gc.getTime()));
	}

	public static String getIdSQLParam(Long[] ids) {
		if (ids == null || ids.length == 0) {
			return null;
		}
		StringBuffer param = new StringBuffer("");
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				param.append(",");
			}
			param.append("?");
		}
		return param.toString();
	}


	public static String getIdSQLValue(Long[] ids) {
		if (ids == null || ids.length == 0) {
			return null;
		}
		StringBuffer param = new StringBuffer("");
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				param.append(",");
			}
			param.append(String.valueOf(ids[i]));
		}
		return param.toString();
	}

	public static String getArrSQLValue(Integer[] values) {
		if (values == null || values.length == 0) {
			return null;
		}
		StringBuffer param = new StringBuffer("");
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				param.append(",");
			}
			param.append(String.valueOf(values[i]));
		}
		return param.toString();
	}

	public static String getArrSQLValue(List<String> values) {
		if (values == null || values.size() == 0) {
			return null;
		}
		StringBuffer param = new StringBuffer("");
		for (int i = 0; i < values.size(); i++) {
			if (i > 0) {
				param.append(",");
			}
			param.append("'"+values.get(i)+"'");
		}
		return param.toString();
	}


	public static SimpleDateFormat getSampleDateFormat(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf;
	}

	public static String getURL(String url, String menuId, String method) {
		return url + "?actionMethod=" + method + "&menuId=" + menuId;
	}

	public static String getDateFormatString(Date date, String format){
		if(date == null) return "";
		SimpleDateFormat simpleDateFormat = getSampleDateFormat(format);
		String dateString = simpleDateFormat.format(date);
		return dateString;
	}

	/**
	 * 密码加密的方法
	 *
	 * @param password
	 * @return
	 */
	public static String encrypt(String password) {
		String enStr = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update("meeno".getBytes());
			byte[] pswKey = md.digest();
			DESKeySpec desKeySpec = new DESKeySpec(pswKey);
			SecretKeyFactory desKeyFac = SecretKeyFactory.getInstance("DES");
			SecretKey desKey = desKeyFac.generateSecret(desKeySpec);
			Cipher encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, desKey);
			byte[] enPassword = encryptCipher.doFinal(password.getBytes("UTF8"));
			enStr = new BASE64Encoder().encode(enPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enStr;
	}

	/**
	 * 32位md5加密方法
	 *
	 * @param sourceStr
	 * @return
	 */
	public static String MD5_32(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			System.out.println("MD5(" + sourceStr + ",32) = " + result);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}

	/**
	 * 中文乱码转换
	 *
	 * @param str
	 * @return
	 */
	public static String encodeStr(String str) {
		try {
			str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
			String retValue = str;
			return retValue;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 向客户端浏览器输出字符串.
	 *
	 * @param response
	 * @param bean
	 */
	public static void toJson(HttpServletResponse response, Object bean) {
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter()
					.write(JSON.toJSONString(bean, SerializerFeature.WriteDateUseDateFormat,
							SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue,
							SerializerFeature.WriteNullStringAsEmpty));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param bean
	 */
	public static String toJson(Object bean) {

		return JSON.toJSONString(bean, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty);
	}

	/**
	 * 生成随机字符串
	 *
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 生成随机数字字符串
	 *
	 * @param length
	 * @return
	 */
	public static String getRandomNumString(int length) { // length表示生成字符串的长度
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 判断字符串是否匹配正则表达式.
	 *
	 * @param aimStr
	 *            目标字符串
	 * @param regex
	 *            正则表达式
	 * @return 匹配结果(true:匹配, false:不匹配)
	 **/
	public static boolean getMatchResult(final String aimStr, final String regex) {
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(aimStr);
			return matcher.matches();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	/**
	 * 将json数组转化为Long型
	 *
	 * @param jsonArray
	 * @return
	 */
	public static Long[] getJsonToLongArray(JSONArray jsonArray) {
		try {
			Long[] arr = new Long[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); i++) {
				arr[i] = jsonArray.getLong(i);
			}
			return arr;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将json数组转化为Long型
	 *
	 * @param jsonArray
	 * @return
	 */
	public static List<Long> getJsonToLongList(JSONArray jsonArray) {
		try {
			List<Long> list = new ArrayList<>();
			for (int i = 0; i < jsonArray.size(); i++) {
				list.add(jsonArray.getLong(i));
			}
			return list;
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 将json数组转化为Integer型
	 *
	 * @param jsonArray
	 * @return
	 */
	public static Integer[] getJsonToIntegerArray(JSONArray jsonArray) {
		try {
			Integer[] arr = new Integer[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); i++) {
				arr[i] = jsonArray.getInteger(i);
			}
			return arr;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将json数组转化为String型
	 *
	 * @param jsonArray
	 * @return
	 */
	public static String[] getJsonToStringArray(JSONArray jsonArray) {
		try {
			String[] arr = new String[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); i++) {
				arr[i] = jsonArray.getString(i);
			}
			return arr;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 检查字符串是否为null，为null则返回空字符串
	 * @param src
	 * @return
	 */
	public static String replaceNullWithEmptyStr(String src){
		if (src == null) {
			return "";
		}
		return src;
	}

	/**
	 * URL编码（utf-8）
	 *
	 * @param source
	 * @return
	 */
	public static String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static <T> List<T> subList(List<T> list, Integer pageIndex, Integer pageSize){
		if ((list == null) || (list.size() == 0)) {
			return list;
		}
		if ((pageIndex != null) && (pageSize != null)) {
			//需要分页
			if ((pageIndex - 1)*pageSize > (list.size() - 1)) {
				//起始位置已越界
				return list;
			}else {
				int startIndex = (pageIndex - 1)*pageSize;
				int endIndex = (pageIndex*pageSize > list.size()) ? list.size() : pageIndex*pageSize;
				return list.subList(startIndex, endIndex);
			}
		}
		return list;
	}

	/**
	 * 获取发起请求的客户端地址
	 * @param request
	 * @return
	 */
	public static String getRemoteHost(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}

	/**
	 * 获取匿名name
	 * @param name
	 * @return
	 */
	public static String getAnonymousName(String name){
		try {
			if (CommonUtil.isZeroLengthTrimString(name)) return "";
			String x = "***";
			if (name.length() == 1) {
				return name + x;
			}
			StringBuffer an  = new StringBuffer();
			an.append(name.charAt(0))
					.append(x)
					.append(name.charAt(name.length() - 1));
			return an.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param preDate
	 * @param nowDate
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date preDate, Date nowDate) {
		Long between_days = -1L;
		try {
			Calendar calendar = new GregorianCalendar();
			int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
			Long days1 = (preDate.getTime() + zoneOffset) / (1000 * 3600 * 24);
			Long days2 = (nowDate.getTime() + zoneOffset) / (1000 * 3600 * 24);
			between_days = days2 - days1;
		} catch (Exception e) {
			between_days = -1L;
		}
		return between_days.intValue();
	}

	public static List<Long> getStringArray(String str) {
		try {
			if (StringUtils.isEmpty(str)) {
				return null;
			}
			List<Long> list = new ArrayList<>();
			String[] split = str.split(",");
			for (String i : split) {
				list.add(Long.valueOf(i));
			}
			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getLongListParam(List<Long> values) {
		if (values == null || values.size() == 0) {
			return null;
		}
		StringBuffer param = new StringBuffer("");
		for (int i = 0; i < values.size(); i++) {
			if (i > 0) {
				param.append(",");
			}
			param.append(values.get(i));
		}
		return param.toString();
	}

	public static String getIntegerArrStr(List<Integer> values) {
		if (values == null || values.size() == 0) {
			return null;
		}
		StringBuffer param = new StringBuffer("");
		for (int i = 0; i < values.size(); i++) {
			if (i > 0) {
				param.append(",");
			}
			param.append(values.get(i));
		}
		return param.toString();
	}


	public static String getAnswerString(String[] values){
		if(values == null || values.length == 0){
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < values.length ; i++){
			if(i > 0){
				buffer.append(",");
			}
			buffer.append(values[i]);
		}
		return buffer.toString();
	}

	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + "?" + param;
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有的响应头字段
           /* for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }*/
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				// 定义BufferedReader输入流来读取URL的响应
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 *
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String sendPost(String url, String param) {
		BufferedReader in = null;
		String result = "";
		DataOutputStream os = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			os = new DataOutputStream(conn.getOutputStream());
			// 发送请求参数

			os.write(param.getBytes("utf-8"));
			// flush输出流的缓冲
			os.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));

			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println("结果集："+result);
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (os != null) {
					os.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String getStringRandom(int length) {

		String val = "";
		Random random = new Random();
		String substring = String.valueOf(new Date().getTime()).substring(10);
		//参数length，表示生成几位随机数
		for(int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			//输出字母还是数字
			if( "char".equalsIgnoreCase(charOrNum) ) {
				//输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char)(random.nextInt(26) + temp);
			} else if( "num".equalsIgnoreCase(charOrNum) ) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val+"_"+substring;
	}

	/**
	 * 获取当天开始和结束时间
	 * @param date
	 */
    public static List<Date> getTodayStartAndEndTimestamp(Date date) {
    	if(date == null) return null;

		try {
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String formatDate = format.format(date);

		Date start = format.parse(formatDate);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);

		Date end = calendar.getTime();

		List<Date> list = new ArrayList<>();
		list.add(start);
		list.add(end);

		return list;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }

	/**
	 * 获取本周始末时间
	 * @throws ParseException
	 */
	public static List<Date> getWeekStartAndEndDate() {
		Date firstDate = getMondayOFWeek();
		Date lastDate = getCurrentWeekday();
		List<Date> list = new ArrayList<>();
		list.add(firstDate);
		list.add(lastDate);
		return list;
	}

	public static Date getMondayOFWeek() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		currentDate.set(GregorianCalendar.HOUR_OF_DAY, 0);
		currentDate.set(GregorianCalendar.MINUTE, 0);
		currentDate.set(GregorianCalendar.SECOND, 0);
		currentDate.set(GregorianCalendar.MILLISECOND, 0);
		Date monday = currentDate.getTime();
		return monday;
	}

	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		//获得今天是一周的第几天，星期日是第一天，星期二是第二天
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	public static Date getCurrentWeekday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();

		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		currentDate.set(GregorianCalendar.HOUR_OF_DAY, 23);
		currentDate.set(GregorianCalendar.MINUTE, 59);
		currentDate.set(GregorianCalendar.SECOND, 59);
		currentDate.set(GregorianCalendar.MILLISECOND, 999);

		Date monday = currentDate.getTime();

		return monday;
	}

	/**
	 * 计算完成百分比
	 * @param relCount
	 * @param count
	 * @return
	 */
	public static String getCalculateCompletionPercentage(Integer relCount, Integer count){

		DecimalFormat df = new DecimalFormat("0%");
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.HALF_UP);


		double passRate = relCount*1.0 / count*1.0;

		if(Double.isNaN(passRate)){
			passRate = 0;
		}

		String rate = df.format(passRate);

		return rate;
	}

	/**
	 * 计算每个所占
	 * @param relCount
	 * @param count
	 * @return
	 */
	public static double getPerRate(Integer relCount, Integer count){

		double passRate = relCount*1.0 / count*1.0;

		if(Double.isNaN(passRate)){
			passRate = 0;
		}

		return passRate;
	}

	/**
	 * 转化为百分比
	 * @param passRate
	 * @return
	 */
	public static String getPerRateDecimal(Double passRate){

		passRate = passRate/100;

		DecimalFormat df = new DecimalFormat("0%");
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.HALF_UP);

		String rate = df.format(passRate);
		return rate;
	}

	/**
	 *
	 */
	public static List<Date> getYesterday(){
		List<Date> list = new ArrayList<>();
		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.DATE,   -1);
		cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
		cal.set(GregorianCalendar.MINUTE, 0);
		cal.set(GregorianCalendar.SECOND, 0);
		cal.set(GregorianCalendar.MILLISECOND, 0);
		Date timeStart = cal.getTime();
		System.out.println(timeStart);

		list.add(timeStart);

		cal.set(GregorianCalendar.HOUR_OF_DAY, 23);
		cal.set(GregorianCalendar.MINUTE, 59);
		cal.set(GregorianCalendar.SECOND, 59);
		cal.set(GregorianCalendar.MILLISECOND, 999);
		Date timeEnd = cal.getTime();
		System.out.println(timeEnd);
		list.add(timeEnd);
		return list;
	}


	/**
	 * 过滤Emoji表情
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {
		if (source != null && source.length() > 0) {
			return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
		} else {
			return source;
		}
	}

	/**
	 * 百分比计算分数
	 */
	public static String getResultScore(String percent, Integer score){
		NumberFormat nf=NumberFormat.getPercentInstance();
		Number parse = null;
		try {
			parse = nf.parse(percent);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Double result = score.doubleValue() * parse.doubleValue();
		DecimalFormat df = new DecimalFormat("#");
		return df.format(result);
	}

	/**
	 * 获取中文星期
	 * @param index
	 * @return
	 */
	public static String getTodayFormatChinese(int index){
		String [] chineseArr = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
		return chineseArr[index];
	}

	/**
	 * excel题库生成选项
	 * @param index
	 * @return
	 */
	public static String getRevolution(Integer index){

		String [] code = new String[]{
				"01","02","03","04","A","B","C","D","E","F","G","H","I","J","K",
				"L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
		};

		return code[index];
	}

	/**
	 * Description: 根据传入日期得到本月月初
	 * @param
	 * @return Date
	 */
	public static Date getFirstDateOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * Description: 根据传入日期得到本月月末
	 * @param
	 * @return Date
	 */
	public static Date getLastDateOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 封装分页数据
	 */
	public static Map<String, Object> getResultData(Page page, Object resultList){
		Map<String, Object> resultData = new HashMap<>();
		resultData.put("totalCount", page.getTotalElements());
		resultData.put("list", resultList);
		return resultData;
	}

	/**
	 * 计算时间差
	 * @param endDate
	 * @param startDate
	 * @return
	 */
	public static String getDatePoor(Date endDate, Date startDate) {

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - startDate.getTime();

		if(diff / nd >= 2){
			return diff / nd + "天";
		}else {
			return diff / nh + "小时";
		}
	}


	/**
	 * 生成盐
	 */
	public static String createSalt(){
		Random ranGen = new SecureRandom();
		byte[] aesKey = new byte[8];
		ranGen.nextBytes(aesKey);
		StringBuffer salt = new StringBuffer();
		for (int i = 0; i < aesKey.length; i++) {
			String hex = Integer.toHexString(0xff & aesKey[i]);
			if (hex.length() == 1)
				salt.append('0');
			salt.append(hex);
		}
		return salt.toString();
	}

	public static int TYPE_START = 1;
	public static int TYPE_END = 2;

	public static Date getStartOrEndTimestamp(int type, Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String formatDate = format.format(date);
		Date result = null;
		try {
			switch (type){
				//初时间
				case 1:
					result = format.parse(formatDate);
					break;
				//末时间
				case 2:
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					calendar.add(Calendar.SECOND, -1);
					result = calendar.getTime();
					break;
			}

			return result;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * list反转
	 * @param list
	 */
	public static <T> List<T> reverseList(List<T> list) {
		if (list == null) return null;
		List<T> tmpList = new ArrayList<>();
		if (list.size() == 0) return tmpList;
		for (int i = list.size() - 1; i >= 0; i--) {
			tmpList.add(list.get(i));
		}
		return tmpList;
	}
}