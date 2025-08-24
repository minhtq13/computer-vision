package com.example.multiplechoiceexam.dto.upload;

import java.util.ArrayList;
import java.util.List;

public class ScoringPreviewResDTO {
    String tmpFileCode;

    List<ScoringPreviewItemDTO> previews = new ArrayList<>();

    List<String> warningMessages = new ArrayList<>();

    public String getTmpFileCode() {
        return tmpFileCode;
    }

    public void setTmpFileCode(String tmpFileCode) {
        this.tmpFileCode = tmpFileCode;
    }

    public List<ScoringPreviewItemDTO> getPreviews() {
        return previews;
    }

    public void setPreviews(List<ScoringPreviewItemDTO> previews) {
        this.previews = previews;
    }

    public List<String> getWarningMessages() {
        return warningMessages;
    }

    public void setWarningMessages(List<String> warningMessages) {
        this.warningMessages = warningMessages;
    }
}
