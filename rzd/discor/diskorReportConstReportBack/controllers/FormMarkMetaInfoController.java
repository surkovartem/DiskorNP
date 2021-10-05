package ru.rzd.discor.diskorReportConstReportBack.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.customElements.LabelWithTooltip;
import ru.rzd.discor.diskorReportConstReportBack.models.currentProject.cellFormMarkInfo.CellFormMarkInfo;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.*;
import ru.rzd.discor.diskorReportConstReportBack.services.GeneralParamsService;

import java.io.IOException;
import java.util.ArrayList;

@Getter
@Setter
@ToString
public class FormMarkMetaInfoController extends AnchorPane {
    private final GeneralParamsService generalParamsService = Main.generalParamsService;
    Stage stage;
    @FXML
    private Label MetaP1,
            MetaP2,
            MetaP3,
            MetaP4,
            MetaP5,
            MetaP6,
            MetaP7,
            MetaP8,
            MetaP9,
            MetaP10,
            MetaPriority,
            MetaAsSourceInformation,
            MetaCompAsSourceInf,
            MetaCompAsToPokValue,
            MetaPok,
            MetaValuePok,
            MetaOffsetDate,
            MetaHourReport;
    @FXML
    private LabelWithTooltip MetaP1Text,
            MetaP2Text,
            MetaP3Text,
            MetaP4Text,
            MetaP5Text,
            MetaP6Text,
            MetaP7Text,
            MetaP8Text,
            MetaP9Text,
            MetaP10Text,
            MetaPriorityText,
            MetaAsSourceInformationText,
            MetaCompAsSourceInfText,
            MetaCompAsToPokValueText,
            MetaPokText,
            MetaValuePokText,
            MetaOffsetDateText,
            MetaHourReportText;

    public FormMarkMetaInfoController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/FormMarkMetaInfo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void initialize() {
        setElementsStyle();
        setElementsAction();
    }

