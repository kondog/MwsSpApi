package main.java.japacomo;

import com.amazon.SellingPartnerAPIAA.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
public class CallMwsApi {
    final String spapiAuth = "https://sellingpartnerapi-na.amazon.com";
    final String mediaType = "application/json; charset=utf-8";
    private TakeSpecifiedProperty prop = new TakeSpecifiedProperty();
    static LoggingJapacomo lj = new LoggingJapacomo();
    static Logger logger = lj.logger;

    public Request getOrders(){
        String MarketID =  prop.getProperty("marketID");
        MediaType MIMEType= MediaType.parse(mediaType);
        Request request = new Request.Builder()
                .url(spapiAuth + "/orders/v0/orders?MarketplaceIds=" + MarketID + "&CreatedAfter=2022-04-01")
                .get()
                .build()
                ;
        return request;
    }
    public Request makeReportRequest(
            String reportType,String dateStart, String dateEnd){
        MediaType MIMEType= MediaType.parse(mediaType);
        StringBuffer reqBody = new StringBuffer();
        reqBody.append("{");
        reqBody.append("\"reportType\":\"" + reportType + "\",");
        reqBody.append("\"dataStartTime\":\"" +  dateStart + "\",");
        reqBody.append("\"dataEndTime\":\"" + dateEnd + "\",");
        reqBody.append("\"marketplaceIds\":[\"" + prop.getProperty("marketID") + "\"]");
        reqBody.append("}");

        Request request = new Request.Builder()
                .url(spapiAuth + "/reports/2021-06-30/reports")
                .post(RequestBody.create(MIMEType, reqBody.toString()))
                .build()
                ;
        return request;
    }

    public LWAAuthorizationCredentials makeLwaAuthCredentials() {
        String clientId = prop.getProperty("clientId");
        String clientSecret = prop.getProperty("clientSecret");
        String refreshToken = prop.getProperty("refreshToken");

        LWAAuthorizationCredentials lwaAuthorizationCredentials = LWAAuthorizationCredentials.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .refreshToken(refreshToken)
                .endpoint("https://api.amazon.com/auth/o2/token")
                .build();
        return lwaAuthorizationCredentials;
    }

    public AWSAuthenticationCredentials makeAwsAuthCredentials(){
        String accessKeyId = prop.getProperty("accessKeyId");
        String secretKey = prop.getProperty("secretKey");

        AWSAuthenticationCredentials awsAuthenticationCredentials=AWSAuthenticationCredentials.builder()
                .accessKeyId(accessKeyId)
                .secretKey(secretKey)
                .region(prop.getProperty("region"))
                .build();
        return awsAuthenticationCredentials;
    }

    public AWSAuthenticationCredentialsProvider makeAwsAuthCredentialsProvider(){
        String roleArn = prop.getProperty("roleArn");
        String roleSessionName = prop.getProperty("roleSessionName");

        AWSAuthenticationCredentialsProvider awsAuthenticationCredentialsProvider=AWSAuthenticationCredentialsProvider.builder()
                .roleArn(roleArn)
                .roleSessionName(roleSessionName)
                .build();
        return awsAuthenticationCredentialsProvider;
    }

    public String callRequest(Request req){
        logger.log(Level.INFO, req.toString());
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(req);
        Response response = null;
        try {
            response = call.execute();
            ResponseBody resBody = response.body();
            return resBody.string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Request makeRequestSigned(Request request){
        LWAAuthorizationCredentials lwaAuthorizationCredentials = this.makeLwaAuthCredentials();
        AWSAuthenticationCredentials awsAuthenticationCredentials = this.makeAwsAuthCredentials();
        AWSAuthenticationCredentialsProvider awsAuthenticationCredentialsProvider =
                this.makeAwsAuthCredentialsProvider();
        AWSSigV4Signer v4signer;
        v4signer = new AWSSigV4Signer(awsAuthenticationCredentials, awsAuthenticationCredentialsProvider);
        Request signedRequest =
                new LWAAuthorizationSigner(lwaAuthorizationCredentials)
                        .sign(request);
        return v4signer.sign(signedRequest);
    }

    public String takeReportID(String reportType, String dateStart, String dateEnd){
        Request request = this.makeReportRequest(reportType, dateStart, dateEnd);
        Request signedRequest = this.makeRequestSigned(request);
        String reportIDJson = this.callRequest(signedRequest);
        logger.log(Level.INFO, reportIDJson);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ReportID reportID = mapper.readValue(reportIDJson, ReportID.class);
            return reportID.reportId;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Request takeReportRequestFromID(String reportID) {
        MediaType MIMEType= MediaType.parse(mediaType);
        Request request = new Request.Builder()
                .url(spapiAuth + "/reports/2021-06-30/reports/" + reportID)
                .get()
                .build()
                ;
        return request;
    }

    public String waitUntilReportCompleted(String reportID){
        while(true) {
            Request request = this.takeReportRequestFromID(reportID);
            Request signedRequest = this.makeRequestSigned(request);
            String reportStatusJson = this.callRequest(signedRequest);
            logger.log(Level.INFO, reportStatusJson);
            String reportDocumentId = takeReportDocumentID(reportStatusJson);
            if(!reportDocumentId.equals("")){
                return reportDocumentId;
            }
            try {
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String takeReportDocumentID(String reportStatusJson){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ReportStatus reportStatus = mapper.readValue(reportStatusJson, ReportStatus.class);
            logger.log(Level.INFO, reportStatus.getProcessingStatus());
            if (reportStatus.getProcessingStatus() == null) {
                return "";
            }
            if(reportStatus.getProcessingStatus().equals("DONE")) {
                return reportStatus.getReportDocumentId();
            }
            else {
                return "";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Request takeReportRequestFromDocumentID(String documentID){
        MediaType MIMEType= MediaType.parse(mediaType);
        Request request = new Request.Builder()
                .url(spapiAuth + "/reports/2021-06-30/documents/" + documentID)
                .get()
                .build()
                ;
        return request;
    }
    public String takeReportAccessURL(String documentID){
        logger.log(Level.INFO, documentID);
        Request request = this.takeReportRequestFromDocumentID(documentID);
        Request signedRequest = this.makeRequestSigned(request);
        String reportStatusJson = this.callRequest(signedRequest);
        logger.log(Level.INFO, reportStatusJson);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ReportDocument reportDoc = mapper.readValue(reportStatusJson, ReportDocument.class);
                return reportDoc.getUrl();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void takeReportFromURL(String downloadPath, String srcURL){
        logger.log(Level.INFO, downloadPath);
        logger.log(Level.INFO, srcURL);

        try (BufferedInputStream in = new BufferedInputStream(new URL(srcURL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(downloadPath)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // handle exception
        }
    }
    public void unzipFile(String srcStr, String dstStr){
        Path src = Paths.get(srcStr);
        Path dst = Paths.get(dstStr);

        try (GZIPInputStream in = new GZIPInputStream(Files.newInputStream(src));
             OutputStream out = Files.newOutputStream(dst)) {
            int len;
            byte[] b = new byte[1024 * 4];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
