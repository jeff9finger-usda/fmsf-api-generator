package gov.msf.rest.client;

import gov.firenet.fmsf.api.client.ModelControllerApi;
import gov.firenet.fmsf.api.client.ResourceControllerApi;
import gov.firenet.fmsf.api.ApiClient;
import gov.firenet.fmsf.api.dto.ModelMetadataDto;
import gov.firenet.fmsf.api.dto.ResourceMetadataDto;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.File;

public class MsfApiClientHelper {
	private final ModelControllerApi modelsApi;
	private final ResourceControllerApi resourcesApi;

	public MsfApiClientHelper(final String username, final String password) {
		final ApiClient apiClient = new ApiClient();
		apiClient.setUsername(username);
		apiClient.setPassword(password);
		//apiClient.setServers(List.of(new ServerConfiguration("https://fmsf2.firenet.gov/api", "Production Server", Map.of())));
		apiClient.setHttpClient(HttpClientBuilder.create().build());
		this.modelsApi = new ModelControllerApi(apiClient);
		this.resourcesApi = new ResourceControllerApi(apiClient);
	}

	public ResourceMetadataDto getResourceMetadata(Long resourceId) {
		return resourcesApi.getResourceMetadata(resourceId);
	}

	public ResourceMetadataDto uploadResource(ModelMetadataDto.ModelTypeEnum modelType, File resourceFile) {
		return resourcesApi.upload(modelType.getValue(), resourceFile);
	}

	public void downloadResource(Long resourceId) {
		resourcesApi.download(resourceId);
	}

	public ModelMetadataDto scheduleModelRun(ModelMetadataDto.ModelTypeEnum modelType, File inputFile,Long landscape,
											 Long ignition, Long barrier, Long griddedWindSpeed, Long griddedWindDir) {
		return modelsApi.uploadModel(modelType.getValue(), landscape, ignition, barrier, griddedWindSpeed, griddedWindDir, inputFile);
	}

	public ModelMetadataDto getModelMetadata(Long modelRunId) {
		return modelsApi.getModelMetadata(modelRunId);
	}

