package com.example.test8;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class JsonLoader extends AsyncTask<String,MainActivity, String>{

	MainActivity _teAct;
	ReadBarcode_Activity _c_Act;
	ProgressDialog _pd;
	Boolean c_flg = false;
	Boolean m_flg = false;

	public JsonLoader(String _str1,MainActivity activity, Boolean flg){
		_teAct = activity;
		m_flg = flg;
	}

	public JsonLoader(String _str1,ReadBarcode_Activity activity, Boolean flg) {  
		_c_Act = activity;
		c_flg = flg;
	}

	@Override  
	protected void onPreExecute() {
		// プログレスバーを表示する  

		if(c_flg){
			_pd = new ProgressDialog(_c_Act);  
		}else if(m_flg){
			_pd = new ProgressDialog(_teAct);  
		}
		_pd.setMessage("Now Loading...");  
		_pd.show();
	}



	@Override  
	protected void onPostExecute(String result) {
		_pd.dismiss();  
		
		Log.d("Jsonこれ",First_Activity.preference.getString("regist", "erorr"));
		if(c_flg){
			s_perthJson(result);
			_c_Act.moveActivity();
		}
		if(m_flg){
			b_perthJson(result);
			_teAct.getBarcodeImage();
		}
	}

	private void b_perthJson(String result) {
		
		try {
			JSONObject jsRoot = new JSONObject(result);

			Log.d("Json",jsRoot.getString("point_amount"));

				MainActivity.JSON_CHECK_NUM = jsRoot.getString("point_amount");
				First_Activity.editor.putString("p_amount", jsRoot.getString("point_amount"));
				First_Activity.editor.commit();
				Log.d("Json",jsRoot.getString("point_amount"));
			
		} catch (JSONException e) {
			//e.printStackTrace();
			Log.d("Json","jsonのパースに失敗");
		}
		
		try {
			JSONObject jsRoot = new JSONObject(result);

			Log.d("Json",jsRoot.getString("barcode_url"));

				MainActivity.JSON_CHECK_NUM = jsRoot.getString("barcode_url");
				First_Activity.editor.putString("image_url", jsRoot.getString("barcode_url"));
				First_Activity.editor.commit();
				Log.d("Json",jsRoot.getString("barcode_url"));
			
		} catch (JSONException e) {
			//e.printStackTrace();
			Log.d("Json","jsonのパースに失敗");
		}
	}

	private void s_perthJson(String result) {

		try {
			JSONObject jsRoot = new JSONObject(result);
			MainActivity.JSON_CHECK_NUM = jsRoot.getString("registed");
			Log.d("Json",jsRoot.getString("registed"));
			if(jsRoot.getString("registed").equals("1")){
				MainActivity.JSON_CHECK_NUM = jsRoot.getString("registed");
				First_Activity.editor.putString("regist", jsRoot.getString("registed"));
				First_Activity.editor.commit();
				Log.d("Json",jsRoot.getString("registed"));
			}
		} catch (JSONException e) {
			//e.printStackTrace();
			Log.d("Json","jsonのパースに失敗");
		}
	}

	/** 
	 * 指定URLからgetした文字列を取得する 
	 * @param sUrl 
	 * @return 
	 */  
	public String getData(String sUrl) {
		HttpClient objHttp = new DefaultHttpClient();
		HttpParams params = objHttp.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 1000); //接続のタイムアウト 
		HttpConnectionParams.setSoTimeout(params, 1000); //データ取得のタイムアウト 
		String sReturn = "";
		try {
			HttpGet objGet   = new HttpGet(sUrl);  
			HttpResponse objResponse = objHttp.execute(objGet);
			if (objResponse.getStatusLine().getStatusCode() < 400){
				InputStream objStream = objResponse.getEntity().getContent();
				InputStreamReader objReader = new InputStreamReader(objStream);
				BufferedReader objBuf = new BufferedReader(objReader);
				StringBuilder objJson = new StringBuilder();
				String sLine;
				while((sLine = objBuf.readLine()) != null){
					objJson.append(sLine);
				}
				sReturn = objJson.toString();
				objStream.close();
			}
		} catch (IOException e) {
			return null;
		}
		return sReturn;
	}

	/** 
	 * 指定URLからpostした文字列を取得する 
	 * @param sUrl 送信先URL 
	 * @param sJson 文字列に変換したJSONデータ 
	 * @return 
	 */  
	public String postJsonData(String sUrl, String sJson) {  
		HttpClient objHttp = new DefaultHttpClient();
		HttpParams params = objHttp.getParams();  
		HttpConnectionParams.setConnectionTimeout(params, 1000); //接続のタイムアウト  
		HttpConnectionParams.setSoTimeout(params, 1000); //データ取得のタイムアウト  
		String sReturn = "";  
		try {  
			HttpPost objPost   = new HttpPost(sUrl);  
			List<NameValuePair> objValuePairs = new ArrayList<NameValuePair>(2);    
			objValuePairs.add(new BasicNameValuePair("json", sJson));  
			objPost.setEntity(new UrlEncodedFormEntity(objValuePairs, "UTF-8"));  

			HttpResponse objResponse = objHttp.execute(objPost);  
			if (objResponse.getStatusLine().getStatusCode() < 400){  
				InputStream objStream = objResponse.getEntity().getContent();  
				InputStreamReader objReader = new InputStreamReader(objStream);  
				BufferedReader objBuf = new BufferedReader(objReader);  
				StringBuilder objJson = new StringBuilder();  
				String sLine;  
				while((sLine = objBuf.readLine()) != null){  
					objJson.append(sLine);  
				}  
				sReturn = objJson.toString();  
				objStream.close();  
			}  
		} catch (IOException e) {  
			return null;  
		}     
		return sReturn;  
	}  


	@Override
	protected String doInBackground(String... params) {
		String _str= getData(params[0]);
		return _str;
	}
}
