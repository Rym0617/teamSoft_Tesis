/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.myBeans;

import org.primefaces.event.FlowEvent;
import org.teamsoft.POJOS.MyJSF_Util;
import org.teamsoft.POJOS.PersonPerProjectAmount;
import org.teamsoft.POJOS.TeamFormationParameters;
import org.teamsoft.locale.LocaleConfig;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author G1lb3rt
 */
@Named("myTeamFormationController")
@SessionScoped
public class MyTeamFormationController implements Serializable {

    @Inject
    TF_StepOneController stepOne;
    @Inject
    TF_StepTwoController stepTwo;
    @Inject
    TF_StepThreeController stepThree;
    @Inject
    LocaleConfig localeConfig;

    public String tfWizardFlowListener(FlowEvent event) {
        String step = event.getNewStep();
        switch (step) {
            case "bossAsignment": {
                if (stepOne.getProjectList().isEmpty() || stepOne.getGroupList().isEmpty()) {
                    MyJSF_Util.addErrorMessage(localeConfig.getBundleValue("error_most_select_project_and_group"));
                    step = event.getOldStep();
                } else if (!stepOne.isMaximunRolesSet()){
                    MyJSF_Util.addErrorMessage(localeConfig.getBundleValue("error_most_set_max_cant_rol"));
                    step = event.getOldStep();
                }
                else if (stepOne.isConfAllGroup()) {
                    int personTotal = 0;
                    ArrayList<PersonPerProjectAmount> ppg = stepOne.getPpg();
                    for (PersonPerProjectAmount personPerProjectAmount : ppg) {
                        personTotal += personPerProjectAmount.getCant();
                    }
//                    if (personTotal != stepOne.getGroupListAmount()) {
//                        MyJSF_Util.addWarningMessage("La cantidad total de personal a asignar debe ser igual a la cantidad de personal disponibles");
//                        step = event.getOldStep();
//                    } else {
                    stepTwo.setParametersBoss(stepOne.initializeParameters());
//                    }
                } else {
                    stepTwo.setParametersBoss(stepOne.initializeParameters());
                }
            }
            break;

            case "teamFormation": {
                if (stepOne.getProjectList().isEmpty() || stepOne.getGroupList().isEmpty()) {
                    MyJSF_Util.addErrorMessage(localeConfig.getBundleValue("error_most_select_project_and_group"));
                    step = event.getOldStep();
                } else {
                    TeamFormationParameters param = stepOne.initializeParameters();
                    param.setFixedWorkers(stepTwo.getParametersBoss().getFixedWorkers());
                    stepThree.setParametersTeam(param);
                }

            }
            break;
        }
        return step;
    }

}
