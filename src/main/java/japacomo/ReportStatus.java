package main.java.japacomo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ReportStatus {
    private String reportType;
    private String processingStatus;
//  private  List<String> marketplaceIds;
//  private  String reportId;
//  private  String dateEndTime;
//  private  String createdTime;
//  private  String processingStartTime;
//  private  String dataStartTime;
    private String reportDocumentId;

    public String getReportType(){return this.reportType;}
    public String getProcessingStatus(){return this.processingStatus;}
    public String getReportDocumentId(){return this.reportDocumentId;}

}
