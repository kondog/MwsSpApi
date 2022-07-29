package main.java.japacomo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ReportStatus {
    private String reportType;
    private String processingStatus;
    private String reportDocumentId;

    public String getReportType(){return this.reportType;}
    public String getProcessingStatus(){return this.processingStatus;}
    public String getReportDocumentId(){return this.reportDocumentId;}

}
