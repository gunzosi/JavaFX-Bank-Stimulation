package com.jmc.mazebank.Controllers.Admin;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Views.AdminMenoOptions;
import com.jmc.mazebank.Views.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button accounts_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void addListeners() {
        // 105. handling logout
    }

   private void onCreateClient() {
       Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenoOptions.CREATE_CLIENT);
   }

   private void onClients() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenoOptions.CLIENTS);
   }

    private void onDeposit() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenoOptions.DEPOSIT);
    }

    private void onLogout() {
        // Get Stage
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        // Close Stage (Admin window)
        Model.getInstance().getViewFactory().closeStage(stage);
        // Show Login Window
        Model.getInstance().getViewFactory().showLoginWindow();
        // Set Client login success flag to false
        Model.getInstance().setAdminLoginSuccessFlag(false);

    }
}