    public void setInitialParams(AttributesForNumericalPokValue identicalFieldsInAttributesForNumericalPokValue) {
        setCellInfoInModal(identicalFieldsInAttributesForNumericalPokValue);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setElementsStyle() {
        //Подчеркинвание строк в блоке "Мета данные"
        MetaPriority.setUnderline(true);
        MetaAsSourceInformation.setUnderline(true);
        MetaCompAsSourceInf.setUnderline(true);
        MetaCompAsToPokValue.setUnderline(true);
        MetaPok.setUnderline(true);
        MetaValuePok.setUnderline(true);
        MetaP1.setUnderline(true);
        MetaP2.setUnderline(true);
        MetaP3.setUnderline(true);
        MetaP4.setUnderline(true);
        MetaP5.setUnderline(true);
        MetaP6.setUnderline(true);
        MetaP7.setUnderline(true);
        MetaP8.setUnderline(true);
        MetaP9.setUnderline(true);
        MetaP10.setUnderline(true);
        MetaHourReport.setUnderline(true);
        MetaOffsetDate.setUnderline(true);
    }

    private void setCellInfoInModal(AttributesForNumericalPokValue identicalFieldsInAttributesForNumericalPokValue) {
        String elementText = "";
        PriorityTypeForNumericalPokValue priority = null;
        AsSourceInformationForNumericalPokValue asSourceInformation = null;
        CompAsSourceInfForNumericalPokValue compAsSourceInf = null;
        CompAsToPokValueForNumericalPokValue compAsToPokValue = null;
        PokForNumericalPokValue pok = null;
        PokValueForNumericalPokValue pokValue = null;
        PermissibleValuePokForNumericalPokValue permissibleValuePok = null;
        EdIzmForNumericalPokValue edIzm = null;
        ParamPokInfoForNumericalPokValue p1 = null;
        ParamPokInfoForNumericalPokValue p2 = null;
        ParamPokInfoForNumericalPokValue p3 = null;
        ParamPokInfoForNumericalPokValue p4 = null;
        ParamPokInfoForNumericalPokValue p5 = null;
        ParamPokInfoForNumericalPokValue p6 = null;
        ParamPokInfoForNumericalPokValue p7 = null;
        ParamPokInfoForNumericalPokValue p8 = null;
        ParamPokInfoForNumericalPokValue p9 = null;
        ParamPokInfoForNumericalPokValue p10 = null;
        OffsetDateForNumericalPokValue offsetDate = null;
        Integer hourReport = null;
        if (identicalFieldsInAttributesForNumericalPokValue != null) {
            priority = identicalFieldsInAttributesForNumericalPokValue.getPriority();
            asSourceInformation = identicalFieldsInAttributesForNumericalPokValue.getAsSourceInformation();
            compAsSourceInf = identicalFieldsInAttributesForNumericalPokValue.getCompAsSourceInf();
            compAsToPokValue = identicalFieldsInAttributesForNumericalPokValue.getCompAsToPokValue();
            pok = identicalFieldsInAttributesForNumericalPokValue.getPok();
            pokValue = identicalFieldsInAttributesForNumericalPokValue.getPokValue();
            permissibleValuePok = identicalFieldsInAttributesForNumericalPokValue.getPermissibleValuePok();
            edIzm = identicalFieldsInAttributesForNumericalPokValue.getEdIzm();
            p1 = identicalFieldsInAttributesForNumericalPokValue.getP1();
            p2 = identicalFieldsInAttributesForNumericalPokValue.getP2();
            p3 = identicalFieldsInAttributesForNumericalPokValue.getP3();
            p4 = identicalFieldsInAttributesForNumericalPokValue.getP4();
            p5 = identicalFieldsInAttributesForNumericalPokValue.getP5();
            p6 = identicalFieldsInAttributesForNumericalPokValue.getP6();
            p7 = identicalFieldsInAttributesForNumericalPokValue.getP7();
            p8 = identicalFieldsInAttributesForNumericalPokValue.getP8();
            p9 = identicalFieldsInAttributesForNumericalPokValue.getP9();
            p10 = identicalFieldsInAttributesForNumericalPokValue.getP10();
            offsetDate = identicalFieldsInAttributesForNumericalPokValue.getOffsetDate();
            hourReport = identicalFieldsInAttributesForNumericalPokValue.getHourReport();
        }
        if (priority != null) {
            if (priority.getRealName() != null) {
                elementText += priority.getRealName();
            } else {
                elementText += "_";
            }
        } else {
            elementText += "";
        }
        MetaPriorityText.setText(elementText);
        elementText = "";
        if (asSourceInformation != null) {
            if (asSourceInformation.getId() != null) {
                elementText += "[" + asSourceInformation.getId() + "]";
            } else {
                elementText += "[]";
            }
            if (asSourceInformation.getName() != null) {
                elementText += " " + asSourceInformation.getName();
            } else {
                elementText += " _";
            }
        } else {
            elementText += "";
        }
        MetaAsSourceInformationText.setText(elementText);
        elementText = "";
        if (compAsSourceInf != null) {
            if (compAsSourceInf.getId() != null) {
                elementText += "[" + compAsSourceInf.getId() + "]";
            } else {
                elementText += "[]";
            }
            if (compAsSourceInf.getName() != null) {
                elementText += " " + compAsSourceInf.getName();
            } else {
                elementText += " _";
            }
        } else {
            elementText += "";
        }
        MetaCompAsSourceInfText.setText(elementText);
        elementText = "";
        if (compAsToPokValue != null) {
            if (compAsToPokValue.getId() != null) {
                elementText += "[" + compAsToPokValue.getId() + "]";
            } else {
                elementText += "[]";
            }
            if (compAsToPokValue.getName() != null) {
                elementText += " " + compAsToPokValue.getName();
            } else {
                elementText += " _";
            }
        } else {
            elementText += "";
        }
        MetaCompAsToPokValueText.setText(elementText);
        elementText = "";
        if (pok != null) {
            if (pok.getId() != null) {
                elementText += "[" + pok.getId() + "]";
            } else {
                elementText += "[]";
            }
            if (pok.getName() != null) {
                elementText += " " + pok.getName();
            } else {
                elementText += " _";
            }
        } else {
            elementText += "";
        }
        MetaPokText.setText(elementText);
        elementText = "";
        if (pokValue == null && permissibleValuePok == null && edIzm == null) {
            elementText += "";
        } else {
            if (pokValue != null) {
                if (pokValue.getId() != null) {
                    elementText += "[" + pokValue.getId() + "]";
                } else {
                    elementText += "[]";
                }
            }
            elementText += " (";
            if (permissibleValuePok != null) {
                if (permissibleValuePok.getId() != null) {
                    elementText += "[" + permissibleValuePok.getId() + "]";
                } else {
                    elementText += "[]";
                }
                if (permissibleValuePok.getName() != null) {
                    elementText += " " + permissibleValuePok.getName();
                } else {
                    elementText += " _";
                }
            } else {
                elementText += "[] _";
            }
            elementText += " - ";
            if (edIzm != null) {
                if (edIzm.getId() != null) {
                    elementText += "[" + edIzm.getId() + "]";
                } else {
                    elementText += "[]";
                }
                elementText += " ";
                if (edIzm.getName() != null) {
                    elementText += edIzm.getName();
                } else {
                    elementText += "_";
                }
            } else {
                elementText += "[] _";
            }
            elementText += ")";
        }
        MetaValuePokText.setText(elementText);
        MetaP1Text.setText(getParamPokTextForCellInfoInModal(
                "p1",
                p1
        ));
        MetaP2Text.setText(getParamPokTextForCellInfoInModal(
                "p2",
                p2
        ));
        MetaP3Text.setText(getParamPokTextForCellInfoInModal(
                "p3",
                p3
        ));
        MetaP4Text.setText(getParamPokTextForCellInfoInModal(
                "p4",
                p4
        ));
        MetaP5Text.setText(getParamPokTextForCellInfoInModal(
                "p5",
                p5
        ));
        MetaP6Text.setText(getParamPokTextForCellInfoInModal(
                "p6",
                p6
        ));
        MetaP7Text.setText(getParamPokTextForCellInfoInModal(
                "p7",
                p7
        ));
        MetaP8Text.setText(getParamPokTextForCellInfoInModal(
                "p8",
                p8
        ));
        MetaP9Text.setText(getParamPokTextForCellInfoInModal(
                "p9",
                p9
        ));
        MetaP10Text.setText(getParamPokTextForCellInfoInModal(
                "p10",
                p10
        ));
        elementText = "";
        if (offsetDate != null) {
            if (offsetDate.getId() != null) {
                elementText += "[" + offsetDate.getId() + "]";
            } else {
                elementText += "[]";
            }
            if (offsetDate.getName() != null) {
                elementText += " " + offsetDate.getName();
            } else {
                elementText += " _";
            }
        } else {
            //OffsetDate может принимать значение null
            //Поэтому, если в identicalFieldsValuesForListSelectedNumericalPokValues.get("offsetDate") лежит null
            //(значит, есть два варианта: все элементы нулевые, либо есть различающиеся элементы)
            //то будем проверять среди всех элементов, есть ли ненулевой элемент, чтобы понять какой текст отображать.
            //Если все нулевые, то "Все элементы пустые", если есть и нулевой и ненулевой элементы, то "")
            for (CellFormMarkInfo cellFormMarkInfo : generalParamsService.getListSelectedCellFormMarkInfo()) {
                NumericalPokValue numericalPokValue = cellFormMarkInfo.getNumericalPokValue();
                OffsetDateForNumericalPokValue offsetDateForNumericalPokValue = numericalPokValue.getAttributes().getOffsetDate();
                if (offsetDateForNumericalPokValue != null) {
                    elementText += "";
                    break;
                }
                if (generalParamsService.getListSelectedCellFormMarkInfo().indexOf(cellFormMarkInfo) == generalParamsService.getListSelectedCellFormMarkInfo().size() - 1) {
                    elementText += "Смещение по дате отсуствует";
                    break;
                }
            }
        }
        MetaOffsetDateText.setText(elementText);
        elementText = "";
        if (hourReport != null) {
            elementText += hourReport;
        } else {
            elementText += "";
        }
        MetaHourReportText.setText(elementText);
    }

    private String getParamPokTextForCellInfoInModal(
            String paramPokCode,
            ParamPokInfoForNumericalPokValue paramPokInfoForNumericalPokValue
    ) {
        if (paramPokInfoForNumericalPokValue == null) {
            return "";
        }
        HandbookForNumericalPokValue handbookForNumericalPokValue = paramPokInfoForNumericalPokValue.getHandbook();
        ParamPokForNumericalPokValue paramPokForNumericalPokValue = paramPokInfoForNumericalPokValue.getParamPok();
        HandbookRowForNumericalPokValue handbookRowForNumericalPokValue = paramPokInfoForNumericalPokValue.getHandbookRow();
        if (handbookForNumericalPokValue == null && paramPokForNumericalPokValue == null && handbookRowForNumericalPokValue == null) {
            return "";
        }
        String paramPokTextForCellInfoInModal = "";
        if (handbookForNumericalPokValue == null) {
            return "";
        }
        if (paramPokForNumericalPokValue != null) {
            //Если paramPokForNumericalPokValue, то это точная гарантия того, что listSelectedNumericalPokValue не равен null и имеет длину больше 0,
            //что все параметры показателя c кодом paramPokCode отличны от null
            //Смотри метод setIdenticalFieldsValuesForListSelectedNumericalPokValues

            //Добавление id параметра показателя
            //Так как одинаковые параметры показателей определяются по имени (используется метода ParamPokForNumericalPokValue.equalsByName),
            //то приходится определять есть ли одинаковый ИД для всех выделенных параметров показателей
            ArrayList<Integer> listParamPokForNumericalPokValueId = new ArrayList<Integer>();
            AttributesForNumericalPokValue attributesForNumericalPokValue;
            for (CellFormMarkInfo cellFormMarkInfo : generalParamsService.getListSelectedCellFormMarkInfo()) {
                NumericalPokValue numericalPokValue = cellFormMarkInfo.getNumericalPokValue();
                attributesForNumericalPokValue = numericalPokValue.getAttributes();
                if (paramPokCode.equals("p1")) {
                    listParamPokForNumericalPokValueId.add(attributesForNumericalPokValue.getP1().getParamPok().getId());
                } else if (paramPokCode.equals("p2")) {
                    listParamPokForNumericalPokValueId.add(attributesForNumericalPokValue.getP2().getParamPok().getId());
                } else if (paramPokCode.equals("p3")) {
                    listParamPokForNumericalPokValueId.add(attributesForNumericalPokValue.getP3().getParamPok().getId());
                } else if (paramPokCode.equals("p4")) {
                    listParamPokForNumericalPokValueId.add(attributesForNumericalPokValue.getP4().getParamPok().getId());
                } else if (paramPokCode.equals("p5")) {
                    listParamPokForNumericalPokValueId.add(attributesForNumericalPokValue.getP5().getParamPok().getId());
                } else if (paramPokCode.equals("p6")) {
                    listParamPokForNumericalPokValueId.add(attributesForNumericalPokValue.getP6().getParamPok().getId());
                } else if (paramPokCode.equals("p7")) {
                    listParamPokForNumericalPokValueId.add(attributesForNumericalPokValue.getP7().getParamPok().getId());
                } else if (paramPokCode.equals("p8")) {
                    listParamPokForNumericalPokValueId.add(attributesForNumericalPokValue.getP8().getParamPok().getId());
                } else if (paramPokCode.equals("p9")) {
                    listParamPokForNumericalPokValueId.add(attributesForNumericalPokValue.getP9().getParamPok().getId());
                } else if (paramPokCode.equals("p10")) {
                    listParamPokForNumericalPokValueId.add(attributesForNumericalPokValue.getP10().getParamPok().getId());
                }
            }
            Integer identicalParamPokForNumericalPokValueId = null;
            for (Integer paramPokForNumericalPokValueId : listParamPokForNumericalPokValueId) {
                if (paramPokForNumericalPokValueId == null) {
                    break;
                }
                if (listParamPokForNumericalPokValueId.indexOf(paramPokForNumericalPokValueId) == 0) {
                    identicalParamPokForNumericalPokValueId = paramPokForNumericalPokValueId;
                    continue;
                }
                if (!paramPokForNumericalPokValueId.equals(identicalParamPokForNumericalPokValueId)) {
                    identicalParamPokForNumericalPokValueId = null;
                    break;
                }
            }
            if (identicalParamPokForNumericalPokValueId != null) {
                //Добавление ИД параметра показателя
                paramPokTextForCellInfoInModal += "[" + identicalParamPokForNumericalPokValueId + "]";
            } else {
                paramPokTextForCellInfoInModal += "[]";
            }
            //Добавление имени параметра показателя
            paramPokTextForCellInfoInModal += " " + (paramPokForNumericalPokValue.getName() != null ? paramPokForNumericalPokValue.getName() : "_");
        } else {
            paramPokTextForCellInfoInModal += "[] _";
        }
        //Добавление информации о классификаторе
        paramPokTextForCellInfoInModal += " (";
        paramPokTextForCellInfoInModal += "[" + (handbookForNumericalPokValue.getId() != null ? handbookForNumericalPokValue.getId() : "") + "] " +
                (handbookForNumericalPokValue.getName() != null ? handbookForNumericalPokValue.getName() : "_") + ")";
        //Добавление информации о строке классификатора
        paramPokTextForCellInfoInModal += " -> ";
        if (handbookRowForNumericalPokValue != null) {
            paramPokTextForCellInfoInModal += "[" + (handbookRowForNumericalPokValue.getId() != null ? handbookRowForNumericalPokValue.getId() : "") + "] " +
                    (handbookRowForNumericalPokValue.getName() != null ? handbookRowForNumericalPokValue.getName() : "_");
        } else {
            paramPokTextForCellInfoInModal += "[] _";
        }
        paramPokTextForCellInfoInModal += ")";
        return paramPokTextForCellInfoInModal;
    }


    public void setElementsAction() {

    }
}
