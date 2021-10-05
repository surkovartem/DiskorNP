package ru.rzd.discor.diskorReportConstReportBack.controllers;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.rzd.discor.diskorReportConstReportBack.models.core.GroupPok;

public class GroupPokController extends AnchorPane {
    FormMarkController formMarkController = null;
    TreeItem<GroupPok> selectedTreeItem = null;

    @FXML private TreeView<GroupPok> TreeViewID;
    @FXML private Button SelectGroupPok;

    public GroupPokController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GroupPok.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void setTreeElementChildren(
            ArrayList<GroupPok> listChildrenGroupPok,
            TreeItem<GroupPok> parentElement,
            GroupPok oldGroupPok
    ) {
        if (listChildrenGroupPok == null) {
            return;
        }
        for (GroupPok childGroupPok : listChildrenGroupPok) {
            if (childGroupPok.getCorTip().equals("D")) {
                continue;
            }
            TreeItem<GroupPok> childRoot = new TreeItem<GroupPok>(childGroupPok);
            if (oldGroupPok != null && childGroupPok.getId().equals(oldGroupPok.getId())) {
                selectedTreeItem = childRoot;
            }
            parentElement.getChildren().add(childRoot);
            setTreeElementChildren(childGroupPok.getListGroupPok(), childRoot, oldGroupPok);
        }
    }

    @FXML void PressedButtonSelect() {
        try {
            TreeItem<GroupPok> selectedItem = TreeViewID.getSelectionModel().getSelectedItem();
            formMarkController.groupPokProperty.setValueCustom(selectedItem.getValue());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        Stage stage = (Stage) SelectGroupPok.getScene().getWindow();
        stage.close();
    }

    @FXML void initialize() {
        assert TreeViewID != null : "fx:id=\"TreeViewID\" was not injected: check your FXML file 'SelectedGroupPok.fxml'.";
        assert SelectGroupPok != null : "fx:id=\"SelectGroupPok\" was not injected: check your FXML file 'SelectedGroupPok.fxml'.";
    }

    //Вызывается из ConstructorModeController, чтобы передать параметры
    //В любом случаем вызывается после initialize, так как controller и initialize вызываются при загрузке компоненты
    public void setInitialParams(FormMarkController formMarkController, GroupPok rootGroupPok, GroupPok oldGroupPok) {
        this.formMarkController = formMarkController;

        TreeItem<GroupPok> rootTreeItem = new TreeItem<GroupPok>(rootGroupPok);

        TreeViewID.setRoot(rootTreeItem);
        TreeViewID.setCellFactory((TreeView<GroupPok> treeView) -> {
            return new TextFieldTreeCell<GroupPok>()
            {
                @Override
                public void updateItem(GroupPok groupPok, boolean isEmpty) {
                    super.updateItem(groupPok, isEmpty);
                    if (groupPok != null) {
                        setText("[" + groupPok.getId() + "] " + groupPok.getName());
                    }
                }
            };
        });
        setTreeElementChildren(rootTreeItem.getValue().getListGroupPok(), rootTreeItem, oldGroupPok);
        // Выделение выбранного до этого элемента
        if (selectedTreeItem != null) {
            TreeViewID.getSelectionModel().select(selectedTreeItem);
        }
    }

    Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