	public ModelMetadataDto cancelModelRun(Long modelRunId) {
		return modelsApi.cancelRun(modelRunId);
	}

//	private String userName = "iftdss";
//	private String userAccessCode = "";
//	private String encryptedAccessCode = "R0gxPkXKnpLTyIU_Nj5G";

//	/** Empty constructor */
//	public MsfRestClientHelper(EntityManager em) {
//		try {
//			FirenetPropertyDAO propDAO = new FirenetPropertyDAO(em);
//			ApplicationEnum appEnum = ApplicationEnum.FIRENET;
//			FirenetPropertyDO propertyDO = propDAO.getFirenetProperty("msfAccessId", appEnum.getId());
//			if (propertyDO != null) {
//				userName = propertyDO.getValue();
//			}
//			propertyDO = propDAO.getFirenetProperty("msfAccessCode", appEnum.getId());
//			if (propertyDO != null) {
//				encryptedAccessCode = propertyDO.getValue();
//			}
//		} catch (FirenetDataException e) { // use default value
//			System.err.println(
//				"Error occurred attempting to obtain MSF Access ID and Code from the database (using defaults instead): " + e.getMessage()
//			);
//		}
//		userAccessCode = new PasswordEncryption().decode(encryptedAccessCode);
//	}
//
//	/** Empty constructor */
//	public MsfRestClientHelper() {
//		EntityManager em = null;
//		ApplicationEnum appEnum = ApplicationEnum.FIRENET;
//		try {
//			em = FirenetEMHelper.getJseFirenetEntityManager("MsfRestClientHelper");
//			FirenetPropertyDAO propDAO = new FirenetPropertyDAO(em);
//			FirenetPropertyDO propertyDO = propDAO.getFirenetProperty("msfAccessId", appEnum.getId());
//			if (propertyDO != null) {
//				userName = propertyDO.getValue();
//			}
//			propertyDO = propDAO.getFirenetProperty("msfAccessCode", appEnum.getId());
//			if (propertyDO != null) {
//				encryptedAccessCode = propertyDO.getValue();
//			}
//		} catch (FirenetDataException e) { // use default value
//			System.err.println(
//					"Error occurred attempting to obtain MSF Access ID and Code from the database (using defaults instead): " + e.getMessage()
//			);
//		} finally {
//			FirenetEMHelper.closeEntityMgr(em);
//		}
//		userAccessCode = new PasswordEncryption().decode(encryptedAccessCode);
//	}
//
//	public MsfRestClientHelper(String user, String accessCode) {
//		userName = user;
//		userAccessCode = accessCode;
//	}
//
//	/**
//	 * Handle logging of errors.
//	 *
//	 * @param fullMessage  the error message string
//	 */
//	protected void processError(String fullMessage) {
//		logger.error(fullMessage);
//	}
//
//	/**
//	 * Handle logging of errors.
//	 *
//	 * @param errMsg  the error message string
//	 * @param e  the caught exception
//	 */
//	protected void processError(String errMsg, Exception e) {
//		String fullMessage = (e.getMessage() == null) ? errMsg + ": " + e.getCause().getMessage() : errMsg + ": " + e.getMessage();
//		processError(fullMessage);
//	}
//
//	/**
//	 * Take the input stream that we get from a REST service response and convert it into a stream.
//	 * This will only work for input streams that contain a String, such as JSON.
//	 *
//	 * @param is  the input stream
//	 * @return  a String of what was in the input stream.
//	 */
//	public String convertInputStreamToString(InputStream is) {
//		final int bufferSize = 1024;
//		final char[] buffer = new char[bufferSize];
//		final StringBuilder out = new StringBuilder();
//		InputStreamReader in = null;
//		try {
//			in = new InputStreamReader(is, "UTF-8");
//			for (;;) {
//				int rsz = in.read(buffer, 0, buffer.length);
//				if (rsz < 0) break;
//				out.append(buffer, 0, rsz);
//			}
//		} catch (IOException e) {
//			processError("Error reading from stream", e);
//		} finally {
//			try {
//				if (in != null) in.close();
//			} catch (IOException ioe) {
//				/* ignore */
//			}
//		}
//		return out.toString();
//	}
//
//	/**
//	 * Take the input stream that we get from a REST service response and save it to a file specified
//	 * by the input parameter <em>file</em>.
//	 * <strong>Note:</strong>
//	 * The caller of this method is responsible for closing the InputStream that is passed in.
//	 *
//	 * @param is  the input stream returned by the REST call
//	 * @param file  the java.io.File created for your zip or image file
//	 * @return  true if the contents of the input stream have been saved to a file
//	 */
//	public boolean convertInputStreamToFile(InputStream is, File file) throws FileNotFoundException {
//		final int bufferSize = 262144;
//		final byte[] bytes = new byte[bufferSize];
//		FileOutputStream out = null;
//		try {
//			out = new FileOutputStream(file);
//			int read = 0;
//			while ((read = is.read(bytes)) != -1) {
//				out.write(bytes, 0, read);
//			}
//		} catch (FileNotFoundException e) {
//			processError("File Not Found: " + file.getPath() + file.getName());
//		} catch (IOException e) {
//			processError("Error reading from stream", e);
//		} finally {
//			try {
//				if (out != null) out.close();
//			} catch (IOException ioe) {
//				/* ignore */
//			}
//		}
//		return true;
//	}
//
//	public String sendAndReceiveRestGetRequest(String hostDomain, String restPathAndArgs)
//		throws MsfHttpException, URISyntaxException, IOException {
//		return sendAndReceiveRestGetRequest(hostDomain, restPathAndArgs, false);
//	}
//
//	/**
//	 * Send and receive a GET request to a REST service
//	 *
//	 * @param  the host domain for the REST call, such as "localhost:8080"
//	 * @param restPathAndArgs additional URL path and URL parameters
//	 * @return  the response body as a String
//	 * @throws MsfHttpException
//	 */
//	public String sendAndReceiveRestGetRequest(String hostDomain, String restPathAndArgs, boolean useHttps)
//		throws MsfHttpException, URISyntaxException, IOException {
//		InputStream is = null;
//		String responseBody = null;
//		String scheme = "http";
//		if (useHttps) {
//			scheme = "https"; //for https requests
//		}
//		boolean runFSMFFromAWS = PropertyRetriever.getBooleanProperty("msfFromAWS", false);
//		if (runFSMFFromAWS) {
//			scheme = "https"; //for https requests
//		}
//		CloseableHttpClient httpClient = null;
//		try {
//			// Set up Basic Authentication credentials
//			CredentialsProvider credProvider = new BasicCredentialsProvider();
//			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, userAccessCode);
//			credProvider.setCredentials(AuthScope.ANY, credentials);
//
//
//			// Build a GET request
//			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//			httpClient = httpClientBuilder.setDefaultCredentialsProvider(credProvider).build();
//			URI uri = new URIBuilder().setScheme(scheme).setHost(hostDomain).setPath(restPathAndArgs).build();
//			HttpGet httpGet = new HttpGet(uri);
//			//Security of basic authentication
//			//As the user ID and password are passed over the network as clear text (it is base64 encoded, but base64 is a reversible encoding),
//			//the basic authentication scheme is not secure. HTTPS/TLS should be used with basic authentication. Without these additional security
//			//enhancements, basic authentication should not be used to protect sensitive or valuable information.
//			byte[] buildCredentials = Base64.encodeBase64((userName + ":" + userAccessCode).getBytes(StandardCharsets.UTF_8));
//			httpGet.setHeader("Authorization", "Basic " + new String(buildCredentials, StandardCharsets.UTF_8));
//
//			// Send the GET request
//			CloseableHttpResponse response = httpClient.execute(httpGet);
//			HttpEntity entity = response.getEntity();
//			if (entity == null) {
//				processError("Entity from http response was null");
//				return "";
//			}
//
//			// Handle the GET response
//			int httpStatusCode = response.getStatusLine().getStatusCode();
//			System.out.println("HTTP Response Code: " + response.getStatusLine().getStatusCode());
//			is = entity.getContent();
//			responseBody = convertInputStreamToString(is);
//			logger.info("Response Body: " + responseBody);
//
//			if (httpStatusCode != 200) {
//				throw new MsfHttpException(httpStatusCode, addErrorDetails(httpStatusCode, response, responseBody));
//			}
//		} catch (URISyntaxException e) {
//			processError("Error building URI", e);
//			throw e;
//		} catch (IOException e) {
//			processError("Error getting response", e);
//			throw e;
//		} finally {
//			try {
//				if (is != null) is.close();
//				if (httpClient != null) httpClient.close();
//			} catch (IOException e) {
//				/* do nothing */
//			}
//		}
//
//		return responseBody;
//	}
//
//	private boolean zipFileisValid(File file) {
//		ZipFile zipfile = null;
//		Enumeration<?> entries;
//		try {
//			zipfile = new ZipFile(file);
//			//force the zip file to read itself, so if we have some error, we trap it earlier
//			entries = zipfile.entries();
//			if (entries == null || (!entries.hasMoreElements())) {
//				logger.error("Invalid Zip file has no elements: " + file.getAbsolutePath());
//				return false;
//			}
//			//if this is not enough to test that the Zip is valid, we may need to walk thru the list of entries
//			//as in below. For now, this seems excessive , so it's here as an commented out option
//			//while (entries.hasMoreElements())
//			//	ZipEntry entry = (ZipEntry)entries.nextElement();
//			//
//			return true;
//		} catch (IOException e) {
//			return false;
//		} finally {
//			entries = null; //force release of resources, just in case
//			try {
//				if (zipfile != null) {
//					zipfile.close();
//					zipfile = null;
//				}
//			} catch (IOException e) {}
//		}
//	}
//
//	/**
//	 * Send and a GET request to a REST service that returns a <strong>Zip</strong> file and save that file.
//	 *
//	 * @param  the host domain for the REST call, such as "localhost:8080"
//	 * @param  restPathAndArgs additional URL path and URL parameters
//	 * @param  the zip file to save to
//	 * @return  true if the zip file was created, false if not
//	 * @throws MsfHttpException
//	 */
//	public boolean sendAndReceiveRestGetZipRequest(String hostDomain, String restPathAndArgs, File file) throws MsfHttpException, Exception {
//		boolean successful = false;
//		MsfHttpException lastException = null;
//		boolean tryFailed = false;
//		//we will make sure we try this at least 6 times
//		final int FMSF_NUMBER_RETRIES = 6;
//		int tryCount = 0;
//		while (tryCount < FMSF_NUMBER_RETRIES && !successful) {
//			tryFailed = false;
//			tryCount++;
//			try {
//				successful = sendAndReceiveRestGetZipRequestJob(hostDomain, restPathAndArgs, file);
//				//check if the zip file is correct
//				if (!zipFileisValid(file)) {
//					//bad zip file
//					logger.error(
//						"Bad zip results file from FMSF for " + restPathAndArgs + " (tryCount=" + tryCount + ")" + " file=" + file.getAbsolutePath()
//					);
//					successful = false;
//					try {
//						TimeUnit.SECONDS.sleep(10);
//					} catch (InterruptedException exc) {}
//				}
//			} catch (MsfHttpException e) {
//				lastException = e;
//				tryFailed = true;
//				logger.error("FMSF HTTP failure for " + restPathAndArgs + " (tryCount=" + tryCount + ")" + " file=" + file.getAbsolutePath(), e);
//				try {
//					TimeUnit.SECONDS.sleep(10);
//				} catch (InterruptedException exc) {}
//			}
//		} //end while
//		if (tryFailed && !successful && lastException != null) {
//			//throw last exception
//			throw lastException;
//		}
//		//if successful wait two seconds ..so the file is ready to be used
//		if (successful) {
//			try {
//				TimeUnit.SECONDS.sleep(2);
//			} catch (InterruptedException e) {}
//		} else {
//			throw new Exception("Bad zip results file from FMSF for " + restPathAndArgs);
//		}
//		return successful;
//	}
//
//	private boolean sendAndReceiveRestGetZipRequestJob(String hostDomain, String restPathAndArgs, File file) throws MsfHttpException {
//		if (file == null) throw new MsfHttpException(400, "'file' parameter can not be null");
//		boolean runFSMFFromAWS = PropertyRetriever.getBooleanProperty("msfFromAWS", false);
//		String scheme = "http";
//		if (runFSMFFromAWS) {
//			scheme = "https"; //for https requests
//		}
//		InputStream is = null;
//		String responseBody = null;
//		boolean createdZip = false;
//
//		CloseableHttpClient httpClient = null;
//		try {
//			if (!file.exists()) {
//				file.createNewFile();
//				if (!file.exists()) {
//					throw new MsfHttpException(400, "Unable to create file " + file.getPath());
//				}
//			}
//
//			// Set up Basic Authentication credentials
//			CredentialsProvider credProvider = new BasicCredentialsProvider();
//			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, userAccessCode);
//			credProvider.setCredentials(AuthScope.ANY, credentials);
//
//			// Build a GET request
//			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//			httpClient = httpClientBuilder.setDefaultCredentialsProvider(credProvider).build();
//			URI uri = new URIBuilder().setScheme(scheme).setHost(hostDomain).setPath(restPathAndArgs).build();
//			HttpGet httpGet = new HttpGet(uri);
//			httpGet.addHeader(HttpHeaders.CONTENT_TYPE, "application/zip");
//			logger.info("GET URI: " + httpGet.getURI());
//
//			//Security of basic authentication
//			//As the user ID and password are passed over the network as clear text (it is base64 encoded, but base64 is a reversible encoding),
//			//the basic authentication scheme is not secure. HTTPS/TLS should be used with basic authentication. Without these additional security
//			//enhancements, basic authentication should not be used to protect sensitive or valuable information.
//			byte[] buildCredentials = Base64.encodeBase64((userName + ":" + userAccessCode).getBytes(StandardCharsets.UTF_8));
//			httpGet.setHeader("Authorization", "Basic " + new String(buildCredentials, StandardCharsets.UTF_8));
//
//			// Send the GET request
//			CloseableHttpResponse response = httpClient.execute(httpGet);
//			HttpEntity entity = response.getEntity();
//			if (entity == null) {
//				processError("Entity from http response was null");
//				return false;
//			}
//
//			// Handle the GET response
//			int httpStatusCode = response.getStatusLine().getStatusCode();
//			logger.info("HTTP Response Code: " + response.getStatusLine().getStatusCode());
//			if (httpStatusCode != 200) {
//				throw new MsfHttpException(httpStatusCode, addErrorDetails(httpStatusCode, response, responseBody));
//			}
//			is = entity.getContent();
//			createdZip = convertInputStreamToFile(is, file);
//		} catch (URISyntaxException e) {
//			processError("Error building URI", e);
//		} catch (IOException e) {
//			processError("Error getting response", e);
//		} finally {
//			try {
//				if (httpClient != null) httpClient.close();
//			} catch (IOException e) {
//				/* do nothing */
//			}
//		}
//
//		return createdZip;
//	}
//
//	/**
//	 * Send and receive a POST request to the Landscape REST service
//	 *
//	 * @restURI  the URI of the rest service
//	 * @formParams restPath additional URL path
//	 * @return  the response body as a String
//	 */
//	public String sendAndReceiveRestPostForm(String hostDomain, String restPath, List<NameValuePair> formParams, String contentType) {
//		InputStream is = null;
//		String responseBody = null;
//
//		CloseableHttpClient httpClient = null;
//		try {
//			boolean runFSMFFromAWS = PropertyRetriever.getBooleanProperty("msfFromAWS", false);
//			String scheme = "http";
//			if (runFSMFFromAWS) {
//				scheme = "https"; //for https requests
//			}
//			// Set up Basic Authentication credentials
//			CredentialsProvider credProvider = new BasicCredentialsProvider();
//			//TODO: change so id and pw are passed in as args or come from properties
//			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, userAccessCode);
//			credProvider.setCredentials(AuthScope.ANY, credentials);
//
//			// Build a POST request
//			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//			httpClient = httpClientBuilder.setDefaultCredentialsProvider(credProvider).build();
//			URI uri = new URIBuilder().setScheme(scheme).setHost(hostDomain).setPath(restPath).build();
//			HttpPost httpPost = new HttpPost(uri);
//
//			logger.info("POST URI: " + httpPost.getURI());
//			httpPost.setEntity(new UrlEncodedFormEntity(formParams));
//			httpPost.setHeader("Content-type", contentType);
//
//			//Security of basic authentication
//			//As the user ID and password are passed over the network as clear text (it is base64 encoded, but base64 is a reversible encoding),
//			//the basic authentication scheme is not secure. HTTPS/TLS should be used with basic authentication. Without these additional security
//			//enhancements, basic authentication should not be used to protect sensitive or valuable information.
//			byte[] buildCredentials = Base64.encodeBase64((userName + ":" + userAccessCode).getBytes(StandardCharsets.UTF_8));
//			httpPost.setHeader("Authorization", "Basic " + new String(buildCredentials, StandardCharsets.UTF_8));
//
//			// Send the POST request
//			CloseableHttpResponse response = httpClient.execute(httpPost);
//			if (response.getStatusLine().getStatusCode() != 200) {
//				processError("Error occurred submitting HTTP post: " + response.getStatusLine().getStatusCode());
//				processError(response.getStatusLine().getReasonPhrase());
//				return "";
//			}
//
//			HttpEntity entity = response.getEntity();
//			if (entity == null) {
//				processError("Entity from http response was null.");
//				return "";
//			}
//
//			// Handle the POST response
//			System.out.println("HTTP Response Code: " + response.getStatusLine().getStatusCode());
//			is = entity.getContent();
//			responseBody = convertInputStreamToString(is);
//			logger.info("Response Body: " + responseBody);
//		} catch (URISyntaxException e) {
//			processError("Error building URI", e);
//		} catch (IOException e) {
//			processError("Error getting response", e);
//		} finally {
//			try {
//				if (is != null) is.close();
//				if (httpClient != null) httpClient.close();
//			} catch (IOException e) {
//				processError("Problem closing input stream", e);
//			}
//		}
//
//		return responseBody;
//	}
//
//	public String sendAndReceiveRestPostForm(String hostDomain, String restPath, MultipartEntityBuilder builder) {
//		return sendAndReceiveRestPostForm(hostDomain, restPath, builder, false);
//	}
//
//	/**
//	 * Send and receive a POST request to the Landscape REST service
//	 *
//	 * @restURI  the URI of the rest service
//	 * @formParams restPath additional URL path
//	 * @return  the response body as a String
//	 */
//	public String sendAndReceiveRestPostForm(String hostDomain, String restPath, MultipartEntityBuilder builder, boolean ishttps) {
//		InputStream is = null;
//		String responseBody = null;
//		String scheme = "http";
//		if (ishttps) {
//			scheme = "https"; //for https requests
//		}
//		boolean runFSMFFromAWS = PropertyRetriever.getBooleanProperty("msfFromAWS", false);
//		if (runFSMFFromAWS) {
//			scheme = "https"; //for https requests
//		}
//		CloseableHttpClient httpClient = null;
//		try {
//			// Set up Basic Authentication credentials
//			CredentialsProvider credProvider = new BasicCredentialsProvider();
//			//TODO: change so id and pw are passed in as args or come from properties
//			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, userAccessCode);
//			credProvider.setCredentials(AuthScope.ANY, credentials);
//
//			// Build a POST request
//			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//			httpClient = httpClientBuilder.setDefaultCredentialsProvider(credProvider).build();
//			URI uri = new URIBuilder().setScheme(scheme).setHost(hostDomain).setPath(restPath).build();
//			HttpPost httpPost = new HttpPost(uri);
//			logger.info("POST URI: " + httpPost.getURI());
//			HttpEntity reqEntity = builder.build();
//			httpPost.setEntity(reqEntity);
//			//added Geta Sometimes doesnt construct by default
//			// since we already trust the server, it's probably easiest to just construct the authorization header ourselfs.
//			//By default, httpclient will not provide credentials preemptively, it will first create a HTTP request
//			//without authentication parameters. This is by design, as a security precaution, and as part of the spec.
//			//But, this causes issues if you don't retry the connection, or wherever you're connecting to expects you to send
//			//authentication details on the first connection. It also causes extra latency to a request, as you need to make
//			//multiple calls, and causes 401s to appear in the logs.
//
//			//SonarQube complains about the Basic authentication
//			//Security of basic authentication
//			//As the user ID and password are passed over the network as clear text (it is base64 encoded, but base64 is a reversible encoding),
//			//the basic authentication scheme is not secure. HTTPS/TLS should be used with basic authentication. Without these additional security
//			//enhancements, basic authentication should not be used to protect sensitive or valuable information.
//			byte[] buildCredentials = Base64.encodeBase64((userName + ":" + userAccessCode).getBytes(StandardCharsets.UTF_8));
//			httpPost.setHeader("Authorization", "Basic " + new String(buildCredentials, StandardCharsets.UTF_8));
//
//			// Send the POST request
//			CloseableHttpResponse response = httpClient.execute(httpPost);
//			if (response.getStatusLine().getStatusCode() != 200) {
//				processError("Error occurred submitting HTTP post: " + response.getStatusLine().getStatusCode());
//				processError(response.getStatusLine().getReasonPhrase());
//				HttpEntity entity = response.getEntity();
//				if (entity != null) {
//					is = entity.getContent();
//					responseBody = convertInputStreamToString(is);
//					return responseBody;
//				}
//				logger.error("Entity is null from response.getentity. Returning blank response.");
//				return "";
//			}
//
//			HttpEntity entity = response.getEntity();
//			if (entity == null) {
//				processError("Entity from http response was null.");
//				return "";
//			}
//
//			// Handle the POST response
//			logger.info("HTTP Response Code: " + response.getStatusLine().getStatusCode());
//			is = entity.getContent();
//			responseBody = convertInputStreamToString(is);
//			logger.info("Response Body: " + responseBody);
//		} catch (URISyntaxException e) {
//			processError("Error building URI", e);
//		} catch (IOException e) {
//			processError("Error getting response", e);
//		} finally {
//			try {
//				if (is != null) is.close();
//				if (httpClient != null) httpClient.close();
//			} catch (IOException e) {
//				processError("Problem closing input stream", e);
//			}
//		}
//
//		return responseBody;
//	}
//
//	/**
//	 * Send and receive a POST request to the Landscape REST service
//	 *
//	 * @restURI  the URI of the rest service
//	 * @formParams restPath additional URL path
//	 * @return  the response body as a String
//	 */
//	public String sendAndReceiveRestPostForm(String hostDomain, String restPath, List<NameValuePair> formParams) {
//		InputStream is = null;
//		String responseBody = null;
//
//		CloseableHttpClient httpClient = null;
//		try {
//			boolean runFSMFFromAWS = PropertyRetriever.getBooleanProperty("msfFromAWS", false);
//			String scheme = "http";
//			if (runFSMFFromAWS) {
//				scheme = "https"; //for https requests
//			}
//			// Set up Basic Authentication credentials
//			CredentialsProvider credProvider = new BasicCredentialsProvider();
//			//TODO: change so id and pw are passed in as args or come from properties
//			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, userAccessCode);
//			credProvider.setCredentials(AuthScope.ANY, credentials);
//
//			// Build a POST request
//			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//			httpClient = httpClientBuilder.setDefaultCredentialsProvider(credProvider).build();
//			URI uri = new URIBuilder().setScheme(scheme).setHost(hostDomain).setPath(restPath).build();
//			HttpPost httpPost = new HttpPost(uri);
//			logger.info("POST URI: " + httpPost.getURI()); ///////////
//			httpPost.setEntity(new UrlEncodedFormEntity(formParams));
//			//Security of basic authentication
//			//As the user ID and password are passed over the network as clear text (it is base64 encoded, but base64 is a reversible encoding),
//			//the basic authentication scheme is not secure. HTTPS/TLS should be used with basic authentication. Without these additional security
//			//enhancements, basic authentication should not be used to protect sensitive or valuable information.
//			byte[] buildCredentials = Base64.encodeBase64((userName + ":" + userAccessCode).getBytes(StandardCharsets.UTF_8));
//			httpPost.setHeader("Authorization", "Basic " + new String(buildCredentials, StandardCharsets.UTF_8));
//
//			// Send the POST request
//			CloseableHttpResponse response = httpClient.execute(httpPost);
//			if (response.getStatusLine().getStatusCode() != 200) {
//				processError("Error occurred submitting HTTP post: " + response.getStatusLine().getStatusCode());
//				processError(response.getStatusLine().getReasonPhrase());
//				return "";
//			}
//
//			HttpEntity entity = response.getEntity();
//			if (entity == null) {
//				processError("Entity from http response was null.");
//				return "";
//			}
//
//			// Handle the POST response
//			logger.info("HTTP Response Code: " + response.getStatusLine().getStatusCode());
//			is = entity.getContent();
//			responseBody = convertInputStreamToString(is);
//			logger.info("Response Body: " + responseBody);
//		} catch (URISyntaxException e) {
//			processError("Error building URI", e);
//		} catch (IOException e) {
//			processError("Error getting response", e);
//		} finally {
//			try {
//				if (is != null) is.close();
//				if (httpClient != null) httpClient.close();
//			} catch (IOException e) {
//				processError("Problem closing input stream", e);
//			}
//		}
//
//		return responseBody;
//	}
//
//	private String addErrorDetails(int httpStatusCode, CloseableHttpResponse response, String responseBody) {
//		String failureReason = response.getStatusLine().getReasonPhrase();
//		failureReason = failureReason == null || failureReason.isEmpty() ? " (no additional information is available) " : failureReason;
//		String msg;
//		if (responseBody == null || responseBody.isEmpty()) {
//			switch (httpStatusCode) {
//				case 400:
//					msg = "Invalid model inputs provided ";
//					break;
//				case 500:
//					msg = "Internal FMSF Server error ";
//					break;
//				default:
//					msg = "FMSF error ";
//					break;
//			}
//		} else {
//			msg = responseBody;
//		}
//
//		return msg + failureReason;
//	}
}
