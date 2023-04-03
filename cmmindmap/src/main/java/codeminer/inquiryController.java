package codeminer;

import codeminer.core.MNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InquiryController {

    @FXML
    private Button inquireCancelButton;

    @FXML
    private Button inquireDoNotSaveButton;

    @FXML
    private Button inquireSaveButton;

    void initializeInquiry(){

        initializeInquireCancelButton();
        initializeInquireDoNotSaveButton();
        initializeInquireSaveButton();

    }

    private void initializeInquireSaveButton() {


    }

    private void initializeInquireDoNotSaveButton() {
        Stage stage = (Stage) MNode.getRootNode().getScene().getWindow();
        stage.close();
    }

    private void initializeInquireCancelButton() {


    }


}
