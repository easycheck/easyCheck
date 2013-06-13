package com.molihugo.easycheck.apis.sugar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class SugarConnection {
	/**
     * Log the user into the application
     *
     * @param UserAuth array user_auth -- Set user_name and password (password needs to be
     *      in the right encoding for the type of authentication the user is setup for.  For Base
     *      sugar validation, password is the MD5 sum of the plain text password.
     * @param String application -- The name of the application you are logging in from.  (Currently unused).
     * @param array name_value_list -- Array of name value pair of extra parameters. As of today only 'language' and 'notifyonsave' is supported
     * @return Array - id - String id is the session_id of the session that was created.
     * 				 - module_name - String - module name of user
     * 				 - name_value_list - Array - The name value pair of user_id, user_name, user_language, user_currency_id, user_currency_name,
     *                                         - user_default_team_id, user_is_admin, user_default_dateformat, user_default_timeformat
     * @exception 'SoapFault' -- The SOAP error, if any
     */
	 public static String login(String username,String password) throws NoSuchAlgorithmException, ClientProtocolException, IOException {
   

   MessageDigest md5 = MessageDigest.getInstance("MD5");
   String passwordHash = new BigInteger(1, md5.digest(password.getBytes()))
       .toString(16);

   // the order is important, so use a ordered map
   Map<String, String> userCredentials = new LinkedHashMap<String, String>();  
   userCredentials.put("user_name", username);
   userCredentials.put("password", passwordHash);
   userCredentials.put("version", "1");

   // the order is important, so use a ordered map
   Map<String, Object> request = new LinkedHashMap<String, Object>();
   request.put("user_auth", userCredentials);
   request.put("application_name", Constants.APPLICATION_NAME);

   MultipartEntity multipartEntity = new MultipartEntity();
   multipartEntity.addPart("method", new StringBody(Constants.LOGIN));
   // define request encoding
   multipartEntity.addPart("input_type", new StringBody(Constants.JSON));
   // define response encoding
   multipartEntity.addPart("response_type", new StringBody(Constants.JSON));
   multipartEntity.addPart("rest_data",
       new StringBody(JSONObject.toJSONString(request)));

   // yourSugarCRM has to be changed to your SugarCRM instance
   // something like localhost/sugarcrm
   
   HttpPost httpPost = new HttpPost(Constants.URL);
   httpPost.setEntity(multipartEntity);

   DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
   HttpResponse execute = defaultHttpClient.execute(httpPost);

   HttpEntity entity = execute.getEntity();
   
   JSONObject parse = (JSONObject) JSONValue.parse(new InputStreamReader(
       entity.getContent()));
   return (String) parse.get(Constants.ID);
 }
	 /**
	     * Retrieve a list of beans.  This is the primary method for getting list of SugarBeans from Sugar using the SOAP API.
	     *
	     * @param String session -- Session ID returned by a previous call to login.
	     * @param String module_name -- The name of the module to return records from.  This name should be the name the module was developed under (changing a tab name is studio does not affect the name that should be passed into this method)..
	     * @param String query -- SQL where clause without the word 'where'
	     * @param String order_by -- SQL order by clause without the phrase 'order by'
	     * @param integer offset -- The record offset to start from.
	     * @param Array  select_fields -- A list of the fields to be included in the results. This optional parameter allows for only needed fields to be retrieved.
	     * @param Array link_name_to_fields_array -- A list of link_names and for each link_name, what fields value to be returned. For ex.'link_name_to_fields_array' => array(array('name' =>  'email_addresses', 'value' => array('id', 'email_address', 'opt_out', 'primary_address')))
	    * @param integer max_results -- The maximum number of records to return.  The default is the sugar configuration value for 'list_max_entries_per_page'
	     * @param integer deleted -- false if deleted records should not be include, true if deleted records should be included.
	     * @return Array 'result_count' -- integer - The number of records returned
	     *               'next_offset' -- integer - The start of the next page (This will always be the previous offset plus the number of rows returned.  It does not indicate if there is additional data unless you calculate that the next_offset happens to be closer than it should be.
	     *               'entry_list' -- Array - The records that were retrieved
	     *	     		 'relationship_list' -- Array - The records link field data. The example is if asked about accounts email address then return data would look like Array ( [0] => Array ( [name] => email_addresses [records] => Array ( [0] => Array ( [0] => Array ( [name] => id [value] => 3fb16797-8d90-0a94-ac12-490b63a6be67 ) [1] => Array ( [name] => email_address [value] => hr.kid.qa@example.com ) [2] => Array ( [name] => opt_out [value] => 0 ) [3] => Array ( [name] => primary_address [value] => 1 ) ) [1] => Array ( [0] => Array ( [name] => id [value] => 403f8da1-214b-6a88-9cef-490b63d43566 ) [1] => Array ( [name] => email_address [value] => kid.hr@example.name ) [2] => Array ( [name] => opt_out [value] => 0 ) [3] => Array ( [name] => primary_address [value] => 0 ) ) ) ) )
	    * @exception 'SoapFault' -- The SOAP error, if any
	    */
	 public static String getRecords(String session,String module_name,String query,String order_by,
				Integer offset,Integer max_results,Integer deleted) throws NoSuchAlgorithmException, ClientProtocolException, IOException{
				
			Map<String, Object> select_fields = new LinkedHashMap<String, Object>();
			Map<String, Object> link_name_to_fields_array = new LinkedHashMap<String, Object>();
			
			
			Map<String, Object> request = new LinkedHashMap<String, Object>();
			request.put("session", session);
			request.put("module_name", module_name);
			request.put("query", query);
			request.put("order_by", order_by);
			request.put("offset", offset);
			request.put("select_fields", select_fields);
			request.put("link_name_to_fields_array", link_name_to_fields_array);
			request.put("max_results", max_results);
			request.put("deleted", deleted);
			   
			   MultipartEntity multipartEntity = new MultipartEntity();
			   multipartEntity.addPart("method", new StringBody(Constants.GET));
			   // define request encoding
			   multipartEntity.addPart("input_type", new StringBody(Constants.JSON));
			   // define response encoding
			   multipartEntity.addPart("response_type", new StringBody(Constants.JSON));
			   multipartEntity.addPart("rest_data",
			       new StringBody(JSONObject.toJSONString(request)));

			   // yourSugarCRM has to be changed to your SugarCRM instance
			   // something like localhost/sugarcrm
			   
			   HttpPost httpPost = new HttpPost(Constants.URL);
			   httpPost.setEntity(multipartEntity);

			   DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			   HttpResponse execute = defaultHttpClient.execute(httpPost);

			   HttpEntity entity = execute.getEntity();
			   
			   JSONObject parse = (JSONObject) JSONValue.parse(new InputStreamReader(
			       entity.getContent()));
			   return (String) parse.toJSONString();//  get("result_count");
			
		}

	    /**
	     * Retrieve a collection of beans that are related to the specified bean and optionally return relationship data for those related beans.
	     * So in this API you can get contacts info for an account and also return all those contact's email address or an opportunity info also.
	     *
	     * @param String  session -- Session ID returned by a previous call to login.
	     * @param String  module_name -- The name of the module that the primary record is from.  This name should be the name the module was developed under (changing a tab name is studio does not affect the name that should be passed into this method)..
	     * @param String  module_id -- The ID of the bean in the specified module
	     * @param String  link_field_name -- The name of the lnk field to return records from.  This name should be the name the relationship.
	     * @param String  related_module_query -- A portion of the where clause of the SQL statement to find the related items.  The SQL query will already be filtered to only include the beans that are related to the specified bean.
	     * @param Array  related_fields - Array of related bean fields to be returned.
	     * @param Array  related_module_link_name_to_fields_array - For every related bean returrned, specify link fields name to fields info for that bean to be returned. For ex.'link_name_to_fields_array' => array(array('name' =>  'email_addresses', 'value' => array('id', 'email_address', 'opt_out', 'primary_address'))).
	     * @param Number  deleted -- false if deleted records should not be include, true if deleted records should be included.
	     * @param String  order_by -- field to order the result sets by
	     * @return Array 'entry_list' -- Array - The records that were retrieved
	     *	     		 'relationship_list' -- Array - The records link field data. The example is if asked about accounts contacts email address then return data would look like Array ( [0] => Array ( [name] => email_addresses [records] => Array ( [0] => Array ( [0] => Array ( [name] => id [value] => 3fb16797-8d90-0a94-ac12-490b63a6be67 ) [1] => Array ( [name] => email_address [value] => hr.kid.qa@example.com ) [2] => Array ( [name] => opt_out [value] => 0 ) [3] => Array ( [name] => primary_address [value] => 1 ) ) [1] => Array ( [0] => Array ( [name] => id [value] => 403f8da1-214b-6a88-9cef-490b63d43566 ) [1] => Array ( [name] => email_address [value] => kid.hr@example.name ) [2] => Array ( [name] => opt_out [value] => 0 ) [3] => Array ( [name] => primary_address [value] => 0 ) ) ) ) )
	    * @exception 'SoapFault' -- The SOAP error, if any
	    */
	 public static String getRelationships(String session,String module_name,String module_id,String link_field_name, String related_module_query,
		Integer deleted,String order_by) throws NoSuchAlgorithmException, ClientProtocolException, IOException{
				
			ArrayList<String> related_fields = new ArrayList<String>();
			Map<String, Object> related_module_link_name_to_fields_array = new LinkedHashMap<String, Object>();
			
			related_fields.add("id");
			related_fields.add("name");
			Map<String, Object> request = new LinkedHashMap<String, Object>();
			request.put("session", session);
			request.put("module_name", module_name);
			request.put("module_id", module_id);
			request.put("link_field_name", link_field_name);
			request.put("related_module_query", related_module_query);
			request.put("related_fields", related_fields);
			request.put("related_module_link_name_to_fields_array", related_module_link_name_to_fields_array);
			request.put("deleted", deleted);
			request.put("order_by", order_by);
			   
			   MultipartEntity multipartEntity = new MultipartEntity();
			   multipartEntity.addPart("method", new StringBody(Constants.GETREL));
			   // define request encoding
			   multipartEntity.addPart("input_type", new StringBody(Constants.JSON));
			   // define response encoding
			   multipartEntity.addPart("response_type", new StringBody(Constants.JSON));
			   multipartEntity.addPart("rest_data",
			       new StringBody(JSONObject.toJSONString(request)));

			   // yourSugarCRM has to be changed to your SugarCRM instance
			   // something like localhost/sugarcrm
			   
			   HttpPost httpPost = new HttpPost(Constants.URL);
			   httpPost.setEntity(multipartEntity);

			   DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			   HttpResponse execute = defaultHttpClient.execute(httpPost);

			   HttpEntity entity = execute.getEntity();
			   
			   JSONObject parse = (JSONObject) JSONValue.parse(new InputStreamReader(
			       entity.getContent()));
			   return (String) parse.toJSONString();//  get("result_count");
			
		}
	 
	 /*
	  * //Como generar los datos para que los pille bien el Sugar
			List<List<Map<String, String>>> name_value_lists = new ArrayList<List<Map<String, String>>>();
			List<Map<String, String>> objeto =  new ArrayList<Map<String, String>>();
			Map<String, String> atributo1 = new LinkedHashMap<String, String>();
			Map<String, String> atributo2 = new LinkedHashMap<String, String>();
			//Un hashmap por cada atributo 
			atributo1.put("name", "first_name");
			atributo1.put("value", "hugo");
			atributo2.put("name", "last_name");
			atributo2.put("value", "garcia");
			//Un objeto por cada record
			objeto.add(atributo1);
			objeto.add(atributo2);
			name_value_lists.add(objeto);
	  * 
	  * 
	  * 
	  * */
	   /**
	  * Update or create a list of SugarBeans
	  *
	  * @param String  session -- Session ID returned by a previous call to login.
	  * @param String  module_name -- The name of the module to return records from.  This name should be the name the module was developed under (changing a tab name is studio does not affect the name that should be passed into this method)..
	  * @param Array  name_value_lists -- Array of Bean specific Arrays where the keys of the array are the SugarBean attributes, the values of the array are the values the attributes should have.
	  * @return Array    'ids' -- Array of the IDs of the beans that was written to (-1 on error)
	  * @exception 'SoapFault' -- The SOAP error, if any
	  */
	 public static String setRecords(String session,String module_name,List<List<Map<String, String>>> name_value_lists) 
			 throws NoSuchAlgorithmException, ClientProtocolException, IOException{
				
			
			Map<String, Object> request = new LinkedHashMap<String, Object>();
			request.put("session", session);
			request.put("module_name", module_name);
			request.put("name_value_lists", name_value_lists);
			   
			   MultipartEntity multipartEntity = new MultipartEntity();
			   multipartEntity.addPart("method", new StringBody(Constants.SET));
			   // define request encoding
			   multipartEntity.addPart("input_type", new StringBody(Constants.JSON));
			   // define response encoding
			   multipartEntity.addPart("response_type", new StringBody(Constants.JSON));
			   multipartEntity.addPart("rest_data",
			       new StringBody(JSONObject.toJSONString(request)));
			   // yourSugarCRM has to be changed to your SugarCRM instance
			   // something like localhost/sugarcrm
			   
			   HttpPost httpPost = new HttpPost(Constants.URL);
			   httpPost.setEntity(multipartEntity);

			   DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			   HttpResponse execute = defaultHttpClient.execute(httpPost);

			   HttpEntity entity = execute.getEntity();
			   
			   JSONObject parse = (JSONObject) JSONValue.parse(new InputStreamReader(
			       entity.getContent()));
			   return (String) parse.toJSONString();//  get("result_count");
			
		}
	 	 
	 
	 
	 
	 /**
	  * Set a single relationship between two beans.  The items are related by module name and id.
	  *
	  * @param String  session -- Session ID returned by a previous call to login.
	  * @param String  module_name -- name of the module that the primary record is from.  This name should be the name the module was developed under (changing a tab name is studio does not affect the name that should be passed into this method)..
	  * @param String  module_id - The ID of the bean in the specified module_name
	  * @param String link_field_name -- name of the link field which relates to the other module for which the relationship needs to be generated.
	  * @param array related_ids -- array of related record ids for which relationships needs to be generated
	  * @param array  name_value_list -- The keys of the array are the SugarBean attributes, the values of the array are the values the attributes should have.
	  * @param integer  delete -- Optional, if the value 0 or nothing is passed then it will add the relationship for related_ids and if 1 is passed, it will delete this relationship for related_ids
	  * @return Array - created - integer - How many relationships has been created
	  *               - failed - integer - How many relationsip creation failed
	  * 				 - deleted - integer - How many relationships were deleted
	  * @exception 'SoapFault' -- The SOAP error, if any
	  */
	 public static String setRelationship(String session,String module_name,String module_id,String link_field_name, ArrayList<String> related_ids,
				List<Map<String, String>> name_value_list,Integer delete)throws NoSuchAlgorithmException, ClientProtocolException, IOException{ 
		 
			Map<String, Object> request = new LinkedHashMap<String, Object>();
			request.put("session", session);
			request.put("module_name", module_name);
			request.put("module_id", module_id);
			request.put("link_field_name", link_field_name);
			request.put("related_ids", related_ids);
			request.put("name_value_list", name_value_list);
			request.put("delete", delete);
			   
			   MultipartEntity multipartEntity = new MultipartEntity();
			   multipartEntity.addPart("method", new StringBody(Constants.SETREL));
			   // define request encoding
			   multipartEntity.addPart("input_type", new StringBody(Constants.JSON));
			   // define response encoding
			   multipartEntity.addPart("response_type", new StringBody(Constants.JSON));
			   multipartEntity.addPart("rest_data",
			       new StringBody(JSONObject.toJSONString(request)));

			   // yourSugarCRM has to be changed to your SugarCRM instance
			   // something like localhost/sugarcrm
			   
			   HttpPost httpPost = new HttpPost(Constants.URL);
			   httpPost.setEntity(multipartEntity);

			   DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			   HttpResponse execute = defaultHttpClient.execute(httpPost);

			   HttpEntity entity = execute.getEntity();
			   
			   JSONObject parse = (JSONObject) JSONValue.parse(new InputStreamReader(
			       entity.getContent()));
			   return (String) parse.toJSONString();
	 
	 }
	 /**
	  * Creates a new contact adding a relationship between the company passed
	  *
	  * @param String  session -- Session ID returned by a previous call to login.
	  * @param String  name -- Name of the contact
	  * @param String  phone -- The phone number of the contact
	  * @param String email -- The email of the contact
	  * @param String hierarchy -- The position in the company of the contact
	  * @param String companyId -- The company id which the relationship have to be made
	  * @return Array - created - integer - How many relationships has been created
	  *               - failed - integer - How many relationship creation failed
	  * 				 - deleted - integer - How many relationships were deleted
	  * @exception 'SoapFault' -- The SOAP error, if any
	  */
	 public static String newContact(String session,String name, 
			 String phone, String email,String hierarchy,String companyId){
		 List<List<Map<String, String>>> name_value_lists = new ArrayList<List<Map<String, String>>>();
			List<Map<String, String>> objeto =  new ArrayList<Map<String, String>>();
			Map<String, String> atributo1 = new LinkedHashMap<String, String>();
			Map<String, String> atributo2 = new LinkedHashMap<String, String>();
			Map<String, String> atributo3 = new LinkedHashMap<String, String>();
			Map<String, String> atributo4 = new LinkedHashMap<String, String>();
			//Un hashmap por cada atributo 
			atributo1.put("name", "name");
			atributo1.put("value", name);
			atributo2.put("name", "hierarchy");
			atributo2.put("value", hierarchy);
			atributo3.put("name", "phone");
			atributo3.put("value", phone);
			atributo4.put("name", "email");
			atributo4.put("value", email);
			//Un objeto por cada record
			objeto.add(atributo1);
			objeto.add(atributo2);
			objeto.add(atributo3);
			objeto.add(atributo4);
			name_value_lists.add(objeto);
			Gson mGson = new Gson();
			Ids ids = new Ids();
			try {
				ids = mGson.fromJson(setRecords(session,"Marke_Contact",name_value_lists),Ids.class);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    catch (JsonSyntaxException e) {
		    	e.printStackTrace();
		    }
			
			
			
			//No se para que se pone
			//EMPIEZA
			List<Map<String, String>> name_value_list =  new ArrayList<Map<String, String>>();
			Map<String, String> atributo = new LinkedHashMap<String, String>();
			atributo1.put("name", "name");
			atributo1.put("value", "hugo");
			name_value_list.add(atributo);
			//FIN
			
			ArrayList<String> related_ids = new ArrayList<String>();
			related_ids.add(companyId);
			
			
			try {
				setRelationship(session, "Marke_Contact", ids.getIds().get(0), "marke_contact_marke_company", related_ids, name_value_list, 0);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return ids.getIds().get(0);
			
	 }
	 /**
	  * Creates a new sale
	  *
	  * @param String  session -- Session ID returned by a previous call to login.
	  * @param String  name -- Name of the sale
	  * @param String  description -- A description of the sale
	  * @param String type -- The type of the sale
	  * @param String ammount -- The ammount of the sale
	  * @param String created -- When it was created the sale (YYYY-MM-DD)
	  * @param String companyId -- The company id which is related the sale
	  * @param String contactId -- The contact id which is related the sale
	  * @param String comercialId -- The comercial how did the sale
	  * @return List<String> - A list of created / modified / deleted
	  */
	 public static List<String> newSale(String session,String name, 
			 String description, String type,String ammount,String created,String companyId, String contactId,String comercialId){
		 List<List<Map<String, String>>> name_value_lists = new ArrayList<List<Map<String, String>>>();
			List<Map<String, String>> objeto =  new ArrayList<Map<String, String>>();
			Map<String, String> atributo1 = new LinkedHashMap<String, String>();
			Map<String, String> atributo2 = new LinkedHashMap<String, String>();
			Map<String, String> atributo3 = new LinkedHashMap<String, String>();
			Map<String, String> atributo4 = new LinkedHashMap<String, String>();
			Map<String, String> atributo5 = new LinkedHashMap<String, String>();
			Map<String, String> atributo6 = new LinkedHashMap<String, String>();
			//Un hashmap por cada atributo 
			atributo1.put("name", "name");
			atributo1.put("value", name);
			atributo2.put("name", "description");
			atributo2.put("value", description);
			atributo3.put("name", "type");
			atributo3.put("value", type);
			atributo4.put("name", "ammount");
			atributo4.put("value", ammount);
			atributo5.put("name","creado");
			atributo5.put("value",created);
			atributo6.put("name","comercial");
			atributo6.put("value",comercialId);
			//Un objeto por cada record
			objeto.add(atributo1);
			objeto.add(atributo2);
			objeto.add(atributo3);
			objeto.add(atributo4);
			objeto.add(atributo5);
			objeto.add(atributo6);
			name_value_lists.add(objeto);
			
			Gson mGson = new Gson();
			Ids ids = new Ids();
			try {
				ids = mGson.fromJson(setRecords(session,"Marke_Sale",name_value_lists),Ids.class);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    catch (JsonSyntaxException e) {
		    	e.printStackTrace();
		    }
			
			
			List<String> response = new ArrayList<String>();
			String company = "";
			String contact = "";
			//No se para que se pone
			//EMPIEZA
			List<Map<String, String>> name_value_list =  new ArrayList<Map<String, String>>();
			Map<String, String> atributo = new LinkedHashMap<String, String>();
			atributo1.put("name", "name");
			atributo1.put("value", "hugo");
			name_value_list.add(atributo);
			//FIN
			ArrayList<String> related_idsCompany = new ArrayList<String>();
			related_idsCompany.add(companyId);
			ArrayList<String> related_idsContact = new ArrayList<String>();
			related_idsContact.add(contactId);
			
			try {
				company = setRelationship(session, "Marke_Sale", ids.getIds().get(0), "marke_sale_marke_company", related_idsCompany, name_value_list, 0);
				contact = setRelationship(session, "Marke_Sale", ids.getIds().get(0), "marke_sale_marke_contact", related_idsContact, name_value_list, 0);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			response.add(company);
			response.add(contact);
			return response;
			
			
	 }
	 /**
	  * Creates a new company
	  *
	  * @param String  session -- Session ID returned by a previous call to login.
	  * @param String  name -- Name of the company
	  * @param String  phone -- The phone number of the company
	  * @param String email -- The email of the company
	  * @param String fax -- The fax of the company
	  * @param String city -- The city of the company
	  * @param String address -- The address of the company
	  * @param String web -- The website of the company
	  * @param String industry -- The industry which the company is related to
	  * @return String - The id of the created company
	  * @exception 'SoapFault' -- The SOAP error, if any
	  */
	 public static String newCompany(String session,String name, String city, String address, 
			 String phone, String email, String fax, String web, String industry){
		 List<List<Map<String, String>>> name_value_lists = new ArrayList<List<Map<String, String>>>();
			List<Map<String, String>> objeto =  new ArrayList<Map<String, String>>();
			Map<String, String> atributo1 = new LinkedHashMap<String, String>();
			Map<String, String> atributo2 = new LinkedHashMap<String, String>();
			Map<String, String> atributo3 = new LinkedHashMap<String, String>();
			Map<String, String> atributo4 = new LinkedHashMap<String, String>();
			Map<String, String> atributo5 = new LinkedHashMap<String, String>();
			Map<String, String> atributo6 = new LinkedHashMap<String, String>();
			Map<String, String> atributo7 = new LinkedHashMap<String, String>();
			Map<String, String> atributo8 = new LinkedHashMap<String, String>();
			//Un hashmap por cada atributo 
			atributo1.put("name", "name");
			atributo1.put("value", name);
			atributo2.put("name", "industry");
			atributo2.put("value", industry);
			atributo3.put("name", "city");
			atributo3.put("value", city);
			atributo4.put("name", "address");  //solo tiene una d
			atributo4.put("value", address);
			atributo5.put("name", "phone");   // solo phone
			atributo5.put("value", phone);
			atributo6.put("name", "email");
			atributo6.put("value", email);
			atributo7.put("name", "fax");   // solo fax
			atributo7.put("value", fax);
			atributo8.put("name", "web");   // solo web
			atributo8.put("value", web);
			//Un objeto por cada record
			objeto.add(atributo1);
			objeto.add(atributo2);
			objeto.add(atributo3);
			objeto.add(atributo4);
			objeto.add(atributo5);
			objeto.add(atributo6);
			objeto.add(atributo7);
			objeto.add(atributo8);
			name_value_lists.add(objeto);
			Gson mGson = new Gson();
			Ids ids = new Ids();
			try {
				ids = mGson.fromJson(setRecords(session,"Marke_Company",name_value_lists),Ids.class);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    catch (JsonSyntaxException e) {
		    	e.printStackTrace();
		    }
			
			return ids.getIds().get(0);
		 
	 }
	 

	 public static String getCompanyId (String name,String session){
		 
		 String response = "";
		 String qry = "name = "+"'"+name+"'";
		 try {
			response = getRecords(session,"Marke_Company",qry,"",null,null,1);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 Gson mGson = new Gson();
		 ResponseCompanyID responseCompanyID = new ResponseCompanyID();
		 
		 try{
			 responseCompanyID = mGson.fromJson(response, ResponseCompanyID.class);
		 }
		 catch (JsonSyntaxException e) {
		    	e.printStackTrace();
		    }
		 if (!responseCompanyID.getEntry_list().isEmpty()){

			 return responseCompanyID.getEntry_list().get(0).getId();
		 }
		 return "";
	 }
	 
	 public static List<HashMap<String, String>> getContacts(String session, String idCompany){
		 
		 
		 String response = "";
		 
		 try {
			response = getRelationships(session, "Marke_Company", idCompany,"marke_contact_marke_company", "", 1,"");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		 Gson mGson = new Gson();
		 Response contactsList = new Response();
		 
		 try{
			 contactsList = mGson.fromJson(response, Response.class);
		 }
		 catch (JsonSyntaxException e) {
		    	e.printStackTrace();
		    }
		 
		 List<HashMap<String, String>> contactos = new ArrayList<HashMap<String, String>>();
		 
		 for (Iterator<Contact> iterator = contactsList.getEntry_list().iterator(); iterator.hasNext();) {
			 Contact contact = iterator.next();
			 HashMap<String,String> contacto = new HashMap<String,String>();
			 contacto.put("id", contact.getName_value_list().getId().getValue());
			 contacto.put("name", contact.getName_value_list().getName().getValue());
			 contactos.add(contacto);
			
		} 
		 return contactos;
	 }
	 
	 
	 /**
	  * Gets all the sales related to a comercial
	  *
	  * @param String  session -- Session ID returned by a previous call to login.
	  * @param String  fechaIni -- Name of the company (YYYY-MM-DD)
	  * @param String  fechaFin -- The phone number of the company (YYYY-MM-DD)
	  * @param String idUsuario -- The email of the company
	  * @param Integer next_offset -- The fax of the company
	  * @return List<SaleResponse> - A list of SaleResponse 
	  * @exception 'SoapFault' -- The SOAP error, if any
	  */
	 public static List<SaleResponse> getRevision(String session, String fechaIni, String fechaFin,String idUsuario,Integer next_offset){

		 String response = "";
		 
		String	qry = "creado >="+"'"+fechaIni+"'"+" and creado <="+"'"+fechaFin+"'"+" and comercial ="+"'"+idUsuario+"'";
		
		
		 try {
			response =getRecords(session,"Marke_Sale",qry,"",null,null,next_offset);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		 Gson mGson = new Gson();
		 ResponseData salesList1 = new ResponseData();
		 
		 try{
			 salesList1 = mGson.fromJson(response, ResponseData.class);
		 }
		 catch (JsonSyntaxException e) {
		    	e.printStackTrace();
		    }
		 List<SaleResponse> finalResponse = new ArrayList<SaleResponse>();
		 String company ="";
		 String contact="";
		 for (Iterator<Sale> iterator = salesList1.getEntry_list().iterator(); iterator.hasNext();) {
			Sale sale = iterator.next();
			String id = sale.getId();
			SaleResponse sr = new SaleResponse();
			sr.setAmmount(sale.getName_value_list().getAmmount().get("value"));
			sr.setDateCreated(sale.getName_value_list().getCreado().get("value"));
			sr.setDescription(sale.getName_value_list().getDescription().get("value"));
			sr.setSaleName(sale.getName_value_list().getName().get("value"));
			sr.setType(sale.getName_value_list().getType().get("value"));
			try {
				company= getRelationships(session, "Marke_Sale", id, "marke_sale_marke_company","", 1, "");
				contact= getRelationships(session, "Marke_Sale", id, "marke_sale_marke_contact","", 1, "");
				Response companyResponse = new Response();
				Response contactResponse = new Response();
				companyResponse = mGson.fromJson(company, Response.class);
				contactResponse = mGson.fromJson(contact, Response.class);

				sr.setCompany(companyResponse.getEntry_list().get(0).getName_value_list().getName().getValue());
				sr.setContact(contactResponse.getEntry_list().get(0).getName_value_list().getName().getValue());
			
				 
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch (JsonSyntaxException e) {
		    	e.printStackTrace();
		    }
			finalResponse.add(sr);
			
			
		}
		 
		 
		
		 
		 return finalResponse;
		 
	 }
	 public static String convertStreamToString(InputStream is)
	    {
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    	StringBuilder sb = new StringBuilder();

	    	String line = null;
	    	try {
	    		while ((line = reader.readLine()) != null) {
	    			sb.append(line + "\n");
	    		}
	    	}
	    	catch (IOException e) {
	    	}
	    	finally {
	    		try {
	    			is.close();
	    		} catch (IOException e1) {
	    		}
	    	}
	    	return sb.toString();
	    }
	 public static TreeMap<String, Integer> getStatics(String session,String fechaIni, String fechaFin) throws ClientProtocolException, IOException{
			
		 String response = "";
		 HashMap<String, Integer> clasificacion = new HashMap<String,Integer>();
		 
		 try {
			response = getRecords(session,"Users","","",null,null,1);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 Gson mGson = new Gson();
		 ResponseCompanyID responseCompanyID = new ResponseCompanyID();
		 
		 try{
			 responseCompanyID = mGson.fromJson(response, ResponseCompanyID.class);
		 }
		 catch (JsonSyntaxException e) {
		    	e.printStackTrace();
		    }
		 int cont = 0;
		 for (Iterator<Contact> iterator = responseCompanyID.getEntry_list().iterator(); iterator.hasNext();) {
			 Contact contact = iterator.next();
			 StringBuilder qry = new StringBuilder("comercial = "+"'"+responseCompanyID.getEntry_list().get(cont).getId()+"'");
			 if (!fechaIni.equals("")){
				 qry.append(" and creado >="+"'"+fechaIni+"'");
			 }
			 if (!fechaFin.equals("")){
				 qry.append(" and creado <="+"'"+fechaFin+"'");
			 } 
			 
			 cont++;
			 try {
					response = getRecords(session,"Marke_Sale",qry.toString(),"",null,null,1);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			 ResponseCompanyID respons = new ResponseCompanyID();
			 
			 try{
				 respons = mGson.fromJson(response, ResponseCompanyID.class);
			 }
			 catch (JsonSyntaxException e) {
			    	e.printStackTrace();
			    }
			 clasificacion.put(contact.getId().split("_")[1], Integer.parseInt(respons.getTotal_count()));
		}
		 	 
		 //Ordenar el map
		ValueComparator bvc = new ValueComparator(clasificacion);
		TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
		sorted_map.putAll(clasificacion);
		return sorted_map;
	 }
	 
	

}

