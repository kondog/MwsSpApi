package japacomo;

import com.amazon.SellingPartnerAPIAA.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CallMwsApi {
    final String mediaType = "application/json; charset=utf-8";
    private TakeSpecifiedProperty prop;
    static LoggingJapacomo lj = new LoggingJapacomo();
    static Logger logger = lj.logger;

    public CallMwsApi(TakeSpecifiedProperty specifiedProp){
        prop = specifiedProp;
    }

    public Request getOrders(){
        String MarketID =  prop.getProperty("marketID");
        String spApiEndPoint = prop.getProperty("spApiEndPoint");
        MediaType MIMEType= MediaType.parse(mediaType);
        Request request = new Request.Builder()
                .url(spApiEndPoint + "/orders/v0/orders?MarketplaceIds=" + MarketID + "&CreatedAfter=2022-04-01")
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

        String spApiEndPoint = prop.getProperty("spApiEndPoint");
        Request request = new Request.Builder()
                .url(spApiEndPoint + "/reports/2021-06-30/reports")
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
        String spApiEndPoint = prop.getProperty("spApiEndPoint");
        Request request = new Request.Builder()
                .url(spApiEndPoint + "/reports/2021-06-30/reports/" + reportID)
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
            if(reportDocumentId.equals("FATAL")){
                return "FATAL";
            }
            if(!reportDocumentId.equals("")){
                return reportDocumentId;
            }
            try {
                Thread.sleep(1 * 60 * 1000);
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
            if(reportStatus.getProcessingStatus().equals("FATAL")) {
                logger.log(Level.WARNING, reportStatus.getProcessingStatus());
                return "FATAL";
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
        String spApiEndPoint = prop.getProperty("spApiEndPoint");
        Request request = new Request.Builder()
                .url(spApiEndPoint + "/reports/2021-06-30/documents/" + documentID)
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
            throw new RuntimeException(e);
        }
    }
    public void unzipFile(String srcStr, String dstStr) {
        Path src = Paths.get(srcStr);
        Path dst = Paths.get(dstStr);
        if (!Files.exists(src)){
            logger.log(Level.INFO, "file not found," + srcStr);
            return;
        }
        try {
            if (Files.exists(dst)){Files.delete(dst);}

            String contentType = Files.probeContentType(src);
            if (!contentType.equals("application/octet-stream")) {
                logger.log(Level.INFO, "file " + srcStr + " is not gz," + contentType);
                Files.copy(src, dst);
                return;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try (InputStream fi = Files.newInputStream(src);
             InputStream gzi = new GzipCompressorInputStream(fi);
             ArchiveInputStream in = new TarArchiveInputStream(gzi)) {

            ArchiveEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                if (!in.canReadEntryData(entry)) {
                    continue;
                }

                File file = new File(dstStr);
                if (entry.isDirectory()) {
                    if (!file.isDirectory() && !file.mkdirs()) {
                        throw new IOException("failed to create directory " + file);
                    }
                } else {
                    File parent = file.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("failed to create directory " + parent);
                    }
                    try (OutputStream o = Files.newOutputStream(file.toPath())) {
                        IOUtils.copy(in, o);
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
