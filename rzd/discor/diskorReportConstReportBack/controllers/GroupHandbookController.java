package ru.rzd.discor.diskorReportConstReportBack.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.rzd.discor.diskorReportConstReportBack.models.core.GroupHandbook;

import java.io.IOException;
import java.util.List;

public class GroupHandbookController extends AnchorPane {
    IterationGroupHandbookController iterationGroupHandbookController = null;
    TreeItem<GroupHandbook> selectedTreeItem = null;

    @FXML private TreeView<GroupHandbook> TreeViewID;
    @FXML private Button SelectGroupHandbook;

    public GroupHandbookController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GroupHandbook.fxml"));
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
            List<GroupHandbook> listChildrenGroupHandbook,
            TreeItem<GroupHandbook> parentElement,
            GroupHandbook oldGroupHandbook
    ) {
        if (listChildrenGroupHandbook == null) {
            return;
        }
        for (GroupHandbook childGroupHandbook : listChildrenGroupHandbook) {
            if (childGroupHandbook.getCorTip().equals("D")) {
                continue;
            }
            TreeItem<GroupHandbook> childRoot = new TreeItem<GroupHandbook>(childGroupHandbook);
            if (oldGroupHandbook != null && childGroupHandbook.getId().equals(oldGroupHandbook.getId())) {
                selectedTreeItem = childRoot;
            }
            parentElement.getChildren().add(childRoot);
            setTreeElementChildren(childGroupHandbook.getListGroupHandbook(), childRoot, oldGroupHandbook);
        }
    }

    @FXML void PressedButtonSelect() {
        try {
            TreeItem<GroupHandbook> selectedItem = TreeViewID.getSelectionModel().getSelectedItem();
            iterationGroupHandbookController.groupHandbookProperty.setValueCustom(selectedItem.getValue());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        Stage stage = (Stage) SelectGroupHandbook.getScene().getWindow();
        stage.close();
    }

    @FXML void initialize() {

    }

    //Вызывается из ConstructorModeController, чтобы передать параметры
    //В любом случаем вызывается после initialize, так как controller и initialize вызываются при загрузке компоненты
    public void setInitialParams(IterationGroupHandbookController iterationGroupHandbookController, GroupHandbook groupHandbook, GroupHandbook oldGroupHandbook) {
        this.iterationGroupHandbookController = iterationGroupHandbookController;

        TreeItem<GroupHandbook> rootTreeItem = new TreeItem<GroupHandbook>(groupHandbook);

        TreeViewID.setRoot(rootTreeItem);
        TreeViewID.setCellFactory((TreeView<GroupHandbook> treeView) -> {
            return new TextFieldTreeCell<GroupHandbook>()
            {
                @Override
                public void updateItem(GroupHandbook groupHandbook, boolean isEmpty) {
                    super.updateItem(groupHandbook, isEmpty);
                    if (groupHandbook != null) {
                        setText("[" + groupHandbook.getId() + "] " + groupHandbook.getName());
                    }
                }
            };
        });
        setTreeElementChildren(rootTreeItem.getValue().getListGroupHandbook(), rootTreeItem, oldGroupHandbook);
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
