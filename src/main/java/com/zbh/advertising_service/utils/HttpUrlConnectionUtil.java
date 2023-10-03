package com.zbh.advertising_service.utils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 网络请求工具
 */
public class HttpUrlConnectionUtil {
	private final static String BOUNDARY = UUID.randomUUID().toString()
			.toLowerCase().replaceAll("-", "");// 边界标识
	private final static String PREFIX = "--";// 必须存在
	private final static String LINE_END = "\r\n";


	public static String sendGet(String url) {
		String result = "";
		PrintWriter out = null;
		BufferedReader in = null;
		HttpURLConnection conn;
		try {
			trustAllHosts();
			URL realUrl = new URL(url);
			// 通过请求地址判断请求类型(http或者是https)
			if (realUrl.getProtocol().toLowerCase().equals("https")) {
				HttpsURLConnection https = (HttpsURLConnection) realUrl.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				conn = https;
			} else {
				conn = (HttpURLConnection) realUrl.openConnection();
			}
			// 设置通用的请求属性
			conn.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			conn.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestMethod("GET");
			conn.setDoOutput(false);
			conn.setDoInput(true);
			conn.setUseCaches(false);// 是否缓存true|false
			conn.connect();// 打开连接端口
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
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

	/**
	 * post请求 传递json参数
	 * @param url
	 * @param jsonObject
	 * @return
	 */
	public static String sendPostByJson(String url, JSONObject jsonObject) {
		String result = "";
		BufferedReader in = null;
		HttpURLConnection conn;
		DataOutputStream out = null;
		try {
			trustAllHosts();
			URL realUrl = new URL(url);
			// 通过请求地址判断请求类型(http或者是https)
			if (realUrl.getProtocol().toLowerCase().equals("https")) {
				HttpsURLConnection https = (HttpsURLConnection) realUrl.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				conn = https;
			} else {
				conn = (HttpURLConnection) realUrl.openConnection();
			}
			// 设置通用的请求属性
			conn.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			conn.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 设置文件类型:
			conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			conn.setUseCaches(false);// 是否缓存true|false
			// 往服务器里面发送数据
			if (jsonObject != null && !StringUtils.isEmpty(jsonObject.toJSONString())) {
				out = new DataOutputStream(conn.getOutputStream());
				String json = jsonObject.toJSONString();
				out.write(json.getBytes("UTF-8"));
				out.flush();
				out.close();
			}
			conn.connect();// 打开连接端口
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
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

	/**
	 * 发送post请求 用表单上传参数
	 * @param url 请求接口url
	 * @param requestText 文件参数
	 * @param requestFile 文件参数
	 * @return
	 */
	public static String sendPostByForm(String url, Map<String, String> requestText, Map<String, MultipartFile> requestFile) {
		String result = "";
		BufferedReader br = null;
		HttpURLConnection conn;
		DataOutputStream out = null;
		try {
			trustAllHosts();
			URL realUrl = new URL(url);
			// 通过请求地址判断请求类型(http或者是https)
			if (realUrl.getProtocol().toLowerCase().equals("https")) {
				HttpsURLConnection https = (HttpsURLConnection) realUrl.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				conn = https;
			} else {
				conn = (HttpURLConnection) realUrl.openConnection();
			}
			// 设置通用的请求属性
			conn.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			conn.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 设置文件类型:
			conn.setUseCaches(false);// 是否缓存true|false
			// 往服务器里面发送数据
			out = new DataOutputStream(conn.getOutputStream());
			if(requestText != null && requestText.size()>0) {
				// 请求参数部分
				writeParams(requestText, out);
			}
			if(requestFile != null && requestFile.size()>0){
				// 请求上传文件部分
				writeFile(requestFile, out);
			}

			// 请求结束标志
			String endTarget = PREFIX + BOUNDARY + PREFIX + LINE_END;
			out.write(endTarget.getBytes());
			out.flush();
			conn.connect();// 打开连接端口
			// 定义BufferedReader输入流来读取URL的响应
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 对post参数进行编码处理并写入数据流中
	 * @throws Exception
	 *
	 * @throws IOException
	 *
	 * */
	private static void writeParams(Map<String, String> requestText,
									OutputStream os) throws Exception {
		try{
			String msg = "请求参数部分:\n";
			if (requestText == null || requestText.isEmpty()) {
				msg += "空";
			} else {
				StringBuilder requestParams = new StringBuilder();
				Set<Map.Entry<String, String>> set = requestText.entrySet();
				Iterator<Map.Entry<String, String>> it = set.iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> entry = it.next();
					requestParams.append(PREFIX).append(BOUNDARY).append(LINE_END);
					requestParams.append("Content-Disposition: form-data; name=\"")
							.append(entry.getKey()).append("\"").append(LINE_END);
					requestParams.append("Content-Type: text/plain; charset=utf-8")
							.append(LINE_END);
					requestParams.append("Content-Transfer-Encoding: 8bit").append(
							LINE_END);
					requestParams.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容
					requestParams.append(entry.getValue());
					requestParams.append(LINE_END);
				}
				os.write(requestParams.toString().getBytes());
				os.flush();

				msg += requestParams.toString();
			}

			System.out.println(msg);
		}catch(Exception e){
			throw new Exception(e);
		}
	}


	/**
	 * 对post上传的文件进行编码处理并写入数据流中
	 *
	 * @throws IOException
	 *
	 * */
	private static void writeFile(Map<String, MultipartFile> requestFile,
								  OutputStream os) throws Exception {
		InputStream is = null;
		try{
			String msg = "请求上传文件部分:\n";
			if (requestFile == null || requestFile.isEmpty()) {
				msg += "空";
			} else {
				StringBuilder requestParams = new StringBuilder();
				Set<Map.Entry<String, MultipartFile>> set = requestFile.entrySet();
				Iterator<Map.Entry<String, MultipartFile>> it = set.iterator();
				while (it.hasNext()) {
					Map.Entry<String, MultipartFile> entry = it.next();
					requestParams.append(PREFIX).append(BOUNDARY).append(LINE_END);
					requestParams.append("Content-Disposition: form-data; name=\"")
							.append(entry.getKey()).append("\"; filename=\"")
							.append(entry.getValue().getName()).append("\"")
							.append(LINE_END);
					requestParams.append("Content-Type:")
							.append(entry.getValue().getContentType())
							.append(LINE_END);
					requestParams.append("Content-Transfer-Encoding: 8bit").append(
							LINE_END);
					requestParams.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容

					os.write(requestParams.toString().getBytes());

					is = (entry.getValue().getInputStream());

					byte[] buffer = new byte[1024*1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						os.write(buffer, 0, len);
					}
					os.write(LINE_END.getBytes());
					os.flush();

					msg += requestParams.toString();
				}
			}
			System.out.println(msg);
		}catch(Exception e){
			throw new Exception(e);
		}finally{
			try{
				if(is!=null){
					is.close();
				}
			}catch(Exception e){
				throw new Exception(e);
			}
		}
	}




	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}
		} };
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	private static String getResult(String urlStr, String content, String encoding) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例
			connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod("POST");// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
			connection.connect();// 打开连接端口
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());// 打开输出流往对端服务器写数据
			out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			out.flush();// 刷新
			out.close();// 关闭输出流
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
			// ,以BufferedReader流来读取
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}
	
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

}
