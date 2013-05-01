package com.molihugo.easycheck.apis.sugar;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
   request.put("application_name", Constantes.APPLICATION_NAME);

   MultipartEntity multipartEntity = new MultipartEntity();
   multipartEntity.addPart("method", new StringBody(Constantes.LOGIN));
   // define request encoding
   multipartEntity.addPart("input_type", new StringBody(Constantes.JSON));
   // define response encoding
   multipartEntity.addPart("response_type", new StringBody(Constantes.JSON));
   multipartEntity.addPart("rest_data",
       new StringBody(JSONObject.toJSONString(request)));

   // yourSugarCRM has to be changed to your SugarCRM instance
   // something like localhost/sugarcrm
   
   HttpPost httpPost = new HttpPost(Constantes.URL);
   httpPost.setEntity(multipartEntity);

   DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
   HttpResponse execute = defaultHttpClient.execute(httpPost);

   HttpEntity entity = execute.getEntity();
   
   JSONObject parse = (JSONObject) JSONValue.parse(new InputStreamReader(
       entity.getContent()));
   return (String) parse.get(Constantes.ID);
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
			   multipartEntity.addPart("method", new StringBody(Constantes.GET));
			   // define request encoding
			   multipartEntity.addPart("input_type", new StringBody(Constantes.JSON));
			   // define response encoding
			   multipartEntity.addPart("response_type", new StringBody(Constantes.JSON));
			   multipartEntity.addPart("rest_data",
			       new StringBody(JSONObject.toJSONString(request)));

			   // yourSugarCRM has to be changed to your SugarCRM instance
			   // something like localhost/sugarcrm
			   
			   HttpPost httpPost = new HttpPost(Constantes.URL);
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
			   multipartEntity.addPart("method", new StringBody(Constantes.GETREL));
			   // define request encoding
			   multipartEntity.addPart("input_type", new StringBody(Constantes.JSON));
			   // define response encoding
			   multipartEntity.addPart("response_type", new StringBody(Constantes.JSON));
			   multipartEntity.addPart("rest_data",
			       new StringBody(JSONObject.toJSONString(request)));

			   // yourSugarCRM has to be changed to your SugarCRM instance
			   // something like localhost/sugarcrm
			   
			   HttpPost httpPost = new HttpPost(Constantes.URL);
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
			   multipartEntity.addPart("method", new StringBody(Constantes.SET));
			   // define request encoding
			   multipartEntity.addPart("input_type", new StringBody(Constantes.JSON));
			   // define response encoding
			   multipartEntity.addPart("response_type", new StringBody(Constantes.JSON));
			   multipartEntity.addPart("rest_data",
			       new StringBody(JSONObject.toJSONString(request)));
			   // yourSugarCRM has to be changed to your SugarCRM instance
			   // something like localhost/sugarcrm
			   
			   HttpPost httpPost = new HttpPost(Constantes.URL);
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
			   multipartEntity.addPart("method", new StringBody(Constantes.SETREL));
			   // define request encoding
			   multipartEntity.addPart("input_type", new StringBody(Constantes.JSON));
			   // define response encoding
			   multipartEntity.addPart("response_type", new StringBody(Constantes.JSON));
			   multipartEntity.addPart("rest_data",
			       new StringBody(JSONObject.toJSONString(request)));

			   // yourSugarCRM has to be changed to your SugarCRM instance
			   // something like localhost/sugarcrm
			   
			   HttpPost httpPost = new HttpPost(Constantes.URL);
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
			
			String response = "";
			
			try {
				response = setRelationship(session, "Marke_Contact", ids.getIds().get(0), "marke_contact_marke_company", related_ids, name_value_list, 0);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return ids.getIds().get(0);
			
	 }
	 
	 public static List<String> newSale(String session,String name, 
			 String description, String type,String ammount,String companyId, String contactId){
		 List<List<Map<String, String>>> name_value_lists = new ArrayList<List<Map<String, String>>>();
			List<Map<String, String>> objeto =  new ArrayList<Map<String, String>>();
			Map<String, String> atributo1 = new LinkedHashMap<String, String>();
			Map<String, String> atributo2 = new LinkedHashMap<String, String>();
			Map<String, String> atributo3 = new LinkedHashMap<String, String>();
			Map<String, String> atributo4 = new LinkedHashMap<String, String>();
			//Un hashmap por cada atributo 
			atributo1.put("name", "name");
			atributo1.put("value", name);
			atributo2.put("name", "description");
			atributo2.put("value", description);
			atributo3.put("name", "type");
			atributo3.put("value", type);
			atributo4.put("name", "ammount");
			atributo4.put("value", ammount);
			//Un objeto por cada record
			objeto.add(atributo1);
			objeto.add(atributo2);
			objeto.add(atributo3);
			objeto.add(atributo4);
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
			atributo4.put("name", "adress");
			atributo4.put("value", address);
			atributo5.put("name", "phone_office");
			atributo5.put("value", phone);
			atributo6.put("name", "email");
			atributo6.put("value", email);
			atributo7.put("name", "phone_fax");
			atributo7.put("value", fax);
			atributo8.put("name", "website");
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
	 
	 public static Response getRevision(String session, String fechaIni, String fechaFin,String idUsuario){

		 String response = "";
		 
		String	qry = "date_entered >="+"'"+fechaIni+"'"+" and date_entered <="+"'"+fechaFin+"'";
		
		
		 try {
			response =getRelationships(session, "Marke_Contact", idUsuario,"marke_sale_marke_contact", qry, 1,"");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		 Gson mGson = new Gson();
		 Response salesList = new Response();
		 
		 try{
			 salesList = mGson.fromJson(response, Response.class);
		 }
		 catch (JsonSyntaxException e) {
		    	e.printStackTrace();
		    }
		 return salesList;
		 
	 }
	 
	 public static TreeMap<String,Long> getStatics(String fechaIni, String fechaFin){
		 HashMap<String, Long> clasificacion = new HashMap<String,Long>();
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sugarcrm", "root", "root");
				Statement st = con.createStatement();
				StringBuilder sql = new StringBuilder();
				sql.append("select distinct t1.name");
				sql.append(" from marke_sale t2");
				sql.append("inner join marke_sale_marke_contact_c t on (t.marke_sale_marke_contactmarke_sale_idb=t2.id)");
				sql.append("inner join marke_contact t1 on (t.marke_sale_marke_contactmarke_contact_ida=t1.id);");
				ResultSet rs = st.executeQuery(sql.toString());
				
				while (rs.next())
				{
					
					String name = (String) rs.getObject("name");
					
					Statement st2 = con.createStatement();
					StringBuilder sql2 = new StringBuilder();
					sql2.append("select count(*)");
					sql2.append(" from marke_sale t2");
					sql2.append(" inner join marke_sale_marke_contact_c t on (t.marke_sale_marke_contactmarke_sale_idb=t2.id)");
					sql2.append(" inner join marke_contact t1 on (t.marke_sale_marke_contactmarke_contact_ida=t1.id);");
					sql2.append(" where t1.name='");
					sql2.append(name);
					sql2.append("'");
					sql2.append(" and");
					sql2.append(" t2.date_entered>='");
					sql2.append(fechaIni);
					sql2.append("' and t2.date_entered<='");
					sql2.append(fechaFin);
					sql2.append("'");
					ResultSet rs2 = st2.executeQuery(sql2.toString());
					while (rs2.next()){
						Long count = (Long) rs2.getObject("count(*)");
						clasificacion.put(name, count);
					}
					rs2.close();
					st2.close();
				}
				rs.close();
				st.close();
				
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}   catch (SQLException e) {
				e.printStackTrace();
			}
			ValueComparator bvc =  new ValueComparator(clasificacion);
	        TreeMap<String,Long> sorted_map = new TreeMap<String,Long>(bvc);
	        sorted_map.putAll(clasificacion);
	        return sorted_map;
	 }
	 

}

